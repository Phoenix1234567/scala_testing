package com.dexcom.dto

import java.util.{Date, UUID}

import org.joda.time.DateTime

/**
  * Created by gaurav.garg on 23-01-2017.
  */
case class DeviceUploadForPatient(
                                   PatientId: UUID,
                                   Model: String,
                                   DeviceUploadDate: DateTime,
                                   Alerts: List[AlertSetting],
                                   DisplayTimeOffset: Int,
                                   IngestionTimestamp: Date,
                                   Is24HourMode: Boolean,
                                   IsBlindedMode: Boolean,
                                   IsMmolDisplayMode: Boolean,
                                   Language: String,
                                   SerialNumber: String,
                                   SoftwareNumber: String,
                                   SoftwareVersion: String,
                                   SystemTimeOffset: Int,
                                   TransmitterId: String,
                                   Udi: Option[String],
                                   RecordedSystemTime: Option[Date]
                                 )
