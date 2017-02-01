
package com.dexcom.testCase

import java.util.{Date, UUID}
import com.dexcom.helper.DeviceUploadHelper
import org.scalatest.FunSuite

/**
  * Created by aditi.nandwana on 27-01-2017.
  */
class DeviceUploadForPatient extends FunSuite {

  //initiate
  val deviceUploadTestCase = new DeviceUploadHelper
  val list_device_upload_cassandra = deviceUploadTestCase.getDeviceUploadForPatientFromCassandra
  val list_device_upload_csv = deviceUploadTestCase.getDeviceUploadForPatientFromCSV.get
    println(s"CSV---$list_device_upload_csv ")
    println(s"-----Cassandra   $list_device_upload_cassandra ")
    test("TC_318 --~> should verify TransmitterId of the DeviceUploadForPatient in cassandra is populating properly") {
    //Verify the results
    list_device_upload_cassandra.foreach {
      x =>
        assert(x.TransmitterId !== null)
        assert(x.TransmitterId.isInstanceOf[String])
    }
    list_device_upload_csv.foreach {
      x =>
        val index = deviceUploadTestCase.getIndexForDeviceUpload(x, list_device_upload_cassandra)

        if (index != -1) {
          assert(x.TransmitterId === list_device_upload_cassandra(index).TransmitterId)
          assert((5 to 6) contains list_device_upload_cassandra(index).TransmitterId.length)
        }
        else
          fail(s"Record not found: $x")

    }
  }

    test("TC_319 --~> should verify Model of the DeviceUploadForPatient in cassandra is populating properly") {
      //verify the results
      list_device_upload_cassandra.foreach {
        x =>
          assert(x.Model !== null)
          assert(x.Model !== "")
          assert(x.Model.isInstanceOf[String])
      }
      list_device_upload_csv.foreach {

        x =>
          val index = deviceUploadTestCase.getIndexForDeviceUpload(x, list_device_upload_cassandra)
          if(index != -1) {
            assert(x.Model === list_device_upload_cassandra(index).Model)
            assert(list_device_upload_cassandra(index).Model === "G5" || list_device_upload_cassandra(index).Model === "G4")
          }
          else
            fail(s"Record not found: $x")

      }
    }

    test("TC_320 --~> should verify isMmolDisplayMode of the DeviceUploadForPatient in cassandra is populating properly") {
      //verify the results
      list_device_upload_cassandra.foreach {
        x =>
          assert(x.IsMmolDisplayMode !== null)
          assert(x.IsMmolDisplayMode !== "")
          assert(x.IsMmolDisplayMode.isInstanceOf[Boolean])
      }
      list_device_upload_csv.foreach {
        x =>
          val index = deviceUploadTestCase.getIndexForDeviceUpload(x, list_device_upload_cassandra)
          if(index != -1)
          assert(x.IsMmolDisplayMode === list_device_upload_cassandra(index).IsMmolDisplayMode)
          else
            fail(s"Record not found: $x")

      }
    }

    test("TC_321 --~> should verify isBlindedMode of the DeviceUploadForPatient in cassandra is populating properly") {
      //verify the results
      list_device_upload_cassandra.foreach {
        x =>
          assert(x.IsBlindedMode !== null)
          assert(x.IsBlindedMode !== "")
          assert(x.IsBlindedMode.isInstanceOf[Boolean])
      }
      list_device_upload_csv.foreach {
        x =>
          val index = deviceUploadTestCase.getIndexForDeviceUpload(x, list_device_upload_cassandra)
          if(index != -1)
          assert(x.IsBlindedMode === list_device_upload_cassandra(index).IsBlindedMode)
          else
            fail(s"Record not found: $x")

      }
    }

    test("TC_322 --~> should verify is24HourMode of the DeviceUploadForPatient in cassandra is populating properly") {
      //verify the results
      list_device_upload_cassandra.foreach {
        x =>
          assert(x.Is24HourMode !== null)
          assert(x.Is24HourMode !== "")
          assert(x.Is24HourMode.isInstanceOf[Boolean])
      }
      list_device_upload_csv.foreach {
        x =>
          val index = deviceUploadTestCase.getIndexForDeviceUpload(x, list_device_upload_cassandra)
          if(index != -1)
          assert(x.Is24HourMode === list_device_upload_cassandra(index).Is24HourMode)
          else
            fail(s"Record not found: $x")

      }
    }

    test("TC_323 --~> should verify Language of the DeviceUploadForPatient in cassandra is populating properly") {
      //verify the results
      list_device_upload_cassandra.foreach {
        x =>
          assert(x.Language !== null)
          assert(x.Language !== "")
          assert(x.Language.isInstanceOf[String])
      }
      list_device_upload_csv.foreach {
        x =>
          val index = deviceUploadTestCase.getIndexForDeviceUpload(x, list_device_upload_cassandra)
          if(index != -1)
          assert(x.Language === list_device_upload_cassandra(index).Language)
          else
            fail(s"Record not found: $x")

      }
    }

    test("TC_324 --~> should verify SoftwareVersion of the DeviceUploadForPatient in cassandra is populating properly") {
      //verify the results
      list_device_upload_cassandra.foreach {
        x =>
          assert(x.SoftwareVersion !== null)
          assert(x.SoftwareVersion !== "")
          assert(x.SoftwareVersion.isInstanceOf[String])
      }
      list_device_upload_csv.foreach {
        x =>
          val index = deviceUploadTestCase.getIndexForDeviceUpload(x, list_device_upload_cassandra)
          if(index != -1)
          assert(x.SoftwareVersion === list_device_upload_cassandra(index).SoftwareVersion)
          else
            fail(s"Record not found: $x")

      }
    }

