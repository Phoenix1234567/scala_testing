package com.dexcom.dto

import java.util.Date


/**
  * Created by gaurav.garg on 23-01-2017.
  */
case class AlertSetting(
                         name: String,
                         value: Int,
                         system_time: Date,
                         display_time: Date,
                         units: String,
                         delay: Int,
                         snooze: Int,
                         enabled: Boolean
                       )
