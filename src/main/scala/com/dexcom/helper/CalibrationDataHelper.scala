package com.dexcom.helper


import com.dexcom.common.{CassandraQueries, Constants}
import com.dexcom.configuration.DexVictoriaConfigurations
import com.dexcom.connection.CassandraConnection
import com.dexcom.dto._
import com.dexcom.utils.Utils._
import com.dexcom.{common, dto}
import org.slf4j.LoggerFactory

import scala.collection.mutable.ListBuffer

/**
  * Created by aditi.nandwana on 20-01-2017.
  */
class CalibrationDataHelper extends DexVictoriaConfigurations with CassandraQueries {

  // fetch records from CSV MeterRecord and merge with patient_id from patient.csv

  val logger = LoggerFactory.getLogger("CalibrationHelper")

  def getMeterRecordsFromCSV: List[MeterRecord] = {
    val list_meter_record = new ListBuffer[MeterRecord]
    val post_records = postRecords()
    val meter_record_csv = scala.io.Source.fromFile(meter_record_path)

    for (
      list_post <- post_records;
      list_patient <- patientRecords();
      line <- meter_record_csv.getLines().drop(1)
    ) {
      val cols = line.split(Constants.Splitter).map(_.trim)
      val meter_record = dto.MeterRecord(
        PatientId = list_patient.PatientId,
        SystemTime = stringToDate(cols(4)).get,
        DisplayTime = stringToDate(cols(5)).get,
        TransmitterId = cols(2),
        IngestionTimestamp = stringToDate(list_post.PostedTimestamp).get,
        Units = common.MeterRecord.Units,
        Value = cols(6).toInt,
        EntryType = cols(7),
        Model = deviceModel
      )
      list_meter_record += meter_record
    }
    meter_record_csv.close()

    list_meter_record.toList
  }

  /*
  * Fetch records from cassandra for CalibrationForPatientBySystemTime
  * @return list of records
  * */
  def getCalibrationForPatientBySystemTimeRecordsFromCassandra: List[MeterRecord] = {
    val list_calibration_record = new ListBuffer[MeterRecord]
    val cassandra_connection = new CassandraConnection
    val session = cassandra_connection.getConnection // get cassandra connection

    val resultSet = session.execute(GET_CALIBRATION_FOR_PATIENT_BY_SYSTEM_TIME)
    while (!resultSet.isExhausted) {
      val row = resultSet.one()
      val calibration_record = dto.MeterRecord(
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
    cassandra_connection.closeConnection() //close cassandra connection
    list_calibration_record.toList
  }


  /**
    * this method fetches cassandra data from table CalibrationForPatientByDisplayTime
    *
    * @return the list of records
    */
  def getCalibrationForPatientByDisplayTimeRecordsFromCassandra: List[MeterRecord] = {
    val list_calibration_record = new ListBuffer[MeterRecord]
    val cassandra_connection = new CassandraConnection
    val session = cassandra_connection.getConnection // get cassandra connection

    val resultSet = session.execute(GET_CALIBRATION_FOR_PATIENT_BY_DISPLAY_TIME)
    while (!resultSet.isExhausted) {
      val row = resultSet.one()
      val calibration_record = dto.MeterRecord(
        PatientId = row.getUUID("patient_id"),
        SystemTime = row.getTimestamp("system_time"),
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
    cassandra_connection.closeConnection() //close cassandra connection
    list_calibration_record.toList
  }

  /**
    * this method returns the index of the list of cassandra data where source record matches
    *
    * @param sourceData          of the source MeterRecord
    * @param destinationDataList of the list of cassandra's getCalibrationForPatientBySystemTimeRecordsFromCassandra
    * @return the index of the list
    */
  def getIndexForSystemTime(sourceData: MeterRecord, destinationDataList: List[MeterRecord]): Int = {

    val index = destinationDataList.indexWhere {
      y =>
        y.PatientId.equals(sourceData.PatientId) &&
          y.SystemTime.equals(sourceData.SystemTime) &&
          y.EntryType.equals(sourceData.EntryType) &&
          y.Model.equals(sourceData.Model)
    }
    index
  }

  /**
    * this method returns the index of the list of cassandra data where source record matches
    *
    * @param sourceData          of the source MeterRecord
    * @param destinationDataList of the list of cassandra's getCalibrationForPatientByDisplayTimeRecordsFromCassandra
    * @return the index of the list
    */


  def getIndexForDisplayTime(sourceData: MeterRecord, destinationDataList: List[MeterRecord]): Int = {

    val index = destinationDataList.indexWhere {
      y =>
        y.PatientId.equals(sourceData.PatientId) &&
          y.DisplayTime.equals(sourceData.DisplayTime) &&
          y.EntryType.equals(sourceData.EntryType) &&
          y.Model.equals(sourceData.Model)
    }
    index
  }

}
