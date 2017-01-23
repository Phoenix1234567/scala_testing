package com.dexcom.common

import com.dexcom.configuration.DexVictoriaConfigurations

/**
  * Created by gaurav.garg on 09-01-2017.
  */
trait CassandraQueries extends DexVictoriaConfigurations {

  // query for fetching data from egv_for_patient_by_system_time
  lazy val GET_EGV_FOR_PATIENT_BY_SYSTEM_TIME = s"SELECT * FROM $keySpaceName.egv_for_patient_by_system_time"

  lazy val GET_DEVICE_SUMMARY = s"SELECT * FROM $keySpaceName.device_summary"

  lazy val GET_EVENT_FOR_PATIENT_BY_SYSTEM_TIME = s"SELECT * FROM $keySpaceName.event_for_patient_by_system_time"

  // query for fetching data from egv_for_patient_by_display_time
  lazy val GET_EGV_FOR_PATIENT_BY_DISPLAY_TIME = s"SELECT * FROM $keySpaceName.egv_for_patient_by_display_time"

  lazy val GET_CALIBRATION_FOR_PATIENT_BY_SYSTEM_TIME = s"SELECT * FROM $keySpaceName.calibration_for_patient_by_system_time"

  lazy val GET_CALIBRATION_FOR_PATIENT_BY_DISPLAY_TIME = s"SELECT * FROM $keySpaceName.calibration_for_patient_by_display_time"
}
