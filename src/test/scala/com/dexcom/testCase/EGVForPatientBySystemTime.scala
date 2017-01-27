package com.dexcom.testCase

import java.util
import java.util.{Date,UUID}

import com.dexcom.helper.GlucoseDataHelper
import com.dexcom.utils.Utils._
import org.scalatest.FunSuite

/**
  * Created by gaurav.garg on 10-01-2017.
  */
class EGVForPatientBySystemTime extends FunSuite {

  //initiate
  val glucoseRecordTestCase = new GlucoseDataHelper

  //getting data from Cassandra as list
  val list_glucose_record_cassandra = glucoseRecordTestCase.getEGVForPatientBySystemTimeRecordsFromCassandra

  //getting data from CSV as list
  val list_glucose_record_csv = glucoseRecordTestCase.getGlucoseRecordsFromCSV

  test("TC_360 --~> should verify Value of the EGVForPatientBySystemTime in cassandra is populating properly") {
    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.Value !== null)
        assert(x.Value !== "")
        assert(x.Value.isInstanceOf[Int])
    }
    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndexForSystemTime(x, list_glucose_record_cassandra)
        if(index != -1) {
          assert(x.Value === list_glucose_record_cassandra(index).Value)
          assert((
            list_glucose_record_cassandra(index).Value === 0 &&
              (list_glucose_record_cassandra(index).Status.equalsIgnoreCase("OutOfCalibration") ||
                list_glucose_record_cassandra(index).Status.equalsIgnoreCase("SensorWarmUp") ||
                list_glucose_record_cassandra(index).Status.equalsIgnoreCase("SensorNoise")
                )) ||
            (
              list_glucose_record_cassandra(index).Value < 40 &&
                list_glucose_record_cassandra(index).Status.equalsIgnoreCase("Low")
              ) ||
            (((40 to 400) contains list_glucose_record_cassandra(index).Value) &&
              (
                list_glucose_record_cassandra(index).Status === null ||
                  list_glucose_record_cassandra(index).Status === ""
                )) ||
            (
              list_glucose_record_cassandra(index).Value > 400 &&
                list_glucose_record_cassandra(index).Status.equalsIgnoreCase("High")
              ))
        } else {
          fail(s"Record not found : $x")
        }
    }
  }

  test("TC_361 --~> should verify Units of the EGVForPatientBySystemTime in cassandra is populating properly") {
    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.Units !== null)
        assert(x.Units !== "")
        assert(x.Units.isInstanceOf[String])
    }
    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndexForSystemTime(x, list_glucose_record_cassandra)
        if(index != -1) {
          assert(x.Units === list_glucose_record_cassandra(index).Units)
          assert(
            list_glucose_record_cassandra(index).Units === "mg/dL" ||
              list_glucose_record_cassandra(index).Units === "mmol/L"
          )
        } else{
          fail(s"Record not found : $x")
        }
    }
  }

  test("TC_362 --~> should verify TrendRate of the EGVForPatientBySystemTime in cassandra is populating properly") {
    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.TrendRate !== "")
        assert(x.TrendRate.isInstanceOf[Double])
    }
    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndexForSystemTime(x, list_glucose_record_cassandra)
        if(index != -1) {
          assert(x.TrendRate === list_glucose_record_cassandra(index).TrendRate)

          if (list_glucose_record_cassandra(index).TrendRate !== null)
            assert(doubleFormatting(-8.0 to 8.0 by 0.1) contains list_glucose_record_cassandra(index).TrendRate)
          else
            assert(
              list_glucose_record_cassandra(index).Value === 0 &&
                list_glucose_record_cassandra(index).TrendRate === null
            )
        } else{
          fail(s"Record not found : $x")
        }
    }
  }

  test("TC_363 --~> should verify Trend of the EGVForPatientBySystemTime in cassandra is populating properly") {

    //initiate
    val trendAndTrendRateMapping = new util.HashMap[String, List[Double]]
    trendAndTrendRateMapping.put("doubleup", doubleFormatting(3.0 to 8.0 by 0.1))
    trendAndTrendRateMapping.put("singleup", doubleFormatting(2.0 until 3.0 by 0.1))
    trendAndTrendRateMapping.put("fortyfiveup", doubleFormatting(1.0 until 2.0 by 0.1))
    trendAndTrendRateMapping.put("flat", doubleFormatting(-1.1 until 1.0 by 0.1))
    trendAndTrendRateMapping.put("fortyfivedown", doubleFormatting(-2.1 to -1.0 by 0.1))
    trendAndTrendRateMapping.put("singledown", doubleFormatting(-3.1 to -2.0 by 0.1))
    trendAndTrendRateMapping.put("doubledown", doubleFormatting(-8.0 to -3.0 by 0.1))

    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.Trend !== null)
        assert(x.Trend.isInstanceOf[String])
    }

    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndexForSystemTime(x, list_glucose_record_cassandra)
        if(index != -1) {
          assert(x.Trend === list_glucose_record_cassandra(index).Trend)

          if (list_glucose_record_cassandra(index).TrendRate === "")
            assert(list_glucose_record_cassandra(index).Trend === None)
          else if (!(doubleFormatting(-8.0 to 8.0 by 0.1) contains list_glucose_record_cassandra(index).TrendRate))
            assert(list_glucose_record_cassandra(index).Trend.equalsIgnoreCase("NotComputable"))
          else
            assert(
              trendAndTrendRateMapping.get(list_glucose_record_cassandra(index).Trend.toLowerCase)
                .contains(list_glucose_record_cassandra(index).TrendRate)
            )
        }else{
          fail(s"Record not found : $x")
        }
    }
  }

  test("TC_374 --~> should verify TransmitterTicks of the EGVForPatientBySystemTime in cassandra is populating properly") {
    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.TransmitterTicks !== null)
        assert(x.TransmitterTicks !== "")
        assert(x.TransmitterTicks.isInstanceOf[Long])
    }
    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndexForSystemTime(x, list_glucose_record_cassandra)
        if(index != -1) {
          assert(x.TransmitterId === list_glucose_record_cassandra(index).TransmitterId)
        } else{
          fail(s"Record not found : $x")
        }
    }
  }

  test("TC_375 --~> should verify TransmitterId of the EGVForPatientBySystemTime in cassandra is populating properly") {
    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.TransmitterId !== null)
        assert(x.TransmitterId.isInstanceOf[String])
    }

    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndexForSystemTime(x, list_glucose_record_cassandra)
        if(index != -1) {
          assert(x.TransmitterId === list_glucose_record_cassandra(index).TransmitterId)
          assert((5 to 6) contains list_glucose_record_cassandra(index).TransmitterId.length)
        } else {
          fail(s"Record not found : $x")
        }
    }
  }

  test("TC_376 --~> should verify DisplayTime of the EGVForPatientBySystemTime in cassandra is populating properly") {

    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>

        assert(x.DisplayTime !== null)
        assert(x.DisplayTime !== "")
        assert(x.DisplayTime.isInstanceOf[Date])
    }

    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndexForSystemTime(x, list_glucose_record_cassandra)
        if(index != -1) {
          assert(x.DisplayTime === list_glucose_record_cassandra(index).DisplayTime)
        } else {
          fail(s"Record not found : $x")
        }
    }
  }

  test("TC_377 --~> should verify Status of the EGVForPatientBySystemTime in cassandra is populating properly") {

    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        if (x.Status != null) {
          assert(x.Status.isInstanceOf[String])
        }

        val index = glucoseRecordTestCase.getIndexForSystemTime(x, list_glucose_record_cassandra)
        if(index != -1) {
          assert(x.Status === list_glucose_record_cassandra(index).Status)

          assert(
            list_glucose_record_cassandra(index).Status === "High" ||
              list_glucose_record_cassandra(index).Status === "Low" ||
              list_glucose_record_cassandra(index).Status === "OutOfCalibration" ||
              list_glucose_record_cassandra(index).Status === "SensorWarmUp" ||
              list_glucose_record_cassandra(index).Status === "SensorNoise" ||
              list_glucose_record_cassandra(index).Status === "" ||
              list_glucose_record_cassandra(index).Status === null
          )
        } else{
          fail(s"Record not found : $x")
        }
    }
  }

  test("TC_378 --~> should verify PatientId of the EGVForPatientBySystemTime in cassandra is populating properly") {

    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.PatientId !== null)
        assert(x.PatientId !== "")
        assert(x.PatientId.isInstanceOf[UUID])
    }

    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndexForSystemTime(x, list_glucose_record_cassandra)
        if(index != -1) {
          assert(x.PatientId === list_glucose_record_cassandra(index).PatientId)
        } else{
          fail(s"Record not found : $x")
        }
    }
  }

  test("TC_381 --~> should verify SystemTime of the EGVForPatientBySystemTime in cassandra is populating properly") {

    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.SystemTime !== null)
        assert(x.SystemTime !== "")
        assert(x.SystemTime.isInstanceOf[Date])
    }

    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndexForSystemTime(x, list_glucose_record_cassandra)
        if(index != -1) {
          assert(x.SystemTime === list_glucose_record_cassandra(index).SystemTime)
        } else {
          fail(s"Record not found : $x")
        }
    }
  }

  test("TC_382 --~> should verify PostId of the EGVForPatientBySystemTime in cassandra is populating properly") {

    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.PostId !== null)
        assert(x.PostId !== "")
        assert(x.PostId.isInstanceOf[UUID])
    }

    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndexForSystemTime(x, list_glucose_record_cassandra)
        if(index != -1) {
          assert(x.PostId === list_glucose_record_cassandra(index).PostId)
        } else {
          fail(s"Record not found : $x")
        }
    }
  }

  test("TC_383 --~> should verify IngestionTimestamp of the EGVForPatientBySystemTime in cassandra is populating properly") {

    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.IngestionTimestamp !== null)
        assert(x.IngestionTimestamp !== "")
        assert(x.IngestionTimestamp.isInstanceOf[Date])
    }

    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndexForSystemTime(x, list_glucose_record_cassandra)
        if(index != -1) {
          assert(x.DisplayTime === list_glucose_record_cassandra(index).IngestionTimestamp)
        } else{
          fail(s"Record not found : $x")
        }
    }
  }

  test("TC_384 --~> should verify RateUnits of the EGVForPatientBySystemTime in cassandra is populating properly") {

    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.RateUnits !== null)
        assert(x.RateUnits !== "")
        assert(x.RateUnits.isInstanceOf[String])
    }

    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndexForSystemTime(x, list_glucose_record_cassandra)
        if(index != -1) {
          assert(x.RateUnits === list_glucose_record_cassandra(index).RateUnits)
          assert(
            list_glucose_record_cassandra(index).RateUnits === "mg/dL/min" ||
              list_glucose_record_cassandra(index).RateUnits === "mmol/L/min"
          )
        } else {
          fail(s"Record not found : $x")
        }
    }
  }

  test("TC_385 --~> should verify Source of the EGVForPatientBySystemTime in cassandra is populating properly") {

    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.Source !== null)
        assert(x.Source !== "")
        assert(x.Source.isInstanceOf[String])
    }

    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndexForSystemTime(x, list_glucose_record_cassandra)
        if(index != -1) {
          assert(x.Source === list_glucose_record_cassandra(index).Source)
          assert(
            list_glucose_record_cassandra(index).Source === "Receiver" ||
              list_glucose_record_cassandra(index).Source === "Phone"
          )
        } else {
          fail(s"Record not found : $x")
        }
    }
  }
}
