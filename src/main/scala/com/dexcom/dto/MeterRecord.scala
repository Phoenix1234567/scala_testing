package com.dexcom.dto

import java.util.{Date, UUID}

/**
  * Created by sarvaraj on 16/01/17.
  */
case class MeterRecord(
                        PatientId: UUID,
                        //PostId : UUID,
                        SystemTime: Date,
                        DisplayTime: Date,
                        TransmitterId: String,
                        IngestionTimestamp: Date,
                        Units: String,
                        Value: Int,
                        EntryType: String,
                        Model: String


                      )
