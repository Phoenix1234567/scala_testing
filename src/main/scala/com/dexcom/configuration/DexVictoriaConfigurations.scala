package com.dexcom.configuration

import com.typesafe.config.ConfigFactory

/**
  * Created by gaurav.garg on 05-01-2017.
  */
trait DexVictoriaConfigurations {

  val conf = ConfigFactory.load("application.conf")

  //read CSV path for Glucose Record
  lazy val glucose_record_path = conf.getString("victoriaSourceCSVs.allCSVsPath") + "/GlucoseRecord.csv"

  //read CSV path for Device_Settings_record
  lazy val device_settings_record_path = conf.getString("victoriaSourceCSVs.allCSVsPath") + "/DeviceSettingsRecord.csv"

  //read CSV path for Alert_Settings_record
  lazy val alert_settings_record_path = conf.getString("victoriaSourceCSVs.allCSVsPath") + "/AlertSettingsRecord.csv"

  //read CSV path for User_event_Record
  lazy val user_event_path = conf.getString("victoriaSourceCSVs.allCSVsPath") + "/UserEventRecord.csv"

  //read CSV path for  Meter_Record
  lazy val meter_record_path = conf.getString("victoriaSourceCSVs.allCSVsPath") + "/MeterRecord.csv"

  //read CSV path for Sensor_Record
  lazy val sensor_record_path = conf.getString("victoriaSourceCSVs.allCSVsPath") + "/SensorRecord.csv"

  //read CSV path for egv_for_patient_by_system_time
  lazy val egv_for_patient_by_system_time = conf.getString("victoriaDestinationCSVs.egv_for_patient_by_system_time")

  //read CSV path of patient.csv
  lazy val patient_records_path = conf.getString("victoriaSourceCSVs.patientPath")

  //read CSV path of postIds.csv
  lazy val post_path = conf.getString("victoriaSourceCSVs.postPath")

  //cassandra hostname
  lazy val hostname = conf.getString("cassandra.host")

  //cassandra port
  lazy val port = conf.getInt("cassandra.port")

  //read userName to access cassandra
  lazy val userName = conf.getString("cassandra.userName")

  //read password to access cassandra
  lazy val password = conf.getString("cassandra.password")

  //read truestore path
  lazy val trueStorePath = conf.getString("cassandra.trueStorePath")

  //read trueStore password
  lazy val trueStorePassword = conf.getString("cassandra.trueStorePassword")

  //read keySpaceName
  lazy val keySpaceName = conf.getString("cassandra.keySpaceName")
}
