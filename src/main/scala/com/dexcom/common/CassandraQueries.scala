package com.dexcom.common

/**
  * Created by gaurav.garg on 09-01-2017.
  */
trait CassandraQueries {

  // query for fetching data from egv_for_patient_by_system_time
  lazy val GET_EGV_FOR_PATIENT_BY_SYSTEM_TIME = "SELECT * FROM aditi_dgt.egv_for_patient_by_system_time"

  lazy val GET_DEVICE_SUMMARY = "SELECT * FROM aditi_dgt.device_summary"


}
