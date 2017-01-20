package com.dexcom.utils

import java.text.ParseException
import java.util.{Date, UUID}

import com.dexcom.common.Constants
import com.dexcom.dto.{Patient, Post}
import org.joda.time.format.DateTimeFormat
import com.dexcom.configuration.DexVictoriaConfigurations

import scala.collection.immutable.NumericRange
import scala.collection.mutable.ListBuffer

/**
  * Created by sarvaraj on 13/01/17.
  */
object Utils extends DexVictoriaConfigurations {

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

  /**
    * this method used to format the double upto one decimal place
    * @param x is the numeric range of doubles to be formatted
    * @return the list of doubles formattted upto one decimal place
    */
  def doubleFormatting(x : NumericRange[Double]): List[Double] = {
    var list = new ListBuffer[Double]
    x.foreach {
      list += new java.math.BigDecimal(_).setScale(1, BigDecimal.RoundingMode.HALF_UP).toDouble
    }
    list.toList

  }
  /**
    * Fetch patientid, source, etc from Patient.csv file
    *
    * @return list of the object patient objects
    */
  def patientRecords() : List[Patient] = {
    val list_patient_record = new ListBuffer[Patient]
    val patient_record_csv = scala.io.Source.fromFile(patient_records_path)
    for(line <- patient_record_csv.getLines().drop(1)) {
      val cols = line.split(Constants.Splitter).map(_.trim)
      val patient_record = Patient (
        PatientId = UUID.fromString(cols(0)),
        SourceStream = cols(1),
        SequenceNumber = cols(2),
        TransmitterNumber = cols(3),
        ReceiverNumber = cols(4),
        Tag = cols(5)
      )
      list_patient_record += patient_record
    }
    patient_record_csv.close()
    list_patient_record.toList

  }

  /**
    * Fetch postid, postedTimestamp from postIds.csv
    *
    * @return list of post object
    */
  def postRecords() : Post = {
    var post_record: Post = null
    val post_record_csv = scala.io.Source.fromFile(post_path)
    for (line <- post_record_csv.getLines().drop(1)) {
      val cols = line.split(Constants.Splitter).map(_.trim)
      post_record = Post(
        PostId = UUID.fromString(cols(0)),
        PostedTimestamp = cols(1)
      )

    }
    post_record_csv.close()
    post_record
  }
}
