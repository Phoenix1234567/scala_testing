package com.dexcom.helper

import java.util.UUID

import com.dexcom.common
import com.dexcom.common.{Constants, CassandraQueries}
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
  def EGVRecordsSource() : List[EGVForPatientBySystemTime]= {
    val list_glucose_record = this.GlucoseRecords()
    val list_patient = Utils.patientRecords()
    val post = Utils.postRecords()
    val list_egv_for_patient_source_data  = new ListBuffer[EGVForPatientBySystemTime]
    for(
      i <- list_patient.indices;
      j <- list_glucose_record.indices
    ) {
      val egv_for_patient_source_data = EGVForPatientBySystemTime (
        PatientId = list_patient(i).PatientId,
        SystemTime = list_glucose_record(j).RecordedSystemTime,
        PostId = post.PostId,
        DisplayTime = list_glucose_record(j).RecordedDisplayTime,
        IngestionTimestamp = list_glucose_record(j).RecordedDisplayTime,
        RateUnits = common.EGVForPatientBySystemTime.RateUnits,
        Source = list_patient(i).SourceStream,
        Status = list_glucose_record(j).Status,
        TransmitterId = list_glucose_record(j).TransmitterId,
        TransmitterTicks = list_glucose_record(j).TransmitterTime,
        Trend = list_glucose_record(j).TrendArrow,
        TrendRate = list_glucose_record(j).TrendRate,
        Units = common.EGVForPatientBySystemTime.Units,
        Value = list_glucose_record(j).Value
      )
      list_egv_for_patient_source_data += egv_for_patient_source_data
    }

    list_egv_for_patient_source_data.toList
  }

  /**
    * Fetch glucose data from destination csv created from the cassandra table
    *
    * @return
    */
  def EGVRecordsDestination(): List[EGVForPatientBySystemTime] = {
    val list_egv_record = new ListBuffer[EGVForPatientBySystemTime]
    val cassandra_connection = new CassandraConnection
    val session = cassandra_connection.getConnection // get cassandra connection

    val resultSet = session.execute(GET_EGV_FOR_PATIENT_BY_SYSTEM_TIME)
    while(!resultSet.isExhausted) {
      val row = resultSet.one()
      val egv_record = EGVForPatientBySystemTime (
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

  /*
  *  To fetch data from Cassandra - EventForPatientBySystemTime
  * */

 /* def EventRecordsDestination(): List[EventForPatientBySystemTime] = {
    val list_event_record = new ListBuffer[EventForPatientBySystemTime]
    val cassandra_connection = new CassandraConnection
    val session = cassandra_connection.getConnection // get cassandra connection

    val resultSet = session.execute(GET_EVENT_FOR_PATIENT_BY_SYSTEM_TIME)
    while(!resultSet.isExhausted) {
      val row = resultSet.one()
      println("*********"+row.getTimestamp("system_time"))
      val event_record = EventForPatientBySystemTime (
        PatientId = row.getUUID("patient_id"),
        SystemTime = row.getTimestamp("system_time"),
        name = row.getString("name"),
        model = row.getString("model"),
        DisplayTime = row.getTimestamp("display_time"),
        IngestionTimestamp = row.getTimestamp("ingestion_timestamp"),
        PostId = row.getUUID("post_id"),
        subtype = row.getString("subtype"),
        unit = row.getString("unit"),
        Value = row.getString("value")
      )
      list_event_record += event_record
    }

    cassandra_connection.closeConnection()  //close cassandra connection
    list_event_record.toList
  }*/


  /*val list_egv_for_patient_source_data = this.EGVRecordsSource()
  val list_egv_for_patient_destination_data = this.EGVRecordsDestination()

  logger.info("---------------Count comparison of source and destination---------------")
  logger.info(s"Count of Glucose Record at source is ${list_egv_for_patient_source_data.length}")
  logger.info(s"Count of EGV records By System time at destination is ${list_egv_for_patient_destination_data.length}")
  if(list_egv_for_patient_destination_data.length == list_egv_for_patient_source_data.length) {
    logger.info("Count at source matches with count at destination")
  } else {
    logger.error("Count at source does not matches with that at destination")
  }

  logger.info("\n\n-------------------Data at source reach at destination-------------------")
  //verify source glucose data whether is present at destination
  println(list_egv_for_patient_destination_data)
  var count : Int = 0
  for( i <- list_egv_for_patient_source_data.indices) {
    if (list_egv_for_patient_destination_data.contains(list_egv_for_patient_source_data(i))) {
      for( j <- list_egv_for_patient_destination_data.indices) {
        if(list_egv_for_patient_destination_data(j) == list_egv_for_patient_source_data(i)) {
          count += 1
        }
      }
      if(count == 1) {
        logger.info(s"Record at source matches with cassandra : ${list_egv_for_patient_source_data(i)}")
      } else {
        logger.error(s"$count records found : ${list_egv_for_patient_source_data(i)}")
      }
    } else {
      logger.error(s"Record not found in cassandra : ${list_egv_for_patient_source_data(i)}")
    }
  }*/
}
