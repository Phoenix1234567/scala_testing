package com.dexcom.utils

import java.text.ParseException
import java.util.{Date, UUID}

import com.dexcom.common.Constants._
import com.dexcom.configuration.DexVictoriaConfigurations
import com.dexcom.dto.{DeviceUploadForPatient, Patient, Post}
import com.dexcom.helper.{DeviceUploadHelper, GlucoseDataHelper}
import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, DateTimeZone}

import scala.collection.immutable.NumericRange
import scala.collection.mutable.ListBuffer

/**
  * Created by sarvaraj on 13/01/17.
  */
object Utils extends DexVictoriaConfigurations {

  val glucoseDataHelper = new GlucoseDataHelper

  def uploadDate(postId: UUID): DateTime =
    glucoseDataHelper.getGlucoseRecordsFromCSV match {
      case Some(data) =>
        new DateTime(data.filter(_.PostId == postId).map(_.SystemTime).min).toDateTime(DateTimeZone.UTC)
      case _ => new DateTime(this.postRecords.filter(_.PostId == postId).head.PostedTimestamp).toDateTime(DateTimeZone.UTC)
    }

  /**
    * This method parsed date
    * @param dateString date to be parsed
    * @return date in Date format
    */
  def stringToDate(dateString: String): Option[Date] = {

    val df = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ")

    try {
      val temp = df.withOffsetParsed().parseDateTime(dateString)
      val date = temp.toDate
      Some(date)
    } catch {
      case e: ParseException =>
        e.printStackTrace()
        None
    }
  }

  /**
    * this method parsed date for Device_upload_date
    * @param dateString date to be parsed
    * @return date in Date format
    */
  def formatDate(dateString: String): String = {
    val df = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    val new_df = DateTimeFormat.forPattern("yyyy-MM-dd")
    val parsedDate = df.parseLocalDate(dateString)
    new_df.print(parsedDate)
  }

  /**
    * this method used to format the double upto one decimal place
    *
    * @param x is the numeric range of doubles to be formatted
    * @return the list of doubles formattted upto one decimal place
    */
  def doubleFormatting(x: NumericRange[Double]): List[Double] = {
    var list = new ListBuffer[Double]
    x.foreach {
      list += new java.math.BigDecimal(_).setScale(1, BigDecimal.RoundingMode.HALF_UP).toDouble
    }
    list.toList

  }

  val deviceUploadHelper = new DeviceUploadHelper
  val latestDeviceSettings: Option[DeviceUploadForPatient] =
    deviceUploadHelper.getDeviceUploadForPatientFromCSV match {
      case Some(data) => data.sortBy(_.RecordedSystemTime.get).lastOption
      case _ => None
    }

  /**
    * This method is for Units ,RateUnits of Glucose and calibration
    * @return displayMode
    */
  def displayMode: String = {
    latestDeviceSettings match {
      case Some(data) =>
        if (data.IsMmolDisplayMode) "mmol/L" else "mg/dL"
      case _ => "mg/dL"
    } //.fold("mg/dL")(rec => if (rec.IsMmolDisplayMode) "mmol/L" else "mg/dL")
  }

  /**
    * This method for other tables
    * @return device model
    */
  def deviceModel: String = {
    latestDeviceSettings match {
      case None => G5
      case Some(setting) =>
        selectDeviceModel(setting.SoftwareNumber, setting.SoftwareVersion)
    }
  }

  /**
    * This method return device_model fro Device_upload_for_patient
    * @param softwareNumber of device_setting_records
    * @param softwareVersion of device_setting_records
    * @return  Device model
    */
  def selectDeviceModel(softwareNumber: String, softwareVersion: String): String = {
    //  In the Device Settings Record, there is a field (VersionNumber) that is a string value that corresponds to the
    //  software version of the receiver.
    //  4.0.1.x = G5
    //  3.0.1.x = Share Direct (G4 + BLE radio)
    //  2.0.1.x = G4

    // iPhone app, and android US and 2 different OUS apps, respectively
    val mobileSoftwareNumbers = Seq("SW10611", "SW10940", "SW11170", "SW11171")

    if (mobileSoftwareNumbers.contains(softwareNumber))
      G5
    else {
      softwareVersion match {
        case g5 if g5.startsWith("4.0.1") => G5
        case diasendModel if diasendModel.startsWith("1.1.") => G5 // Diasend upload
        case sd if sd.startsWith("3.0.1") => ShareDirect
        case g4 if g4.startsWith("2.0.1") => G4
        case _ => DefaultDeviceModel
      }
    }
  }

  /**
    * Fetch patientid, source, etc from Patient.csv file
    *
    * @return list of the object patient objects
    */
  def patientRecords(): List[Patient] = {
    val list_patient_record = new ListBuffer[Patient]
    val patient_record_csv = scala.io.Source.fromFile(patient_records_path)
    for (line <- patient_record_csv.getLines().drop(1)) {
      val cols = line.split(Splitter).map(_.trim)
      val patient_record = Patient(
        PatientId = UUID.fromString(cols(0)),
        SourceStream = cols(1),
        SequenceNumber = cols(2),
        TransmitterNumber = cols(3),
        ReceiverNumber = Some(cols(4)),
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
  def postRecords: List[Post] = {
    var list_post_record: List[Post] = Nil
    val post_record_csv = scala.io.Source.fromFile(post_path)
    for (line <- post_record_csv.getLines().drop(1)) {
      val cols = line.split(Splitter).map(_.trim)
      val post_record = Post(
        PostId = UUID.fromString(cols(0)),
        PostedTimestamp = stringToDate(cols(1)).get
      )
      list_post_record = post_record :: list_post_record
    }
    post_record_csv.close()
    list_post_record
  }

  val serialNumber: String = {
    this.patientRecords().head.SourceStream match {
      case "Receiver" => this.patientRecords().head.ReceiverNumber.getOrElse("???") //For one patient record only
      case "Phone" => "iPhone"
      case "Phone2" => "Android"
      case _ => "Unknown source"
    }
  }
}
