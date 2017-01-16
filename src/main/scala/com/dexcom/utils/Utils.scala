package com.dexcom.utils

import java.text.ParseException
import java.util.Date

import org.joda.time.format.DateTimeFormat

/**
  * Created by sarvaraj on 13/01/17.
  */
object Utils{

  def stringToDate(dateString : String) : Either[Unit, Date]= {

    val df = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ")

    try {
      val temp = df.withOffsetParsed().parseDateTime(dateString)
      val date = temp.toDate
      Right(date)
    } catch {
      case e : ParseException =>
        Left(e.printStackTrace())
    }
  }
}
