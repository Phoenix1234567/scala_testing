package com.dexcom.dto

import java.util.{Date, UUID}

import scala.beans.BeanProperty

/**
  * Created by gaurav.garg on 05-01-2017.
  */
case class GlucoseRecord(
                          RecordedSystemTime: Date,
                          RecordedDisplayTime: Date,
                          TransmitterId: String,
                          TransmitterTime: Long,
                          GlucoseSystemTime: Date,
                          GlucoseDisplayTime: Date,
                          Value: Int,
                          Status: String,
                          TrendArrow: String,
                          TrendRate: Double,
                          IsBackfilled: Boolean,
                          InternalStatus: String
                        )
