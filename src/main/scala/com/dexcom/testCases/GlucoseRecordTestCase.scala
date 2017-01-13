package com.dexcom.testCases

import java.text.{ParseException, SimpleDateFormat}
import java.util.{Date, UUID}

import com.dexcom.common
import com.dexcom.common.{AppCommon, CassandraQueries}
import com.dexcom.configuration.DexVictoriaConfigurations
import com.dexcom.connection.CassandraConnection
import com.dexcom.dto.{EGVForPatientBySystemTime, GlucoseRecord, Patient, Post}
import org.slf4j.LoggerFactory

import scala.collection.mutable.ListBuffer

/**
  * Created by gaurav.garg on 05-01-2017.
  */
class GlucoseRecordTestCase extends /*App with  */DexVictoriaConfigurations with CassandraQueries {

  val logger = LoggerFactory.getLogger("GlucoseRecordTestCase")

  def stringToDate(dateString : String) : Either[Unit, Date]= {
    val df = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSSSSSS'Z'")  //2014-05-16T20:06:17.1592279Z
    val dfNew = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy")
    try {
      val date = df.parse(dateString)
      val dateNew = dfNew.parse(dfNew.format(date))
      Right(dateNew)
    } catch {
      case e : ParseException =>
        Left(e.printStackTrace())
    }
  }
  /**
    * Fetch patientid, source, etc from Patient.csv file
 *
    * @return list of the object patient objects
    */
  def patientRecords() : List[Patient] = {
  val list_patient_record = new ListBuffer[Patient]
  val patient_record_csv = scala.io.Source.fromFile(patient_records_path)
  for(line <- patient_record_csv.getLines().drop(1)) {
    val cols = line.split(AppCommon.Splitter).map(_.trim)
    val patient_record = Patient (
      PatientId = UUID.fromString(cols(0)),
      SourceStream = cols(1),
      SequenceNumber = cols(2),
      TransmitterNumber = cols(3),
      ReceiverNumber = cols(4),
      Tag = cols(5)
    )
    list_patient_record += patient_record
  }
  patient_record_csv.close()
  list_patient_record.toList

}

  /**
    * Fetch postid, postedTimestamp from postIds.csv
 *
    * @return list of post object
    */
  def postRecords() : Post = {
    var post_record : Post= null
    val post_record_csv = scala.io.Source.fromFile(post_path)
    for(line <- post_record_csv.getLines().drop(1)) {
      val cols = line.split(AppCommon.Splitter).map(_.trim)
      post_record = Post (
        PostId = UUID.fromString(cols(0)),
        PostedTimestamp = cols(1)
      )

    }
    post_record_csv.close()
    post_record
  }

  /**
    * Fetch glucose data from the Glucose.csv
 *
    * @return list of Glucose object
    */
  def GlucoseRecords() : List[GlucoseRecord] = {
    val list_glucose_record = new ListBuffer[GlucoseRecord]

    val glucose_record_csv = scala.io.Source.fromFile(glucose_record_path)
    for (line <- glucose_record_csv.getLines().drop(1)) {
      val cols = line.split(AppCommon.Splitter).map(_.trim)
      val glucose_record = GlucoseRecord(
        RecordedSystemTime =
          stringToDate(cols(0)) match {
            case Right(x) => x
          },
        RecordedDisplayTime = stringToDate(cols(1)) match {
          case Right(x) => x
        },
        TransmitterId = cols(2),
        TransmitterTime = cols(3).toLong,
        GlucoseSystemTime = stringToDate(cols(4)) match {
          case Right(x) => x
        },
        GlucoseDisplayTime = stringToDate(cols(5)) match {
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
    val list_patient = this.patientRecords()
    val post = this.postRecords()
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
