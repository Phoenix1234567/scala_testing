package com.dexcom.testCase

import java.text.{ParseException, SimpleDateFormat}
import java.util.{Date, UUID}

import com.dexcom.common.CassandraQueries
import com.dexcom.connection.CassandraConnection
import com.dexcom.dto.{DeviceSummary, EGVForPatient}
import com.dexcom.helper.{DeviceSummaryDataHelper, GlucoseDataHelper}
import org.scalatest.{BeforeAndAfterAll, FunSuite}

import scala.collection.mutable.ListBuffer

/**
  * Created by sarvaraj on 11/01/17.
  */
class DeviceSummary extends FunSuite  {

  //initiate
  val deviceSummaryTestCase = new DeviceSummaryDataHelper
  val list_device_summary_cassandra = deviceSummaryTestCase.getDeviceSummaryRecordsFromCassandra
  val list_device_summary_csv = deviceSummaryTestCase.getDeviceSummaryFromCSV

  test("TC_327 --~> should verify Model of DeviceSummary in Cassandra is populating properly") {
    //verify the results
    list_device_summary_cassandra.foreach {
      x=>
        assert(x.Model !== null)
        assert(x.Model !== "")
        assert(x.Model.isInstanceOf[String])
    }
    list_device_summary_csv.foreach{
      x=>
        val index = deviceSummaryTestCase.getIndexForDeviceSummary(x,list_device_summary_cassandra)
        if(index != -1){
          assert(list_device_summary_cassandra(index).Model === "G4" ||
            list_device_summary_cassandra(index).Model === "G5" )
          assert(x.Model === list_device_summary_cassandra(index).Model)
        }
        else{
          fail(s"Record not found : $x")
        }
    }
  }

  test("TC_328 --~> should verify SerialNumber of DeviceSummary in Cassandra is populating properly") {
    list_device_summary_cassandra.foreach {
      x=>
        assert(x.SerialNumber !== null)
        assert(x.SerialNumber !== "")
        assert(x.SerialNumber.isInstanceOf[String])
    }
    list_device_summary_csv.foreach{
      x=>
        val index = deviceSummaryTestCase.getIndexForDeviceSummary(x,list_device_summary_cassandra)
        if(index != -1){
          assert(x.SerialNumber === list_device_summary_cassandra(index).SerialNumber)
        }
        else{
          fail(s"Record not found : $x")
        }
    }
  }

  test("TC_329 --~> should verify CreateDate of DeviceSummary in Cassandra is populating properly") {
    list_device_summary_cassandra.foreach{
      x=>
        assert(x.CreateDate !== null)
        assert(x.CreateDate !== "")
        assert(x.CreateDate.isInstanceOf[Date])
    }
    list_device_summary_csv.foreach{
      x=>
        val index = deviceSummaryTestCase.getIndexForDeviceSummary(x,list_device_summary_cassandra)
        if(index != -1){
          assert(x.CreateDate === list_device_summary_cassandra(index).CreateDate)
        }
        else{
          fail(s"Record not found : $x")
        }
    }
  }

  test("TC_330 --~> should verify LastUpdateDate of DeviceSummary in Cassandra is populating properly"){
    list_device_summary_cassandra.foreach{
      x=>
         assert(x.LastUpdateDate !== null)
         assert(x.LastUpdateDate !== "")
         assert(x.LastUpdateDate.isInstanceOf[Date])
    }
    list_device_summary_csv.foreach{
      x=>
        val index = deviceSummaryTestCase.getIndexForDeviceSummary(x,list_device_summary_cassandra)
        if(index != -1){
          assert(x.LastUpdateDate === list_device_summary_cassandra(index).LastUpdateDate)
        }
        else{
          fail(s"Record not found : $x")
        }
    }
  }

  test("TC_380 --~> should verify LastUpdateDate of DeviceSummary in Cassandra is populating properly"){
    list_device_summary_cassandra.foreach{
      x=>
        assert(x.PatientId !== null)
        assert(x.PatientId !== "")
        assert(x.PatientId.isInstanceOf[UUID])
    }
    list_device_summary_csv.foreach{
      x=>
        val index = deviceSummaryTestCase.getIndexForDeviceSummary(x,list_device_summary_cassandra)
        if(index != -1){
          assert(x.PatientId === list_device_summary_cassandra(index).PatientId)
        }
    }
  }
}
