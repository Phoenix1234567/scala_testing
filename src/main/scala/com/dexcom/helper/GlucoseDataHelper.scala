package com.dexcom.helper

import com.dexcom.common
import com.dexcom.common.{CassandraQueries, Constants}
import com.dexcom.configuration.DexVictoriaConfigurations
import com.dexcom.connection.CassandraConnection
import com.dexcom.dto._
import com.dexcom.utils.Utils
import org.slf4j.LoggerFactory

import scala.collection.mutable.ListBuffer

/**
  * Created by gaurav.garg on 05-01-2017.
  */
class GlucoseDataHelper extends DexVictoriaConfigurations with CassandraQueries {

  val logger = LoggerFactory.getLogger("GlucoseHelper")

  /**
    * this method fetch data from CSV files
    * @return the list of EGVRecords
    */
  def getGlucoseRecordsFromCSV : List[EGVForPatient] = {
    val list_glucose_record = new ListBuffer[EGVForPatient]
    val post = Utils.postRecords()
    val glucose_record_csv = scala.io.Source.fromFile(glucose_record_path)

    for (
      list_patient <- Utils.patientRecords();
      line <- glucose_record_csv.getLines().drop(1)
    ) {
      val cols = line.split(Constants.Splitter).map(_.trim)
      val glucose_record = EGVForPatient(
        PatientId = list_patient.PatientId,
        SystemTime = Utils.stringToDate(cols(0)) match {
          case Right(x) => x
        },
        PostId = post.PostId,
        DisplayTime = Utils.stringToDate(cols(1)) match {
          case Right(x) => x
        },
        IngestionTimestamp = Utils.stringToDate(cols(1)) match {
          case Right(x) => x
        },
        RateUnits = common.EGVForPatient.RateUnits,
        Source = list_patient.SourceStream,
        Status = cols(7),
        TransmitterId = cols(2),
        TransmitterTicks = cols(3).toLong,
        Trend = cols(8),
        TrendRate = cols(9).toDouble,
        Units = common.EGVForPatient.Units,
        Value = cols(6).toInt
      )
      list_glucose_record += glucose_record
    }
    glucose_record_csv.close()

    list_glucose_record.toList
  }

  /**
    * Fetch glucose data from the cassandra table
    * @return
    */
  def getEGVForPatientBySystemTimeRecordsFromCassandra : List[EGVForPatient] = {
    val list_egv_record = new ListBuffer[EGVForPatient]
    val cassandra_connection = new CassandraConnection
    val session = cassandra_connection.getConnection // get cassandra connection

    val resultSet = session.execute(GET_EGV_FOR_PATIENT_BY_SYSTEM_TIME)
    while(!resultSet.isExhausted) {
      val row = resultSet.one()
      val egv_record = EGVForPatient (
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
  }

  /**
    * this method fetches cassandra data from table EGVForPatientByDisplayTime
    * @return the list of records
    */
  def getEGVForPatientByDisplayTimeRecordsFromCassandra : List[EGVForPatient] = {
    val list_egv_record = new ListBuffer[EGVForPatient]
    val cassandra_connection = new CassandraConnection
    val session = cassandra_connection.getConnection // get cassandra connection

    val resultSet = session.execute(GET_EGV_FOR_PATIENT_BY_DISPLAY_TIME)
    while(!resultSet.isExhausted) {
      val row = resultSet.one()
      val egv_record = EGVForPatient (
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
  }

  /**
    * this method returns the index of the list of cassandra data where source record matches
    * @param sourceData of the source EGVForPatient
    * @param destinationDataList of the list of cassandra's EGVForPatientBySystemTime
    * @return the index of the list
    */
  def getIndexForSystemTime(sourceData : EGVForPatient, destinationDataList : List[EGVForPatient]) : Int = {

    val index = destinationDataList.indexWhere {
      y =>
        y.PatientId.equals(sourceData.PatientId) &&
        y.SystemTime.equals(sourceData.SystemTime) &&
        y.PostId.equals(sourceData.PostId)
    }
    index
  }

  /**
    * this method returns the index of the list of cassandra data where source record matches
    * @param sourceData of the source EGVForPatient
    * @param destinationDataList of the list of cassandra's getEGVForPatientByDisplayTimeRecordsFromCassandra
    * @return the index of the list
    */
  def getIndexForDisplayTime(sourceData : EGVForPatient, destinationDataList : List[EGVForPatient]) : Int = {

    val index = destinationDataList.indexWhere {
      y =>
        y.PatientId.equals(sourceData.PatientId) &&
          y.DisplayTime.equals(sourceData.DisplayTime) &&
          y.PostId.equals(sourceData.PostId)
    }
    index
  }
}
