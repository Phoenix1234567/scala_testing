package com.dexcom.dto

import java.util.{Date, UUID}

/**
  * Created by aditi.nandwana on 13-01-2017.
  */
case class EventForPatientBySystemTime (
                                         PatientId : UUID,
                                         SystemTime : Date,
                                         name : String,
                                         model : String,
                                         DisplayTime : Date,
                                         IngestionTimestamp : Date,
                                         PostId : UUID,
                                         subtype : String,
                                         unit : String,
                                         Value : String
                                       )
//PK - patient id, system time,name,model