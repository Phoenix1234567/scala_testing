package com.dexcom.dto

import java.util.Date

/**
  * Created by sarvaraj on 16/01/17.
  */
case class MeterRecord (
  RecordedSystemTime :Date,
  RecordedDisplayTime : Date,
  TransmitterId : String,
  TransmitterTime :Date,
  MeterSystemTime :Date,
  MeterDisplayTime : Date,
  Value : String,
  EntryType : String


)
