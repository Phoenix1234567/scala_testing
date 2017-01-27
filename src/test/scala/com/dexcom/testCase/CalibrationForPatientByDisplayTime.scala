package com.dexcom.testCase

import java.util.{Date, UUID}

import com.dexcom.helper.CalibrationDataHelper
import org.scalatest.FunSuite
/**
  * Created by aditi.nandwana on 20-01-2017.
  */
class CalibrationForPatientByDisplayTime extends  FunSuite{

  val meterRecordTestCase = new CalibrationDataHelper
  val list_meter_record_cassandra = meterRecordTestCase.getCalibrationForPatientByDisplayTimeRecordsFromCassandra
  val list_meter_record_csv = meterRecordTestCase.getMeterRecordsFromCSV

  test("TC_334 --~> should verify System Time of the CalibrationForPatientByDisplayTime in cassandra is populating properly"){
    //verify the results
    //check for mapping of system time
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

  test("TC_336 --~> should verify Display Time of the CalibrationForPatientByDisplayTime in cassandra is populating properly"){

    //verify the results
    //check for mapping of display time

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

  test("TC_369 --~> should verify Entry Type of CalibrationForPatientByDisplayTime in cassandra is populating properly"){
    val list_entry_type = List("UserEntered","CommandCancelled","SentToTransmitterCalibrationSuccess","SentToTransmitterCalibrationError0",
      "SentToTransmitterCalibrationError1","SentToTransmitterCalibrationLinearityFitFailure","SentToTransmitterOutlierCalibrationFailure",
      "SentToTransmitterBGOutsideOf40to400Fail","SentToTransmitterSecondStartupBGRequired","SentToTransmitterBGOutsideOf40to400Pass",
      "SentToTransmitterOutlierCalibrationRequest","SentToTransmitterBGUnmatched","SentToTransmitterBGOutsideOf20to600",
      "SentToTransmitterNotInSession","SentToTransmitterBGTimestampInTheFuture","SentToTransmitterBGIsDuplicate","SentToTransmitterBGTimestampTooEarly",
      "SentToTransmitterBGTimestampEarlierThanSessionStartCommandReceived","SentToTransmitterBGNotInChronologicalOrder",
      "SentToTransmitterCalibrationAlreadyDoneWithOtherDevice"
    )

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
        if (index != -1) {
          if (list_meter_record_cassandra(index).Model === "G4") {
            assert(list_meter_record_cassandra(index).EntryType === "None")
          }
          else {
            assert(list_entry_type contains list_meter_record_cassandra(index).EntryType)
          }
        }
        else{
          fail(s"Record not found : $x")
        }
    }
  }

  test("TC_370 --~> should verify Model of the CalibrationForPatientByDisplayTime in cassandra is populating properly"){
    //verify the results
    list_meter_record_cassandra.foreach {
      x =>
        assert(x.Model !== null)
        assert(x.Model !== "")
        assert(x.Model.isInstanceOf[String])
    }
    list_meter_record_csv.foreach{
      x =>
        val index = meterRecordTestCase.getIndexForSystemTime(x,list_meter_record_cassandra)
        if(index != -1){
          assert(x.Model === list_meter_record_cassandra(index).Model)
          assert(list_meter_record_cassandra(index).Model ==="G4" ||
            list_meter_record_cassandra(index).Model === "G5")
        }
    }
  }

  test("TC_371 --~> should verify TransmitterId of the CalibrationForPatientByDisplayTime in cassandra is populating properly"){

    //verify the results
    //TODO
    //check for null values in transmitter id , how to deal when transmitter id is null. Transmitter id can be empty & null
    list_meter_record_cassandra.foreach {
      x =>
        assert(x.TransmitterId.isInstanceOf[String])
    }

    list_meter_record_csv.foreach {
      x =>
        val index = meterRecordTestCase.getIndexForSystemTime(x, list_meter_record_cassandra)
        if(index != -1) {
          assert(x.TransmitterId === list_meter_record_cassandra(index).TransmitterId)
          assert((5 to 6) contains list_meter_record_cassandra(index).TransmitterId.length)
        }else {
          fail(s"Record not found : $x")
        }
    }

  }

  test("TC_372 --~> should verify Units of the CalibrationForPatientByDisplayTime in cassandra is populating properly"){

    //verify the results
    list_meter_record_cassandra.foreach {
      x =>
        assert(x.Units !== null)
        assert(x.Units !== "")
        assert(x.Units.isInstanceOf[String])
    }

    list_meter_record_csv.foreach {
      x =>
        val index = meterRecordTestCase.getIndexForSystemTime(x, list_meter_record_cassandra)
        if(index != -1) {
          assert(x.Units === list_meter_record_cassandra(index).Units)
          assert(list_meter_record_cassandra(index).Units === "mg/dL")
        } else {
          fail(s"Record not found : $x")
        }
    }
  }

  test("TC_373 --~> should verify Value of the CalibrationForPatientByDisplayTime in cassandra is populating properly"){

    //verify the results
    list_meter_record_cassandra.foreach {
      x =>
        assert(x.Value !== null)
        assert(x.Value !== "")
        assert(x.Value.isInstanceOf[Int])
    }
    list_meter_record_csv.foreach {
      x=>
        val index = meterRecordTestCase.getIndexForSystemTime(x, list_meter_record_cassandra)
        if(index != -1){
          //          assert(x.Value === list_meter_record_cassandra(index).Value)
          //          assert((20 to 600 by 1) contains list_meter_record_cassandra(index).Value)
        }
        else{
          fail(s"Record not found : $x")
        }
    }
  }

  test("TC_388 --~> should verify IngestionTimestamp of the CalibrationForPatientByDisplayTime in cassandra is populating properly"){

    //verify the results
    list_meter_record_cassandra.foreach {
      x=>
        assert(x.IngestionTimestamp !==null)
        assert(x.IngestionTimestamp !=="")
        assert(x.IngestionTimestamp.isInstanceOf[Date])
    }
    list_meter_record_csv.foreach{
      x =>
        val index = meterRecordTestCase.getIndexForSystemTime(x,list_meter_record_cassandra)
        if(index != -1){
          assert(x.SystemTime === list_meter_record_cassandra(index).IngestionTimestamp) //check for the mapping of IngestionTimestamp,
          // if it is mapped to System time or not
        }
        else{
          fail(s"Record not found : $x")
        }

    }
  }

  test("TC_390 --~> should verify PatientId of the CalibrationForPatientByDisplayTime in cassandra is populating properly") {

    //verify the results
    list_meter_record_cassandra.foreach {
      x =>
        assert(x.PatientId !== null)
        assert(x.PatientId !== "")
        assert(x.PatientId.isInstanceOf[UUID])
    }

    list_meter_record_csv.foreach {
      x =>
        val index = meterRecordTestCase.getIndexForSystemTime(x, list_meter_record_cassandra)
        if(index != -1) {
          assert(x.PatientId === list_meter_record_cassandra(index).PatientId)
        } else{
          fail(s"Record not found : $x")
        }
    }
  }

}
