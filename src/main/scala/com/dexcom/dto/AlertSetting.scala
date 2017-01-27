package com.dexcom.dto


/**
  * Created by gaurav.garg on 23-01-2017.
  */
case class AlertSetting (
                          name : String,
                          value : Int,
                          system_time : String,
                          display_time : String,
                          units : String,
                          delay : Int,
                          snooze : Int,
                          enabled : Boolean
                        )
