package com.dexcom.dto

/**
  * Created by gaurav.garg on 05-01-2017.
  */
case class Patient (
                     PatientId : String,
                     SourceStream : String,
                     SequenceNumber : String,
                     TransmitterNumber : String,
                     ReceiverNumber : String,
                     Tag : String
                   )
