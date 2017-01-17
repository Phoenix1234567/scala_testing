package com.dexcom.helper

import java.util.{Date, UUID}

import com.dexcom.{common, dto}
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
    * Fetch glucose data from the Glucose.csv
 *
    * @return list of Glucose object
    */
  def GlucoseRecords() : List[GlucoseRecord] = {
    val list_glucose_record = new ListBuffer[GlucoseRecord]

    val glucose_record_csv = scala.io.Source.fromFile(glucose_record_path)
    for (line <- glucose_record_csv.getLines().drop(1)) {
      val cols = line.split(Constants.Splitter).map(_.trim)
      val glucose_record = GlucoseRecord(
        RecordedSystemTime =
          Utils.stringToDate(cols(0)) match {
            case Right(x) => x
            case Left(e) => new Date(11111111)
          },
        RecordedDisplayTime = Utils.stringToDate(cols(1)) match {
          case Right(x) => x
          case Left(e) => new Date(11111111)
        },
        TransmitterId = cols(2),
        TransmitterTime = cols(3).toLong,
        GlucoseSystemTime = Utils.stringToDate(cols(4)) match {
          case Right(x) => x
          case Left(e) => new Date(11111111)
        },
        GlucoseDisplayTime = Utils.stringToDate(cols(5)) match {
          case Right(x) => x
          case Left(e) => new Date(11111111)
        },
        Value = cols(6).toInt,
        Status = cols(7),
        TrendArrow = cols(8),
        TrendRate = cols(9).toDouble,
        IsBackfilled = cols(10).toBoolean,
        InternalStatus = cols(11)
      )
      list_glucose_record += glucose_record
    }
    glucose_record_csv.close()

    list_glucose_record.toList
  }



  def test() : List[GlucoseRecord] = {
    val list_glucose_record = new ListBuffer[GlucoseRecord]

    val glucose_record_csv = scala.io.Source.fromFile(glucose_record_path)
    for (line <- glucose_record_csv.getLines().drop(1)) {
      val cols = line.split(Constants.Splitter).map(_.trim)
      val glucose_record = GlucoseRecord(
        RecordedSystemTime =
          Utils.stringToDate(cols(0)) match {
            case Right(x) => x
          },
        RecordedDisplayTime = Utils.stringToDate(cols(1)) match {
          case Right(x) => x
        },
        TransmitterId = cols(2),
        TransmitterTime = cols(3).toLong,
        GlucoseSystemTime = Utils.stringToDate(cols(4)) match {
          case Right(x) => x
        },
        GlucoseDisplayTime = Utils.stringToDate(cols(5)) match {
          case Right(x) => x
        },
        Value = cols(6).toInt,
        Status = cols(7),
        TrendArrow = cols(8),
        TrendRate = cols(9).toDouble,
        IsBackfilled = cols(10).toBoolean,
        InternalStatus = cols(11)
      )
      list_glucose_record += glucose_record
    }
    glucose_record_csv.close()

    list_glucose_record.toList
  }





  /**
    * Combining the record of three CSVs Patient.csv, PostIds.csv, Glucose.csv
 *
    * @return the list of source glucose record data
    */
  def EGVForPatient() : List[EGVForPatient]= {
    val list_glucose_record = this.GlucoseRecords()
    val list_patient = Utils.patientRecords()
    val post = Utils.postRecords()
    val list_egv_for_patient_source_data  = new ListBuffer[EGVForPatient]
    for(
      i <- list_patient.indices;
      j <- list_glucose_record.indices
    ) {
      val egv_for_patient_source_data = dto.EGVForPatient (
        PatientId = list_patient(i).PatientId,
        SystemTime = list_glucose_record(j).RecordedSystemTime,
        PostId = post.PostId,
        DisplayTime = list_glucose_record(j).RecordedDisplayTime,
        IngestionTimestamp = list_glucose_record(j).RecordedDisplayTime,
        RateUnits = common.EGVForPatient.RateUnits,
        Source = list_patient(i).SourceStream,
        Status = list_glucose_record(j).Status,
        TransmitterId = list_glucose_record(j).TransmitterId,
        TransmitterTicks = list_glucose_record(j).TransmitterTime,
        Trend = list_glucose_record(j).TrendArrow,
        TrendRate = list_glucose_record(j).TrendRate,
        Units = common.EGVForPatient.Units,
        Value = list_glucose_record(j).Value
      )
      list_egv_for_patient_source_data += egv_for_patient_source_data
    }

    list_egv_for_patient_source_data.toList
  }

  /**
    * Fetch glucose data from the cassandra table
    *
    * @return
    */
  def EGVForPatientBySystemTime() : List[EGVForPatient] = {
    val list_egv_record = new ListBuffer[EGVForPatient]
    val cassandra_connection = new CassandraConnection
    val session = cassandra_connection.getConnection // get cassandra connection

    val resultSet = session.execute(GET_EGV_FOR_PATIENT_BY_SYSTEM_TIME)
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
  }

  /**
    * this method fetches cassandra data from table EGVForPatientByDisplayTime
    * @return the list of records
    */
  def EGVForPatientByDisplayTime() : List[EGVForPatient] = {
    val list_egv_record = new ListBuffer[EGVForPatient]
    val cassandra_connection = new CassandraConnection
    val session = cassandra_connection.getConnection // get cassandra connection

    val resultSet = session.execute(GET_EGV_FOR_PATIENT_BY_DISPLAY_TIME)
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
  }

  /**
    * this method returns the index of the list of cassandra data where source record matches
    * @param sourceData of the source EGVForPatient
    * @param destinationDataList of the list of cassandra's EGVForPatient
    * @return the index of the list
    */
  def getIndex(sourceData : EGVForPatient, destinationDataList : List[EGVForPatient]) : Int = {

    val index = destinationDataList.indexWhere {
      y =>
        y.PatientId.equals(sourceData.PatientId) &&
        y.SystemTime.equals(sourceData.SystemTime) &&
        y.PostId.equals(sourceData.PostId)
    }
    index
  }
}
