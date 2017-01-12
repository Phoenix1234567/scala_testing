package com.dexcom.dto

/**
  * Created by gaurav.garg on 05-01-2017.
  */
case class EGVForPatientBySystemTime (
                                     PatientId : String,
                                     SystemTime : String,
                                     PostId : String,
                                     DisplayTime : String,
                                     IngestionTimestamp : String,
                                     RateUnits : String,
                                     Source : String,
                                     Status : String,
                                     TransmitterId : String,
                                     TransmitterTicks : String,
                                     Trend : String,
                                     TrendRate : String,
                                     Units : String,
                                     Value : String
                                     )
