package com.dexcom.configuration

import java.io.File

import com.dexcom.common.Constants
import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by gaurav.garg on 05-01-2017.
  */
trait DexVictoriaConfigurations {

  var conf: Config = _

  def initConf: Config = {
    conf = ConfigFactory.parseFileAnySyntax(new File(Constants.ConfigPath))
    if (conf.isEmpty || conf == null)
      conf = ConfigFactory.load("application.conf")
    conf
  }

  lazy val config = initConf
  //read path
  lazy val common_path = config.getString("victoriaSourceCSVs.allCSVsPath")

  //read CSV path of patient.csv
  lazy val patient_records_path = config.getString("victoriaSourceCSVs.patientPath")

  //read CSV path of postIds.csv
  lazy val post_path = config.getString("victoriaSourceCSVs.postPath")

  //cassandra hostname
  lazy val hostname = config.getString("cassandra.host")

  //cassandra port
  lazy val port = config.getInt("cassandra.port")

  //read userName to access cassandra
  lazy val userName = config.getString("cassandra.userName")

  //read password to access cassandra
  lazy val password = config.getString("cassandra.password")

  //read truestore path
  lazy val trueStorePath = config.getString("cassandra.trueStorePath")

  //read trueStore password
  lazy val trueStorePassword = config.getString("cassandra.trueStorePassword")

  //read keySpaceName
  lazy val keySpaceName = config.getString("cassandra.keySpaceName")
}
