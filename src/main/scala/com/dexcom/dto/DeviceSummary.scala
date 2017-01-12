package com.dexcom.dto

import java.util.{UUID,Date}



/**
  * Created by sarvaraj on 11/01/17.
  */
case class DeviceSummary(
                          PatientId: UUID,
                          Model: String,
                          SerialNumber: String,
                          CreateDate: Date,
                          LastUpdateDate: Date
                        )


