package com.dexcom.testCase

import java.util.{Date, UUID}

import com.dexcom.helper.GlucoseDataHelper
import org.scalatest.FunSuite

/**
  * Created by gaurav.garg on 10-01-2017.
  */
class EGVForPatientBySystemTime extends FunSuite {

  //initiate
  val glucoseRecordTestCase = new GlucoseDataHelper
  val list_glucose_record_cassandra = glucoseRecordTestCase.EGVForPatientBySystemTime()
  val list_glucose_record_csv = glucoseRecordTestCase.EGVForPatient()

  test("TC_378 : should verify PatientId of the EGVForPatient in cassandra is populating properly") {

    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.PatientId !== null)
        assert(x.PatientId !== "")
        assert(x.PatientId.isInstanceOf[UUID])
    }

    list_glucose_record_csv.foreach {
        x =>
          val index = glucoseRecordTestCase.getIndex(x, list_glucose_record_cassandra)
          assert(x.PatientId === list_glucose_record_cassandra(index).PatientId)
    }
  }

  test("TC_381 : should verify SystemTime of the EGVForPatient in cassandra is populating properly") {

    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.SystemTime !== null)
        assert(x.SystemTime !== "")
        assert(x.SystemTime.isInstanceOf[Date])
    }

    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndex(x, list_glucose_record_cassandra)
        assert(x.SystemTime === list_glucose_record_cassandra(index).SystemTime)
    }
  }

  test("TC_382 : should verify PostId of the EGVForPatient in cassandra is populating properly") {

    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.PostId !== null)
        assert(x.PostId !== "")
        assert(x.PostId.isInstanceOf[UUID])
    }

    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndex(x, list_glucose_record_cassandra)
        assert(x.PostId === list_glucose_record_cassandra(index).PostId)
    }
  }

  test("TC_376 : should verify DisplayTime of the EGVForPatient in cassandra is populating properly") {

    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>

        assert(x.DisplayTime !== null)
        assert(x.DisplayTime !== "")
        assert(x.DisplayTime.isInstanceOf[Date])
    }

    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndex(x, list_glucose_record_cassandra)
        assert(x.DisplayTime === list_glucose_record_cassandra(index).DisplayTime)
    }
  }

  test("TC_383 : should verify IngestionTimestamp of the EGVForPatient in cassandra is populating properly") {

    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.IngestionTimestamp !== null)
        assert(x.IngestionTimestamp !== "")
        assert(x.IngestionTimestamp.isInstanceOf[Date])
    }

    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndex(x, list_glucose_record_cassandra)
        assert(x.DisplayTime === list_glucose_record_cassandra(index).IngestionTimestamp)
    }

  }

  test("TC_384 : should verify RateUnits of the EGVForPatient in cassandra is populating properly") {

    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.RateUnits !== null)
        assert(x.RateUnits !== "")
        assert(x.RateUnits.isInstanceOf[String])
    }

    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndex(x, list_glucose_record_cassandra)
        assert(x.RateUnits === list_glucose_record_cassandra(index).RateUnits)
        assert(
          list_glucose_record_cassandra(index).RateUnits === "mg/dL/min" ||
          list_glucose_record_cassandra(index).RateUnits === "mmol/L/min"
        )
    }
  }

  test("TC_385 : should verify Source of the EGVForPatient in cassandra is populating properly") {

    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.Source !== null)
        assert(x.Source !== "")
        assert(x.Source.isInstanceOf[String])
    }

    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndex(x, list_glucose_record_cassandra)
        assert(x.Source === list_glucose_record_cassandra(index).Source)
        assert(
          list_glucose_record_cassandra(index).Source === "Receiver" ||
            list_glucose_record_cassandra(index).Source === "Phone"
        )
    }
  }

  test("TC_377 : should verify Status of the EGVForPatient in cassandra is populating properly") {

    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        if(x.Status != null) {
          assert(x.Status.isInstanceOf[String])
        }

        val index = glucoseRecordTestCase.getIndex(x, list_glucose_record_cassandra)
        assert(x.Status === list_glucose_record_cassandra(index).Status)

        assert(
          x.Status === "High" ||
          x.Status === "Low" ||
          x.Status === "OutOfCalibration" ||
          x.Status === "SensorWarmUp" ||
          x.Status === "SensorNoise" ||
          x.Status === "" ||
          x.Status === null
        )
    }
}

  test("should test dataType of the column TransmitterId in EGVForPatient in cassandra") {


    //verify the results
    assert(list_glucose_record_cassandra.head.TransmitterId.isInstanceOf[String])

  }

  test("should test dataType of the column TransmitterTicks in EGVForPatient in cassandra") {

    //verify the results
    assert(list_glucose_record_cassandra.head.TransmitterTicks.isInstanceOf[Long])

  }

  test("should test dataType of the column Trend in EGVForPatient in cassandra") {

    //verify the results
     list_glucose_record_cassandra.foreach {
      x =>
        assert(x.Trend.isInstanceOf[String])
        assert(x.Trend === "None" || x.Trend === "DoubleUp" || x.Trend === "SingleUp" || x.Trend === "FortyFiveUp" || x.Trend === "Flat")
    }

     /*list_glucose_record_csv.foreach {
      x =>
        list_glucose_record_cassandra.foreach {
          y =>
            if (x === y) {
              assert(x.Trend === y.Trend)
            } else {
              fail(s"No record found with $x")
            }
        }
    }*/

    }

  test("should test dataType of the column TrendRate in EGVForPatient in cassandra") {

    //verify the results
    assert(list_glucose_record_cassandra.head.TrendRate.isInstanceOf[Double])
    assert((list_glucose_record_cassandra.head.TrendRate >= -8 && list_glucose_record_cassandra.head.TrendRate <= 8) || list_glucose_record_cassandra.head.TrendRate === "")
  }

  test("should test dataType of the column Units in EGVForPatient in cassandra") {

    //verify the results
    assert(list_glucose_record_cassandra.head.Units.isInstanceOf[String])
    assert(list_glucose_record_cassandra.head.Units === "mg/dL" || list_glucose_record_cassandra.head.Units === "mmol/L")

  }

  test("should test dataType of the column Value in EGVForPatient in cassandra") {

    //verify the results
    assert(list_glucose_record_cassandra.head.Value.isInstanceOf[Int])
    assert(list_glucose_record_cassandra.head.Value >= 0 && list_glucose_record_cassandra.head.Value <= 400)

  }
}
