package com.dexcom.dto

import java.util.{Date, UUID}

import scala.beans.BeanProperty

/**
  * Created by sarvaraj on 17/01/17.
  */
  class UserEventR {

  @BeanProperty var PatientID: UUID = null
  @BeanProperty var DisplayTime: Date = null
  @BeanProperty var Name: String = ""
  @BeanProperty var Model: String = ""
  @BeanProperty var IngestionTimestamp: Date = null
  @BeanProperty var PostID: UUID = null
  @BeanProperty var SubType: String = ""
  @BeanProperty var SystemTimes: Date =null
  @BeanProperty var Units: String = ""
  @BeanProperty var Value: String = ""

  override def toString: String = {
    return ( ": "+PatientID+" , "+ DisplayTime+" , "+ Name+" , "+Model+" , "+IngestionTimestamp+" , "+PostID+" , "+SubType+" , "+SystemTimes+" , "+Units+" , "+Value)
  }



}
