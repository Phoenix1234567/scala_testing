package com.dexcom.testCase

import java.text.{ParseException, SimpleDateFormat}
import java.util.{Date, UUID}

import com.dexcom.common.CassandraQueries
import com.dexcom.connection.CassandraConnection
import com.dexcom.dto.{DeviceSummary, EGVForPatient}
import org.scalatest.{BeforeAndAfterAll, FunSuite}

import scala.collection.mutable.ListBuffer

/**
  * Created by sarvaraj on 11/01/17.
  */
class Device_Summary extends FunSuite with CassandraQueries with BeforeAndAfterAll {

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





  test("TC_327 : Verify schema and value of model of device_sumary") {
    //initiate
    assert(list_device_summary.head.Model.isInstanceOf[String])
    assert(list_device_summary.head.Model === "G5" || list_device_summary.head.Model === "G4")
    // fetch data frm cassandra
  }

  test("TC_328 : Verify schema  and value of serialNumber of device_sumary") {
    //initiate
    assert(list_device_summary.head.SerialNumber.isInstanceOf[String])
    assert(list_device_summary.head.SerialNumber.length >4 && list_device_summary.head.SerialNumber.length <=6 )
    // fetch data frm cassandra
  }

  test("TC_380 : Verify schema and value of serialNumber of device_sumary") {
    //initiate
    assert(list_device_summary.head.PatientId.isInstanceOf[UUID])
  //  assert(list_device_summary.head.PatientId.equals(123e4567-e89b-12d3-a456-426655440000))
    // fetch data frm cassandra
  }
}
