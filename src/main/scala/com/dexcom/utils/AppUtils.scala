package com.dexcom.utils

import java.text.{ParseException, SimpleDateFormat}
import java.util.Date

/**
  * Created by sarvaraj on 13/01/17.
  */
object AppUtils {


  def stringToDate(dateString : String) : Either[Unit, Date]= {
    val df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSX")  //2014-05-16T20:06:17.1592279Z
    val dfNew = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy")
    try {
      val date = df.parse(dateString)
      val dateNew = dfNew.parse(dfNew.format(date))
      Right(dateNew)
    } catch {
      case e : ParseException =>
        Left(e.printStackTrace())
    }
  }
}
