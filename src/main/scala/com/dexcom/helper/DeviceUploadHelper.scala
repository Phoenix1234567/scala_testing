package com.dexcom.helper

import com.dexcom.common.{CassandraQueries, Constants}
import com.dexcom.connection.CassandraConnection
import com.dexcom.dto.{AlertSetting, DeviceUploadForPatient}
import com.dexcom.utils.Utils._

import scala.collection.mutable.ListBuffer

/**
  * Created by gaurav.garg on 23-01-2017.
  */
object DeviceUploadHelper extends CassandraQueries with App{

  /**
    * thiss method get the alertsettingsrecord from CSV
    * @return the list of AlertSetting records
    */
  def getAlertsRecordFromCSV : List[AlertSetting] = {

    val list_alerts = new ListBuffer[AlertSetting]
    val alerts_record_csv = scala.io.Source.fromFile(alert_settings_record_path)

    for(line <- alerts_record_csv.getLines().drop(1)) {
      val alert_record = AlertSetting(
        name = line(2).toString,
        value = line(3).toInt,
        system_time = line(0).toString,
        display_time = line(1).toString,
        units = units(line(2).toString),
        delay = line(4).toInt,
        snooze = line(5).toInt,
        enabled = line(7).toString.toBoolean
      )

      list_alerts += alert_record
    }
    list_alerts.toList
  }

  /**
    * this method returns the unit of alert setting
    * @param alertName of te alert
    * @return the alerts' unit
    */
  def units(alertName: String): String =
    alertName match {
      case "Fall" => "mg/dl/min"
      case "FixedLow" => "mg/dl"
      case "High" => "mg/dl"
      case "Low" => "mg/dl"
      case "OutOfRange" => "min"
      case "Rise" => "mg/dl/min"
      case _ => "???"
    }

  /**
    * this method fethces data from CSV
    * @return the list of DeviceUploadForPatient records
    */
  def getDeviceUploadForPatientFromCSV : List[DeviceUploadForPatient] = {
    val list_device_record = new ListBuffer[DeviceUploadForPatient]
    val post = postRecords()
    val device_record_csv = scala.io.Source.fromFile(device_settings_record_path)

    for (
      list_patient <- patientRecords();
      line <- device_record_csv.getLines().drop(1)
    ) {
      val cols = line.split(Constants.Splitter).map(_.trim)
      val device_record = DeviceUploadForPatient(
        PatientId = list_patient.PatientId,
        Model = selectDeviceModel(cols(9), cols(10)),
        DeviceUploadDate = stringToDate(post.PostedTimestamp) match {
          case Right(x) => x
        }, //val uploadDate = lastEgvDateOrDefault(manifests.toSeq, post.postedTimestamp)
        Alerts = getAlertsRecordFromCSV,
        DisplayTimeOffset = cols(8).toInt,
        IngestionTimestamp = stringToDate(post.PostedTimestamp) match {
          case Right(x) => x
        },
        Is24HourMode = cols(4).toBoolean,
        IsBlindedMode = cols(5).toBoolean,
        IsMmolDisplayMode = cols(3).toBoolean,
        Language = cols(2),
        SerialNumber = list_patient.TransmitterNumber, // as per Uma Doddi and Richard's mail
        SoftwareNumber = cols(9),
        SoftwareVersion = cols(10),
        SystemTimeOffset = cols(7).toInt,
        TransmitterId = cols(6),
        Udi = null
      )
      list_device_record += device_record
    }
    device_record_csv.close()

    list_device_record.toList
  }
  /**
    * this method fetches data from Cassandra table DeviceUploadForPatient
    * @return the list of records of device_upload_for_patient
    */
  def getDeviceUploadForPatientFromCassandra : List[DeviceUploadForPatient] = {
    val list_device_upload_record = new ListBuffer[DeviceUploadForPatient]
    val cassandra_connection = new CassandraConnection
    val session = cassandra_connection.getConnection // get cassandra connection

    val resultSet = session.execute(GET_DEVICE_UPLOAD_FOR_PATIENT)
    while(!resultSet.isExhausted) {
      val row = resultSet.one()

      val device_upload_record = DeviceUploadForPatient (
        PatientId = row.getUUID("patient_id"),
        Model = row.getString("model"),
        DeviceUploadDate = row.getTimestamp("device_upload_date"),
        Alerts = getObject(row.getString("system.tojson(alerts)")),
        DisplayTimeOffset = row.getInt("display_time_offset"),
        IngestionTimestamp = row.getTimestamp("ingestion_timestamp"),
        Is24HourMode = row.getBool("is_24_hour_mode"),
        IsBlindedMode = row.getBool("is_blinded_mode"),
        IsMmolDisplayMode = row.getBool("is_mmol_display_mode"),
        Language = row.getString("language"),
        SerialNumber = row.getString("serial_number"),
        SoftwareNumber = row.getString("software_number"),
        SoftwareVersion = row.getString("software_version"),
        SystemTimeOffset = row.getInt("system_time_offset"),
        TransmitterId = row.getString("transmitter_id"),
        Udi = row.getString("udi")
      )
      list_device_upload_record += device_upload_record
    }
    cassandra_connection.closeConnection()  //close cassandra connection
    list_device_upload_record.toList
  }
}
