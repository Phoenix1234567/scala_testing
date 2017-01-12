package com.dexcom.dto

/**
  * Created by gaurav.garg on 05-01-2017.
  */
case class GlucoseRecord(
                          RecordedSystemTime: String,
                          RecordedDisplayTime: String,
                          TransmitterId: String,
                          TransmitterTime: String,
                          GlucoseSystemTime: String,
                          GlucoseDisplayTime: String,
                          Value: String,
                          Status: String,
                          TrendArrow: String,
                          TrendRate: String,
                          IsBackfilled: String,
                          InternalStatus: String
                        )
