package com.dexcom.dto

import java.util.{Date, UUID}

/**
  * Created by gaurav.garg on 05-01-2017.
  */
case class Post(
                 PostId: UUID,
                 PostedTimestamp: Date
               )