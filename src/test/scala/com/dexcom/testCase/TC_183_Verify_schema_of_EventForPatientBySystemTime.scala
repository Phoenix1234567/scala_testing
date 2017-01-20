/*
package com.dexcom.testCase

import java.util.{Date, UUID}

import com.dexcom.testCase.GlucoseRecordTestCase
import org.scalatest.FunSuite

/**
  * Created by gaurav.garg on 10-01-2017.
  */
class TC_183_Verify_schema_of_EventForPatientBySystemTime extends FunSuite {

  //initiate
  val eventRecordTestCase = new GlucoseRecordTestCase
  val list_event_record_cassandra = eventRecordTestCase.EventRecordsDestination()
 //println("cassandra----"+list_event_record_cassandra)
  //val list_glucose_record_csv = glucoseRecordTestCase.EGVRecordsSource()
 //println("csv----"+list_glucose_record_csv)

  test("should test Datatype of PatientId of the EventForPatientBySystemTime in cassandra") {

    //verify the results
    list_event_record_cassandra.foreach {
      x =>
        assert(x.PatientId !== null)
        assert(x.PatientId !== "")
        assert(x.PatientId.isInstanceOf[UUID])
    }

     }
  test("should test Datatype of SystemTime of the EventForPatientBySystemTime in cassandra") {

    //verify the results
    list_event_record_cassandra.foreach {
      x =>
        assert(x.SystemTime !== null)
        assert(x.SystemTime !== "")
        assert(x.SystemTime.isInstanceOf[Date])
    }
  }
  test("should test Datatype of name of the EventForPatientBySystemTime in cassandra") {

    //verify the results
    list_event_record_cassandra.foreach {
      x =>
        assert(x.name !== null)
        assert(x.name !== "")
        assert(x.name.isInstanceOf[String])
        assert(x.name === "Carbs" || x.name === "Insulin" || x.name === "Health" || x.name === "Exercise")
    }
  }
  test("should test Datatype of model of the EventForPatientBySystemTime in cassandra") {

    //verify the results
    list_event_record_cassandra.foreach {
      x =>
        assert(x.model !== null)
        assert(x.model !== "")
        assert(x.model.isInstanceOf[String])
        assert(x.model === "G5" || x.model === "G4")
    }
  }
  test("should test Datatype of DisplayTime of the EventForPatientBySystemTime in cassandra") {

    //verify the results
    list_event_record_cassandra.foreach {
      x =>
        assert(x.DisplayTime !== null)
        assert(x.DisplayTime !== "")
        assert(x.DisplayTime.isInstanceOf[Date])
    }
  }
  test("should test Datatype of IngestionTimestamp of the EventForPatientBySystemTime in cassandra") {

    //verify the results
    list_event_record_cassandra.foreach {
      x =>
        assert(x.IngestionTimestamp !== null)
        assert(x.IngestionTimestamp !== "")
        assert(x.IngestionTimestamp.isInstanceOf[Date])
    }
  }
  test("should test Datatype of PostId of the EventForPatientBySystemTime in cassandra") {

    //verify the results
    list_event_record_cassandra.foreach {
      x =>
        assert(x.PostId !== null)
        assert(x.PostId !== "")
        assert(x.PostId.isInstanceOf[UUID])
    }
  }
  test("should test Datatype of subtype of the EventForPatientBySystemTime in cassandra") {

    //verify the results
    list_event_record_cassandra.foreach {
      x => {
        assert(x.subtype !== null)
        assert(x.subtype !== "")
        assert(x.subtype.isInstanceOf[String])
      }

    }
  }
  test("should test Datatype of unit of the EventForPatientBySystemTime in cassandra") {

    //verify the results
    list_event_record_cassandra.foreach {
      x =>
        assert(x.unit !== null)
        assert(x.unit !== "")
        assert(x.unit.isInstanceOf[String])
    }
  }
  test("should test Datatype of Value of the EventForPatientBySystemTime in cassandra") {

    //verify the results
    list_event_record_cassandra.foreach {
      x =>
        assert(x.Value !== null)
        assert(x.Value !== "")
        assert(x.Value.isInstanceOf[String])
    }
  }

}

*/