    test("TC_325 --~> should verify DisplayTimeOffset of the DeviceUploadForPatient in cassandra is populating properly") {
      //verify the results
      list_device_upload_cassandra.foreach {
        x =>
          assert(x.DisplayTimeOffset !== null)
          assert(x.DisplayTimeOffset.isInstanceOf[Int])
      }
      list_device_upload_csv.foreach {
        x =>
          val index = deviceUploadTestCase.getIndexForDeviceUpload(x, list_device_upload_cassandra)
          if(index != -1)
          assert(x.DisplayTimeOffset === list_device_upload_cassandra(index).DisplayTimeOffset)
          else
            fail(s"Record not found: $x")

      }
    }

    test("TC_326 --~> should verify SystemTimeOffset of the DeviceUploadForPatient in cassandra is populating properly") {
      //verify the results
      list_device_upload_cassandra.foreach {
        x =>
          assert(x.SystemTimeOffset !== null)
          assert(x.SystemTimeOffset.isInstanceOf[Int])
      }
      list_device_upload_csv.foreach {
        x =>
          val index = deviceUploadTestCase.getIndexForDeviceUpload(x, list_device_upload_cassandra)
          if(index != -1)
          assert(x.SystemTimeOffset === list_device_upload_cassandra(index).SystemTimeOffset)
          else
            fail(s"Record not found: $x")
      }
    }

    test("TC_331 --~> should verify SoftwareNumber of the DeviceUploadForPatient in cassandra is populating properly") {
      //verify the results
      list_device_upload_cassandra.foreach {
        x =>
          assert(x.SoftwareNumber !== null)
          assert(x.SoftwareNumber.isInstanceOf[String])
      }
      list_device_upload_csv.foreach {
        x =>
          val index = deviceUploadTestCase.getIndexForDeviceUpload(x, list_device_upload_cassandra)
          if(index != -1)
          assert(x.SoftwareNumber === list_device_upload_cassandra(index).SoftwareNumber)
          else
            fail(s"Record not found: $x")

      }
    }

    test("TC_332 --~> should verify SerialNumber of the DeviceUploadForPatient in cassandra is populating properly") {
      //verify the results
      list_device_upload_cassandra.foreach {
        x =>
          assert(x.SerialNumber !== null)
          assert(x.SerialNumber.isInstanceOf[String])
      }
      list_device_upload_csv.foreach {
        x =>
          val index = deviceUploadTestCase.getIndexForDeviceUpload(x, list_device_upload_cassandra)
          if(index != -1)
          assert(x.SerialNumber === list_device_upload_cassandra(index).SerialNumber)
          else
            fail(s"Record not found: $x")

      }
    }

    test("TC_395 --~> should verify PatientId of the DeviceUploadForPatient in cassandra is populating properly") {
      //verify the results
      list_device_upload_cassandra.foreach {
        x =>
          assert(x.PatientId !== null)
          assert(x.PatientId !== "")
          assert(x.PatientId.isInstanceOf[UUID])
      }
      list_device_upload_csv.foreach {
        x =>
          val index = deviceUploadTestCase.getIndexForDeviceUpload(x, list_device_upload_cassandra)
          if(index != -1)
          assert(x.PatientId === list_device_upload_cassandra(index).PatientId)
          else
            fail(s"Record not found: $x")
      }
    }

    //DeviceUploadDate - PK
    test("TC_396 --~> should verify DeviceUploadDate of the DeviceUploadForPatient in cassandra is populating properly") {
      //verify the results //need to check the mapping of device_upload_date from code
      list_device_upload_cassandra.foreach {
        x =>
          assert(x.DeviceUploadDate !== null)
          assert(x.DeviceUploadDate !== "")
          assert(x.DeviceUploadDate.isInstanceOf[Date])
      }
      list_device_upload_csv.foreach {
        x =>
          val index = deviceUploadTestCase.getIndexForDeviceUpload(x, list_device_upload_cassandra)
          if(index != -1)
          assert(x.DeviceUploadDate === list_device_upload_cassandra(index).DeviceUploadDate)
          else
            fail(s"Record not found: $x")
      }

    }

    test("TC_397 --~> should verify IngestionTimestamp of the DeviceUploadForPatient in cassandra is populating properly"){
      //verify the results
      list_device_upload_cassandra.foreach{
        x=>
          assert(x.IngestionTimestamp !== null)
          assert(x.IngestionTimestamp !== "")
          assert(x.IngestionTimestamp.isInstanceOf[Date])
      }
      list_device_upload_csv.foreach{
        x=>
          val index = deviceUploadTestCase.getIndexForDeviceUpload(x,list_device_upload_cassandra)
          if(index != -1)
          assert(x.IngestionTimestamp === list_device_upload_cassandra(index).IngestionTimestamp)
          else
            fail(s"Record not found: $x")

      }
    }

    test("TC_398 --~> should verify Udi of the DeviceUploadForPatient in cassandra is populating properly") {
      //verify the results
      list_device_upload_cassandra.foreach {
        x =>
          assert(x.Udi.get == null)
         // assert(x.Udi.isInstanceOf[String])
      }
    }

    /*test("TC_399 --~> should verify alerts of the DeviceUploadForPatient in cassandra is populating properly"){
      for( device_records <- list_device_upload_csv) {
        for(alerts_record <- device_records.Alerts) {
          //verify name in alerts column
          assert(alerts_record.name !== null && alerts_record.name != "" && alerts_record.name.isInstanceOf[String])
        }
      }
    }*/


}
