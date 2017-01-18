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
  val list_glucose_record_cassandra = glucoseRecordTestCase.getRecordsFromCassandra
  val list_glucose_record_csv = glucoseRecordTestCase.getRecordsFromCSV

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
        if (x.Status != null) {
          assert(x.Status.isInstanceOf[String])
        }

        val index = glucoseRecordTestCase.getIndex(x, list_glucose_record_cassandra)
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
    }
  }

  test("TC_375 : should verify TransmitterId of the EGVForPatient in cassandra is populating properly") {
    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.TransmitterId !== null)
        assert(x.TransmitterId !== "")
        assert(x.TransmitterId.isInstanceOf[String])
    }

    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndex(x, list_glucose_record_cassandra)
        assert(x.TransmitterId === list_glucose_record_cassandra(index).TransmitterId)
        assert((5 to 6) contains list_glucose_record_cassandra(index).TransmitterId.length)
    }
  }

  test("TC_374 : should verify TransmitterTicks of the EGVForPatient in cassandra is populating properly") {
    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.TransmitterTicks !== null)
        assert(x.TransmitterTicks !== "")
        assert(x.TransmitterTicks.isInstanceOf[Long])
    }
    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndex(x, list_glucose_record_cassandra)
        assert(x.TransmitterId === list_glucose_record_cassandra(index).TransmitterId)
    }
  }

  /*test("should test dataType of the column Trend in EGVForPatient in cassandra") {
    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.Trend !== null)
        assert(x.Trend.isInstanceOf[String])
    }

    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndex(x, list_glucose_record_cassandra)
        assert(x.Trend === list_glucose_record_cassandra(index).Trend)
    }
    list_glucose_record_cassandra.foreach {
      x =>

        assert(x.Trend === "None" || x.Trend === "DoubleUp" || x.Trend === "SingleUp" || x.Trend === "FortyFiveUp" || x.Trend === "Flat")
    }
  }*/

  test("TC_362 : should verify TrendRate of the EGVForPatient in cassandra is populating properly") {
    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.TrendRate !== "")
        assert(x.TrendRate.isInstanceOf[Double])
    }
    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndex(x, list_glucose_record_cassandra)
        assert(x.TrendRate === list_glucose_record_cassandra(index).TrendRate)

        if(list_glucose_record_cassandra(index).TrendRate !== null)
          assert((-8.0 until 8.0 by 0.1) contains list_glucose_record_cassandra(index).TrendRate)
        else
          assert(
            list_glucose_record_cassandra(index).Value === 0 &&
            list_glucose_record_cassandra(index).TrendRate === null
          )

    }
  }

  test("TC_361 : should verify Units of the EGVForPatient in cassandra is populating properly") {
    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.Units !== null)
        assert(x.Units !== "")
        assert(x.Units.isInstanceOf[String])
    }
    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndex(x, list_glucose_record_cassandra)
        assert(x.Units === list_glucose_record_cassandra(index).Units)
        assert(
          list_glucose_record_cassandra(index).Units === "mg/dL" ||
            list_glucose_record_cassandra(index).Units === "mmol/L"
        )
    }
  }

  test("TC_360 : should verify Value of the EGVForPatient in cassandra is populating properly") {
    //verify the results
    list_glucose_record_cassandra.foreach {
      x =>
        assert(x.Value !== null)
        assert(x.Value !== "")
        assert(x.Value.isInstanceOf[Int])
    }
    list_glucose_record_csv.foreach {
      x =>
        val index = glucoseRecordTestCase.getIndex(x, list_glucose_record_cassandra)
        assert(x.Value === list_glucose_record_cassandra(index).Value)
        assert((
          list_glucose_record_cassandra(index).Value === 0 &&
            (list_glucose_record_cassandra(index).Status.equalsIgnoreCase("OutOfCalibration") ||
              list_glucose_record_cassandra(index).Status.equalsIgnoreCase("SensorWarmUp") ||
              list_glucose_record_cassandra(index).Status.equalsIgnoreCase("SensorNoise")
              ))||
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
    }
  }
}
