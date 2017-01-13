package com.dexcom.testCase

import java.util.{Date, UUID}

import com.dexcom.testCases.GlucoseRecordTestCase
import org.scalatest.FunSuite

/**
  * Created by gaurav.garg on 10-01-2017.
  */
class TC_165_Verify_schema_of_EGVForPatientBySystemTime extends FunSuite {

  //initiate
  val glucoseRecordTestCase = new GlucoseRecordTestCase
  val list_glucose_record_cassandra = glucoseRecordTestCase.EGVRecordsDestination()
  println(list_glucose_record_cassandra)
  val list_glucose_record_csv = glucoseRecordTestCase.EGVRecordsSource()
  println(list_glucose_record_csv)
  test("should verify PatientId of the EGVForPatientBySystemTime in cassandra is populating properly") {

    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.PatientId.isInstanceOf[UUID])
        assert(x.PatientId !== null)
        assert(x.PatientId !== "")

    }

    list_glucose_record_csv.foreach {
      x =>
        list_glucose_record_cassandra.foreach {
          y =>
            if(x === y) {
              assert(x.PatientId === y.PatientId)
            } else {
              fail(s"No record found with $x")
            }
        }
    }

  }

  test("should test dataType of the column SystemTime in EGVForPatientBySystemTime in cassandra") {

    //verify the results
    assert(list_glucose_record_cassandra.head.SystemTime.isInstanceOf[Date])
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.SystemTime !== null)
        assert(x.SystemTime !== "")
    }

  }

  test("should test dataType of the column PostId in EGVForPatientBySystemTime in cassandra") {

    //verify the results
    assert(list_glucose_record_cassandra.head.PostId.isInstanceOf[UUID])

  }

  test("should test dataType of the column DisplayTime in EGVForPatientBySystemTime in cassandra") {

    //verify the results
    assert(list_glucose_record_cassandra.head.DisplayTime.isInstanceOf[Date])

  }

  test("should test dataType of the column IngestionTimestamp in EGVForPatientBySystemTime in cassandra") {

    //verify the results
    assert(list_glucose_record_cassandra.head.IngestionTimestamp.isInstanceOf[Date])

  }

  test("should test dataType of the column RateUnits in EGVForPatientBySystemTime in cassandra") {

    //verify the results
    assert(list_glucose_record_cassandra.head.RateUnits.isInstanceOf[String])
    assert(list_glucose_record_cassandra.head.RateUnits === "mg/dL/min" || list_glucose_record_cassandra.head.RateUnits === "mmol/L/min")

  }

  test("should test dataType of the column Source in EGVForPatientBySystemTime in cassandra") {

    //verify the results
    assert(list_glucose_record_cassandra.head.Source.isInstanceOf[String])
    assert(list_glucose_record_cassandra.head.Source === "Receiver" || list_glucose_record_cassandra.head.Source === "Phone")

  }

  test("should test dataType of the column Status in EGVForPatientBySystemTime in cassandra") {

    //verify the results
    if(list_glucose_record_cassandra.head.Status != null)
      assert(list_glucose_record_cassandra.head.Status.isInstanceOf[String])


  }

  test("should test dataType of the column TransmitterId in EGVForPatientBySystemTime in cassandra") {


    //verify the results
    assert(list_glucose_record_cassandra.head.TransmitterId.isInstanceOf[String])

  }

  test("should test dataType of the column TransmitterTicks in EGVForPatientBySystemTime in cassandra") {

    //verify the results
    assert(list_glucose_record_cassandra.head.TransmitterTicks.isInstanceOf[Long])

  }

  test("should test dataType of the column Trend in EGVForPatientBySystemTime in cassandra") {

    //verify the results
    assert(list_glucose_record_cassandra.head.Trend.isInstanceOf[String])

  }

  test("should test dataType of the column TrendRate in EGVForPatientBySystemTime in cassandra") {

    //verify the results
    assert(list_glucose_record_cassandra.head.TrendRate.isInstanceOf[Double])
    assert((list_glucose_record_cassandra.head.TrendRate >= -8 && list_glucose_record_cassandra.head.TrendRate <= 8) || list_glucose_record_cassandra.head.TrendRate === "")
  }

  test("should test dataType of the column Units in EGVForPatientBySystemTime in cassandra") {

    //verify the results
    assert(list_glucose_record_cassandra.head.Units.isInstanceOf[String])
    assert(list_glucose_record_cassandra.head.Units === "mg/dL" || list_glucose_record_cassandra.head.Units === "mmol/L")

  }

  test("should test dataType of the column Value in EGVForPatientBySystemTime in cassandra") {

    //verify the results
    assert(list_glucose_record_cassandra.head.Value.isInstanceOf[Int])
    assert(list_glucose_record_cassandra.head.Value >= 0 && list_glucose_record_cassandra.head.Value <= 400)

  }
}

