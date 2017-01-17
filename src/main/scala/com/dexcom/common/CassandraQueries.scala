package com.dexcom.common

/**
  * Created by gaurav.garg on 09-01-2017.
  */
trait CassandraQueries {

  // query for fetching data from egv_for_patient_by_system_time
  lazy val GET_EGV_FOR_PATIENT_BY_SYSTEM_TIME = "SELECT * FROM gg_test.egv_for_patient_by_system_time"

  lazy val GET_DEVICE_SUMMARY = "SELECT * FROM gg_test.device_summary"

  lazy val GET_EVENT_FOR_PATIENT_BY_SYSTEM_TIME ="SELECT * FROM gg_test.event_for_patient_by_system_time"

  // query for fetching data from egv_for_patient_by_display_time
  lazy val GET_EGV_FOR_PATIENT_BY_DISPLAY_TIME = "SELECT * FROM gg_test.egv_for_patient_by_display_time"


}
