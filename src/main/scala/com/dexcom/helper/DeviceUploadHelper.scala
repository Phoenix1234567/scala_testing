package com.dexcom.helper

import com.datastax.driver.core.UDTValue
import com.dexcom.common.{CassandraQueries, Constants}
import com.dexcom.connection.CassandraConnection
import com.dexcom.dto.{AlertSetting, DeviceUploadForPatient}
import com.dexcom.utils.Utils._

import scala.collection.mutable.ListBuffer

/**
  * Created by gaurav.garg on 23-01-2017.
  */
class DeviceUploadHelper extends CassandraQueries {

  /**
    * thiss method get the alertsettingsrecord from CSV
    *
    * @return the list of AlertSetting records
    */
  def getAlertsRecordFromCSV: List[AlertSetting] = {

    val list_alerts = new ListBuffer[AlertSetting]
    val alerts_record_csv = scala.io.Source.fromFile(alert_settings_record_path)

    for (line <- alerts_record_csv.getLines().drop(1)) {
      val cols = line.split(Constants.Splitter).map(_.trim)
      val alert_record = AlertSetting(
        name = cols(2),
        value = cols(3).toInt,
        system_time = stringToDate(cols(0)).get,
        display_time = stringToDate(cols(1)).get,
        units = units(cols(2)),
        delay = cols(4).toInt,
        snooze = cols(5).toInt,
        enabled = cols(7).toBoolean
      )

      list_alerts += alert_record
    }
    list_alerts.toList
  }

  /**
    * this method returns the unit of alert setting
    * get that code from DEV in modules AlertSetting.scalas
    *
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
    * this method fetches data from CSV
    *
    * @return the list of DeviceUploadForPatient records
    */
  def getDeviceUploadForPatientFromCSV: Option[List[DeviceUploadForPatient]] = {
    val list_device_record = new ListBuffer[DeviceUploadForPatient]
    val post_records = postRecords
    val device_record_csv = scala.io.Source.fromFile(device_settings_record_path)

    try {
      for (
        list_post <- post_records;
        list_patient <- patientRecords();
        line <- device_record_csv.getLines().drop(1)
      ) {
        val cols = line.split(Constants.Splitter).map(_.trim)
        val device_record = DeviceUploadForPatient(
          PatientId = list_patient.PatientId,
          Model = selectDeviceModel(cols(9), cols(10)),
          DeviceUploadDate = uploadDate(list_post.PostId), //val uploadDate = lastEgvDateOrDefault(manifests.toSeq, post_records.postedTimestamp)
          Alerts = getAlertsRecordFromCSV,
          DisplayTimeOffset = cols(8).toInt, //TODO
          IngestionTimestamp = list_post.PostedTimestamp,
          Is24HourMode = cols(4).toBoolean, //TODO
          IsBlindedMode = cols(5).toBoolean, //TODO
          IsMmolDisplayMode = cols(3).toBoolean, //TODO
          Language = cols(2), //TODO
          SerialNumber = serialNumber,
          SoftwareNumber = cols(9), //TODO
          SoftwareVersion = cols(10), //TODO
          SystemTimeOffset = cols(7).toInt, //TODO
          TransmitterId = cols(6),
          Udi = None,
          RecordedSystemTime = stringToDate(cols(0))
        )
        list_device_record += device_record
      }
      device_record_csv.close()

      Some(list_device_record.toList)
    } catch {
      case e: Exception => None
    }
  }

  /**
    * this method fetches data from Cassandra table DeviceUploadForPatient
    *
    * @return the list of records of device_upload_for_patient
    */
  def getDeviceUploadForPatientFromCassandra: List[DeviceUploadForPatient] = {
    val list_device_upload_record = new ListBuffer[DeviceUploadForPatient]
    var list_alerts: List[AlertSetting] = Nil
    val cassandra_connection = new CassandraConnection
    val session = cassandra_connection.getConnection // get cassandra connection

    val resultSet = session.execute(GET_DEVICE_UPLOAD_FOR_PATIENT)
    while (!resultSet.isExhausted) {
      val row = resultSet.one()
      val alerts = row.getList[UDTValue]("alerts", classOf[UDTValue])
      val iterator = alerts.iterator()
      while (iterator.hasNext) {
        val row_alerts = iterator.next()
        val alerts = AlertSetting(
          name = row_alerts.getString("name"),
          value = row_alerts.getInt("value"),
          system_time = row_alerts.getTimestamp("system_time"),
          display_time = row_alerts.getTimestamp("display_time"),
          units = row_alerts.getString("units"),
          delay = row_alerts.getInt("delay"),
          snooze = row_alerts.getInt("snooze"),
          enabled = row_alerts.getBool("enabled")
        )
        list_alerts = alerts :: list_alerts
      }

      val device_upload_record = DeviceUploadForPatient(
        PatientId = row.getUUID("patient_id"),
        Model = row.getString("model"),
        DeviceUploadDate = row.getTimestamp("device_upload_date"),
        Alerts = list_alerts, //getObject(row.getString("system.tojson(alerts)")),
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
        Udi = Some(row.getString("udi")),
        RecordedSystemTime = None //hardcoded to handle deviceModel value
      )
      list_device_upload_record += device_upload_record
    }
    cassandra_connection.closeConnection() //close cassandra connection
    list_device_upload_record.toList
  }
  def getIndexForDeviceUpload(sourceData : DeviceUploadForPatient, destinationDataList : List[DeviceUploadForPatient]) : Int = {
    val index = destinationDataList.indexWhere {
      y =>
        y.PatientId.equals(sourceData.PatientId) &&
          y.Model.equals(sourceData.Model) &&
          y.DeviceUploadDate.equals(sourceData.DeviceUploadDate) //check mapping of deviceUploadDate
    }
    index
  }
}
