package com.dexcom.dto

import java.util.UUID

/**
  * Created by gaurav.garg on 05-01-2017.
  */
case class Patient(
                    PatientId: UUID,
                    SourceStream: String,
                    SequenceNumber: String,
                    TransmitterNumber: String,
                    ReceiverNumber: Option[String],
                    Tag: String
                  )
