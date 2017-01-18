package com.dexcom.helper

import java.util.Date

import com.datastax.driver.core.Session
import com.dexcom.common.{CassandraQueries, Constants}
import com.dexcom.configuration.DexVictoriaConfigurations
import com.dexcom.connection.CassandraConnection
import com.dexcom.dto.{EGVForPatient, GlucoseRecord, UserEventRecord}
import com.dexcom.utils.Utils

import scala.collection.mutable.ListBuffer

/**
  * Created by sarvaraj on 17/01/17.
  */
class UserEventDataHelper(session: Session) extends DexVictoriaConfigurations with CassandraQueries{

  // fetch records from cassandra

  def getRecordFromCassandra(): List[UserEventRecord] = {
    val list_user_event_record = new ListBuffer[UserEventRecord]
    val resultSet = session.execute(GET_EVENT_FOR_PATIENT_BY_SYSTEM_TIME)
    while(!resultSet.isExhausted) {
      val row = resultSet.one()
     val user_event_records = new UserEventRecord()
      user_event_records.setPatientID(row.getUUID("patient_id"))
      user_event_records.setDisplayTime(row.getTimestamp("display_time"))
      user_event_records.setName(row.getString("name"))
      user_event_records.setModel(row.getString("model"))
      user_event_records.setIngestionTimestamp(row.getTimestamp("ingestion_timestamp"))
      user_event_records.setPostID(row.getUUID("post_id"))
      user_event_records.setSubType(row.getString("subtype"))
      user_event_records.setSystemTimes(row.getTimestamp("system_time"))
      user_event_records.setUnits(row.getString("unit"))
      user_event_records.setValue(row.getString("value"))

      list_user_event_record += user_event_records
    }

    list_user_event_record.toList
  }


  //fetch record from CSV

  def getRecordsFromCSV() : List[UserEventRecord] = {
    val list_user_event_records = new ListBuffer[UserEventRecord]

    val user_event_record_csv = scala.io.Source.fromFile(user_event_path)
    val post = Utils.postRecords()

    for (patient<-Utils.patientRecords();line <- user_event_record_csv.getLines().drop(1)) {
      val cols = line.split(Constants.Splitter).map(_.trim)
      val event_record = new UserEventRecord()
        event_record.setPatientID(patient.PatientId)
        event_record.setDisplayTime( Utils.stringToDate(cols(3)) match {
          case Right(x) => x
          case Left(e) => new Date(11111111)
        })
      event_record.setName(cols(4))
      event_record.setModel("G5")
      event_record.setIngestionTimestamp(Utils.stringToDate(cols(0)) match {
        case Right(x) => x
        case Left(e) => new Date(11111111)
      })

      event_record.setPostID(post.PostId)
      event_record.setSubType(cols(5))
      event_record.setSystemTimes(Utils.stringToDate(cols(2)) match {
        case Right(x) => x
        case Left(e) => new Date(11111111)
      })
      event_record.setUnits(cols(7))
      event_record.setValue(cols(6))
      list_user_event_records += event_record
    }
    user_event_record_csv.close()
    println(list_user_event_records.toList.toString())
    list_user_event_records.toList
  }

}