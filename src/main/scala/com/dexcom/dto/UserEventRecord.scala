package com.dexcom.dto

import java.util.{Date, UUID}

import scala.beans.BeanProperty

/**
  * Created by sarvaraj on 17/01/17.
  */
case class UserEventRecord(
                            @BeanProperty PatientID: UUID,
                            DisplayTime: Date,
                            Name: String,
                            Model: String,
                            IngestionTimestamp: Date,
                            PostId: UUID,
                            SubType: String,
                            SystemTime: Date,
                            Units: String,
                            Value: String
                          )


