package com.dexcom.helper


import com.dexcom.common.{CassandraQueries, Constants}
import com.dexcom.configuration.DexVictoriaConfigurations
import com.dexcom.connection.CassandraConnection
import com.dexcom.{common, dto}
import com.dexcom.dto._
import com.dexcom.utils.Utils

import scala.collection.mutable.ListBuffer

/**
  * Created by aditi.nandwana on 20-01-2017.
  */
class CalibrationDataHelper extends DexVictoriaConfigurations with CassandraQueries {

  // fetch records from CSV MeterRecord

  def getMeterRecordsFromCSV: List[MeterRecord] = {
    val list_meter_record = new ListBuffer[MeterRecord]
    val post = Utils.postRecords()
    val meter_record_csv = scala.io.Source.fromFile(meter_record_path)

    for (
      list_patient <- Utils.patientRecords();
      line <- meter_record_csv.getLines().drop(1)
    ) {
      val cols = line.split(Constants.Splitter).map(_.trim)
      val meter_record = dto.MeterRecord(
        PatientId = list_patient.PatientId,
        //PostId = post.PostId,
        SystemTime = Utils.stringToDate(cols(0)) match {
          case Right(x) => x
        },
        DisplayTime = Utils.stringToDate(cols(1)) match {
          case Right(x) => x
        },
        TransmitterId = cols(2),
        IngestionTimestamp = Utils.stringToDate(cols(0)) match {
          case Right(x) => x
        },

        Units = common.MeterRecord.Units,
        Value = cols(6).toInt,
        EntryType = cols(7),
        Model = common.MeterRecord.Model
      )
      list_meter_record += meter_record
    }
    meter_record_csv.close()

    list_meter_record.toList
  }
/*
* Fetch records from cassandra for CalibrationForPatientBySystemTime
* */
  def getCalibrationForPatientBySystemTimeRecordsFromCassandra : List[MeterRecord] = {
    val list_calibration_record = new ListBuffer[MeterRecord]
    val cassandra_connection = new CassandraConnection
    val session = cassandra_connection.getConnection // get cassandra connection

    val resultSet = session.execute(GET_CALIBRATION_FOR_PATIENT_BY_SYSTEM_TIME)
    while(!resultSet.isExhausted) {
      val row = resultSet.one()
      val calibration_record = dto.MeterRecord (
        PatientId = row.getUUID("patient_id"),

        SystemTime = row.getTimestamp("system_time"),
        //PostId = row.getUUID("post_id"),
        DisplayTime = row.getTimestamp("display_time"),
        TransmitterId = row.getString("transmitter_id"),
        IngestionTimestamp = row.getTimestamp("ingestion_timestamp"),
        Units = row.getString("units"),
        Value = row.getInt("value"),
        EntryType = row.getString("entry_type"),
        Model = row.getString("model")
      )
      list_calibration_record += calibration_record
    }
    cassandra_connection.closeConnection()  //close cassandra connection
    list_calibration_record.toList
  }

  def getIndexForSystemTime(sourceData : MeterRecord, destinationDataList : List[MeterRecord]) : Int = {

    val index = destinationDataList.indexWhere {
      y =>
          y.PatientId.equals(sourceData.PatientId) &&
          y.SystemTime.equals(sourceData.SystemTime) &&
         // y.PostId.equals(sourceData.PostId)
          y.EntryType.equals(sourceData.EntryType)&&
          y.Model.equals(sourceData.Model)
    }
    index
  }
  /**
    * this method fetches cassandra data from table CalibrationForPatientByDisplayTime
    * @return the list of records
    */
  /*def getEGVForPatientByDisplayTimeRecordsFromCassandra() : List[MeterRecord] = {
    val list_calibration_record = new ListBuffer[MeterRecord]
    val cassandra_connection = new CassandraConnection
    val session = cassandra_connection.getConnection // get cassandra connection

    val resultSet = session.execute(GET_CALIBRATION_FOR_PATIENT_BY_DISPLAY_TIME)
    while(!resultSet.isExhausted) {
      val row = resultSet.one()
      val egv_record = dto.EGVForPatient (
        PatientId = row.getUUID("patient_id"),
        SystemTime = row.getTimestamp("system_time"),
        PostId = row.getUUID("post_id"),
        DisplayTime = row.getTimestamp("display_time"),
        IngestionTimestamp = row.getTimestamp("ingestion_timestamp"),
        RateUnits = row.getString("rate_units"),
        Source = row.getString("source"),
        Status = row.getString("status"),
        TransmitterId = row.getString("transmitter_id"),
        TransmitterTicks = row.getLong("transmitter_ticks"),
        Trend = row.getString("trend"),
        TrendRate = row.getDouble("trend_rate"),
        Units = row.getString("units"),
        Value = row.getInt("value")
      )
      list_egv_record += egv_record
    }
    cassandra_connection.closeConnection()  //close cassandra connection
    list_egv_record.toList
  }*/








}












  //fetch records from Cassandra

/*  def getRecordsfromCassandra(): List[MeterRecord] = {

    val list_calibration_record = new ListBuffer[MeterRecord]
    val cassandra_connection = new CassandraConnection
    val session = cassandra_connection.getConnection // get cassandra connection

    val resultSet = session.execute(GET_CALIBRATION_FOR_PATIENT_BY_SYSTEM_TIME)
    while (!resultSet.isExhausted) {
      val row = resultSet.one()
      val calibration_record = dto.MeterRecord(
      /*  ,

        SystemTime = row.getTimestamp("system_time"),
        PostId = row.getUUID("post_id"),
        DisplayTime = row.getTimestamp("display_time"),
        IngestionTimestamp = row.getTimestamp("ingestion_timestamp"),
        RateUnits = row.getString("rate_units"),
        Source = row.getString("source"),
        Status = row.getString("status"),
        TransmitterId = row.getString("transmitter_id"),
        TransmitterTicks = row.getLong("transmitter_ticks"),
        Trend = row.getString("trend"),
        TrendRate = row.getDouble("trend_rate"),
        Units = row.getString("units"),
        Value = row.getInt("value")*/
        PatientId = row.getUUID("patient_id")

      )
      list_calibration_record += calibration_record
    }
    cassandra_connection.closeConnection() //close cassandra connection
    list_calibration_record.toList
  }*/

