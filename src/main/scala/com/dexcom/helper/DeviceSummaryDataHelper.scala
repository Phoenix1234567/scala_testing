package com.dexcom.helper

import com.dexcom.common.CassandraQueries
import com.dexcom.configuration.DexVictoriaConfigurations
import com.dexcom.connection.CassandraConnection
import com.dexcom.dto._
import com.dexcom.utils.Utils._
import org.slf4j.LoggerFactory

import scala.collection.mutable.ListBuffer

class DeviceSummaryDataHelper extends DexVictoriaConfigurations with CassandraQueries {

  val logger = LoggerFactory.getLogger("DeviceSummaryHelper")

  /**
    * this method fetch data from CSV files
    *
    * @return the list of DeviceSummary
    */
  def getDeviceSummaryFromCSV: List[DeviceSummary] = {

    val list_device_summary = new ListBuffer[DeviceSummary]
    val post_records = postRecords
    val device_summary_csv = scala.io.Source.fromFile(device_settings_record_path)

    for (
      list_post <- post_records;
      list_patient <- patientRecords() //;
    //line <- device_summary_csv.getLines().drop(1)
    ) {
      //val cols = line.split(Constants.Splitter).map(_.trim)
      val device_summary = DeviceSummary(
        PatientId = list_patient.PatientId,
        Model = deviceModel,
        SerialNumber = serialNumber,
        CreateDate = list_post.PostedTimestamp, // createDate = post_records.postedTimestamp
        LastUpdateDate = list_post.PostedTimestamp //lastUpdateDate = post_records.postedTimestamp
      )
      list_device_summary += device_summary
    }
    device_summary_csv.close()

    list_device_summary.toList
  }

  /*
  * Fetch records from Cassandra Device Summary
  * @return list of records
  * */
  def getDeviceSummaryRecordsFromCassandra: List[DeviceSummary] = {
    val list_device_summary = new ListBuffer[DeviceSummary]
    val cassandra_connection = new CassandraConnection
    val session = cassandra_connection.getConnection // get cassandra connection

    val resultSet = session.execute(GET_DEVICE_SUMMARY)
    while (!resultSet.isExhausted) {
      val row = resultSet.one()
      val device_summary_records = DeviceSummary(
        PatientId = row.getUUID("patient_id"),
        Model = row.getString("model"),
        SerialNumber = row.getString("serial_number"),
        CreateDate = row.getTimestamp("create_date"),
        LastUpdateDate = row.getTimestamp("last_update_date")
      )
      list_device_summary += device_summary_records
    }
    cassandra_connection.closeConnection() //close cassandra connection
    list_device_summary.toList
  }

  /*
    this method returns the index of the list of cassandra data where source record matches
    @param sourceData of the source DeviceSummary
    @param destinationDataList of the list of cassandra's getDeviceSummaryRecordsFromCassandra
    @return the index of the list
   */
  def getIndexForDeviceSummary(sourceData: DeviceSummary, destinationDataList: List[DeviceSummary]): Int = {

    val index = destinationDataList.indexWhere {
      y =>
        y.PatientId.equals(sourceData.PatientId) &&
          y.Model.equals(sourceData.Model) &&
          y.SerialNumber.equals(sourceData.SerialNumber)
    }
    index
  }
}