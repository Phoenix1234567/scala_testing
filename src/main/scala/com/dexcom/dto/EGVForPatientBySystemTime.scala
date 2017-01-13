package com.dexcom.dto

import java.util.{Date, UUID}

/**
  * Created by gaurav.garg on 05-01-2017.
  */
case class EGVForPatientBySystemTime (
                                       PatientId : UUID,
                                       SystemTime : Date,
                                       PostId : UUID,
                                       DisplayTime : Date,
                                       IngestionTimestamp : Date,
                                       RateUnits : String,
                                       Source : String,
                                       Status : String,
                                       TransmitterId : String,
                                       TransmitterTicks : Long,
                                       Trend : String,
                                       TrendRate : Double,
                                       Units : String,
                                       Value : Int
                                     )
