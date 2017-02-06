package com.dexcom.configuration

import com.typesafe.config.ConfigFactory

/**
  * Created by gaurav.garg on 05-01-2017.
  */
trait DexVictoriaConfigurations {

  val conf = ConfigFactory.load("application.conf")

  //read path
  lazy val common_path = conf.getString("victoriaSourceCSVs.allCSVsPath")

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
