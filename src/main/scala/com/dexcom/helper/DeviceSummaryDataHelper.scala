package com.dexcom.helper

import java.util.Date

import com.dexcom.common.Constants
import com.dexcom.dto.{DeviceSummary, GlucoseRecord}
import com.dexcom.utils.Utils
import com.dexcom.configuration.DexVictoriaConfigurations

import scala.collection.mutable.ListBuffer

/**
  * Created by sarvaraj on 16/01/17.
  */
class DeviceSummaryDataHelper extends DexVictoriaConfigurations {

  def GlucoseRecords() : List[GlucoseRecord] = {
    val list_device_summary_record = new ListBuffer[GlucoseRecord]

    val device_settings_record_csv = scala.io.Source.fromFile(device_settings_record_path)
    for (line <- device_settings_record_csv.getLines().drop(1)) {
      val cols = line.split(Constants.Splitter).map(_.trim)
     /* val device_setting_record = DeviceSummary(

      )
      list_device_summary_record += device_setting_record*/
    }
    device_settings_record_csv.close()

    list_device_summary_record.toList
  }

}
