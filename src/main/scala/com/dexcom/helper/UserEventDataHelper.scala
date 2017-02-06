package com.dexcom.helper

import com.datastax.driver.core.Session
import com.dexcom.common.{CassandraQueries, Constants}
import com.dexcom.configuration.DexVictoriaConfigurations
import com.dexcom.dto.UserEventRecord
import com.dexcom.utils.Utils._

import scala.collection.mutable.ListBuffer

/**
  * Created by sarvaraj on 17/01/17.
  */
class UserEventDataHelper(session: Session) extends DexVictoriaConfigurations with CassandraQueries {

  // fetch records from cassandra

  def getRecordFromCassandra: List[UserEventRecord] = {
    val list_user_event_record = new ListBuffer[UserEventRecord]
    val resultSet = session.execute(GET_EVENT_FOR_PATIENT_BY_SYSTEM_TIME)
    while (!resultSet.isExhausted) {
      val row = resultSet.one()
      val user_event_records = new UserEventRecord()
      user_event_records.setPatientID(row.getUUID("patient_id"))
      user_event_records.setDisplayTime(row.getTimestamp("display_time"))
      user_event_records.setName(row.getString("name"))
      user_event_records.setModel(row.getString("model"))
      user_event_records.setIngestionTimestamp(row.getTimestamp("ingestion_timestamp"))
      user_event_records.setPostID(row.getUUID("post_id"))
      user_event_records.setSubType(row.getString("subtype"))
      user_event_records.setSystemTime(row.getTimestamp("system_time"))
      user_event_records.setUnits(row.getString("unit"))
      user_event_records.setValue(row.getString("value"))

      list_user_event_record += user_event_records
    }

    list_user_event_record.toList
  }


  //fetch record from CSV

  def getRecordsFromCSV: List[UserEventRecord] = {
    val list_user_event_records = new ListBuffer[UserEventRecord]
    val post_records = postRecords

    for (list_post <- post_records) {
      val user_event_record_csv = scala.io.Source.fromFile(getpath(list_post.PostId.toString, Constants.UserEvent))

      for (patient <- patientRecords(); line <- user_event_record_csv.getLines().drop(1)) {
        val cols = line.split(Constants.Splitter).map(_.trim)
        val event_record = new UserEventRecord()
        event_record.setPatientID(patient.PatientId)
        event_record.setDisplayTime(stringToDate(cols(3)).get)
        event_record.setName(cols(4))
        event_record.setModel(deviceModel)
        event_record.setIngestionTimestamp(list_post.PostedTimestamp)
        event_record.setPostID(list_post.PostId)
        event_record.setSubType(cols(5))
        event_record.setSystemTime(stringToDate(cols(2)).get)
        event_record.setUnits(cols(7))
        event_record.setValue(cols(6))
        list_user_event_records += event_record
      }
      user_event_record_csv.close()
    }
    list_user_event_records.toList
  }


  /**
    * this method returns the index of the list of cassandra data where source record matches
    *
    * @param sourceData          of the source MeterRecord
    * @param destinationDataList of the list of cassandra's getCalibrationForPatientBySystemTimeRecordsFromCassandra
    * @return the index of the list
    */
  def getIndexForSystemTime(sourceData: UserEventRecord, destinationDataList: List[UserEventRecord]): Int = {

    val index = destinationDataList.indexWhere {
      y =>
        y.PatientID.equals(sourceData.PatientID) &&
          y.SystemTime.equals(sourceData.SystemTime) &&
          y.PostID.equals(sourceData.PostID) &&
          y.Model.equals(sourceData.Model)
    }
    index
  }

  /**
    * this method returns the index of the list of cassandra data where source record matches
    *
    * @param sourceData          of the source MeterRecord
    * @param destinationDataList of the list of cassandra's userEventRecordsFromCassandra
    * @return the index of the list
    */
  def getIndexForDisplayTime(sourceData: UserEventRecord, destinationDataList: List[UserEventRecord]): Int = {

    val index = destinationDataList.indexWhere {
      y =>
        y.PatientID.equals(sourceData.PatientID) &&
          y.DisplayTime.equals(sourceData.DisplayTime) &&
          y.PostID.equals(sourceData.PostID) &&
          y.Model.equals(sourceData.Model)
    }
    index
  }

}
