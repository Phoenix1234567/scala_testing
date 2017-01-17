/*
package com.dexcom.testCase

import java.util.UUID

import com.dexcom.helper.GlucoseDataHelper
import org.scalatest.FunSuite

/**
  * Created by gaurav.garg on 17-01-2017.
  */
class EGVForPatientByDisplayTime extends FunSuite {

  //initiate
  val glucoseRecordTestCase = new GlucoseDataHelper
  val list_glucose_record_cassandra = glucoseRecordTestCase.EGVForPatientByDisplayTime()
  val list_glucose_record_csv = glucoseRecordTestCase.EGVForPatient()

  test("TC_379 : should verify PatientId of the EGVForPatientByDisplayTime in cassandra is populating properly") {
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
}
*/
