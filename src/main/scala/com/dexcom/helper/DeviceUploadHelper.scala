package com.dexcom.helper

import com.dexcom.common.CassandraQueries
import com.dexcom.connection.CassandraConnection
import com.dexcom.dto.{AlertSetting, DeviceUploadForPatient}
import com.google.common.reflect.TypeToken

import scala.collection.mutable.ListBuffer

/**
  * Created by gaurav.garg on 23-01-2017.
  */
object DeviceUploadHelper extends CassandraQueries with App{


  def getDeviceUploadForPatientFromCassandra : List[DeviceUploadForPatient] = {
    val list_device_upload_record = new ListBuffer[DeviceUploadForPatient]
    val list_alert_setting = new ListBuffer[AlertSetting]
    val cassandra_connection = new CassandraConnection
    val session = cassandra_connection.getConnection // get cassandra connection

    val resultSet = session.execute(GET_DEVICE_UPLOAD_FOR_PATIENT)
    while(!resultSet.isExhausted) {
      val row = resultSet.one()
      //val c = row.getList("alerts", AlertSetting.apply())
     // println(row.getUDTValue("alerts"))

     // val listOfAlerts = new TypeToken[List[AlertSetting]]() {}
     // val test = row.getString(0)

      list_alert_setting += AlertSetting(
        Name = row.getString("alerts.name"),
        Value = row.getInt("alerts.alue"),
        SystemTime = row.getTimestamp("alerts.system_time"),
        DisplayTime = row.getTimestamp("alerts.display_time"),
        Units = row.getString("alerts.units"),
        Delay = row.getInt("alerts.delay"),
        Snooze = row.getInt("alerts.snooze"),
        Enabled = row.getBool("alerts.enabled")
      )
      val device_upload_record = DeviceUploadForPatient (
        PatientId = row.getUUID("patient_id"),
        Model = row.getString("model"),
        DeviceUploadDate = row.getTimestamp("device_upload_date"),
        Alerts = list_alert_setting.toList,
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

  println(this.getDeviceUploadForPatientFromCassandra)
}
