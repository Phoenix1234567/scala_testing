package com.dexcom.testCase

import java.text.{ParseException, SimpleDateFormat}
import java.util.Date

import com.dexcom.common.CassandraQueries
import com.dexcom.connection.CassandraConnection
import com.dexcom.dto.{DeviceSummary, EGVForPatientBySystemTime}
import org.scalatest.{BeforeAndAfterAll, FunSuite}

import scala.collection.mutable.ListBuffer

/**
  * Created by sarvaraj on 11/01/17.
  */
class TC_327 extends FunSuite with CassandraQueries with BeforeAndAfterAll {

  val cassandraConnection = new CassandraConnection().getConnection
  var list_device_summary: ListBuffer[DeviceSummary] = null
  initDataSet()
  cassandraConnection.close();

  def initDataSet(): Unit = {
    this.list_device_summary = new ListBuffer[DeviceSummary]
    val resultSet = cassandraConnection.execute(GET_DEVICE_SUMMARY)
    while (!resultSet.isExhausted) {
      val row = resultSet.one()
      val device_summary_record = DeviceSummary(
        PatientId = row.getUUID("patient_id"),
        Model = row.getString("model"),
        SerialNumber = row.getString("serial_number"),
        CreateDate = row.getTimestamp("create_date"),
        LastUpdateDate = row.getTimestamp("last_update_date")

      )
      list_device_summary += device_summary_record
    }
  }

  test("Verify schema of model od device_sumary") {
    //initiate
    assert(list_device_summary.head.Model.isInstanceOf[String])
    assert(list_device_summary.head.Model === "G5" || list_device_summary.head.Model === "G4")
    // fetch data frm cassandra

  }

  def stringToDate(dateString : String) : Either[Unit, Date]= {
    val df = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSSz") //2014-05-16T20:06:17.1592279Z
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

}
