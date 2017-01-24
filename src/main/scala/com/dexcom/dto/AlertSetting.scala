package com.dexcom.dto

import java.util.Date

/**
  * Created by gaurav.garg on 23-01-2017.
  */
case class AlertSetting (
                          Name : String,
                          Value : Int,
                          SystemTime : Date,
                          DisplayTime : Date,
                          Units : String,
                          Delay : Int,
                          Snooze : Int,
                          Enabled : Boolean
                        )
