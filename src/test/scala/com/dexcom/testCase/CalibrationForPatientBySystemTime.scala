package com.dexcom.testCase

import java.util.Date

import com.dexcom.helper.CalibrationDataHelper
import org.scalatest.FunSuite
/**
  * Created by aditi.nandwana on 20-01-2017.
  */
class CalibrationForPatientBySystemTime extends  FunSuite{

  val meterRecordTestCase = new CalibrationDataHelper
  val list_meter_record_cassandra = meterRecordTestCase.getCalibrationForPatientBySystemTimeRecordsFromCassandra
  val list_meter_record_csv = meterRecordTestCase.getMeterRecordsFromCSV

  test("TC_333 --~> should verify System Time of the CalibrationForPatientBySystemTime in cassandra is populating properly"){
    //verify the results
    list_meter_record_cassandra.foreach {
      x =>
        assert(x.SystemTime !== null)
        assert(x.SystemTime !== "")
        assert(x.SystemTime.isInstanceOf[Date])
    }

    list_meter_record_csv.foreach {
      x =>
        val index = meterRecordTestCase.getIndexForSystemTime(x, list_meter_record_cassandra)
        if(index != -1) {
          assert(x.SystemTime === list_meter_record_cassandra(index).SystemTime)
        } else {
          fail(s"Record not found : $x")
        }
    }
  }

  test("TC_335 --~> should verify Display Time of the CalibrationForPatientBySystemTime in cassandra is populating properly"){

    //verify the results
    list_meter_record_cassandra.foreach {
      x =>
        assert(x.DisplayTime !== null)
        assert(x.DisplayTime !== "")
        assert(x.DisplayTime.isInstanceOf[Date])
    }

    list_meter_record_csv.foreach {
        x =>
          val index = meterRecordTestCase.getIndexForSystemTime(x, list_meter_record_cassandra)
          if(index != -1) {
            assert(x.DisplayTime === list_meter_record_cassandra(index).DisplayTime)
          } else {
            fail(s"Record not found : $x")
          }
    }

  }

  test("TC_364 --~> should verify Entry Type of CalibrationForPatientBySystemTime in cassandra is populating properly"){

    //verify the results
    list_meter_record_cassandra.foreach {
      x =>
        assert(x.EntryType !== null)
        assert(x.EntryType !== "")
        assert(x.EntryType.isInstanceOf[String])
    }
    list_meter_record_csv.foreach {
      x =>
        val index = meterRecordTestCase.getIndexForSystemTime(x, list_meter_record_cassandra)

        if(list_meter_record_cassandra(index).Model === "G4"){
          assert(list_meter_record_cassandra(index).EntryType === "None")
        }
        else{
         // assert()
        }
    }

  }




}
