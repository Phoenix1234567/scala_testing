package com.dexcom.testCase

import java.util
import java.util.{Date, UUID}

import com.dexcom.common.CassandraQueries
import com.dexcom.connection.CassandraConnection
import com.dexcom.dto.UserEventRecord
import com.dexcom.helper.UserEventDataHelper
import org.scalatest.{BeforeAndAfterAll, FunSuite}

/**
  * Created by sarvaraj on 17/01/17.
  */
class EventForPatientByDisplayTime extends FunSuite with CassandraQueries with BeforeAndAfterAll {
  var recordsFromCassandra: List[UserEventRecord] = null
  var recordsFromCSV: List[UserEventRecord] = null
  var cassandraConnection: CassandraConnection = null
  var nameAndSubtypeMapping: util.HashMap[String, List[String]] = null
  var userEventDataHelper: UserEventDataHelper = null

  override def beforeAll() = {
    cassandraConnection = new CassandraConnection()
    userEventDataHelper = new UserEventDataHelper(cassandraConnection.getConnection)
    recordsFromCassandra = userEventDataHelper.getRecordFromCassandra()
    recordsFromCSV = userEventDataHelper.getRecordsFromCSV()
    initNameAndSubtypeMapping

  }

  def initNameAndSubtypeMapping: util.HashMap[String, List[String]] = {
    nameAndSubtypeMapping = new util.HashMap[String, List[String]]()
    nameAndSubtypeMapping.put("Health", List("Illness", "Stress", "HighSymptoms", "LowSymptoms", "Cycle", "Alcohol"))
    nameAndSubtypeMapping.put("Exercise", List("Light", "Medium", "Heavy"))
    nameAndSubtypeMapping.put("Carbs", List(""))
    nameAndSubtypeMapping.put("Insulin", List(""))
    nameAndSubtypeMapping
  }


  test("TC_392 --~> should verify PatientId of the EventForPatientByDisplayTime in cassandra is populating properly") {

    //verify the results
    recordsFromCassandra.foreach {
      x =>
        assert(x.PatientID !== null)
        assert(x.PatientID !== "")
        assert(x.PatientID.isInstanceOf[UUID])
    }

    recordsFromCSV.foreach {
      x =>
        val index = recordsFromCassandra.indexWhere {
          y =>
            y.PatientID.equals(x.PatientID) &&
              y.DisplayTime.equals(x.DisplayTime) &&
              y.PostID.equals(x.PostID) &&
              y.Model.equals(x.Model)
        }
        assert(x.PatientID === recordsFromCassandra(index).PatientID)
    }
  }


  test("TC_337 --~> should verify Name of the EventForPatientByDisplayTime in cassandra is populating properly") {

    //verify the results
    recordsFromCassandra.foreach {
      x =>
        assert(x.Name !== null)
        assert(x.Name !== "")
        assert(x.Name.isInstanceOf[String])
    }

    recordsFromCSV.foreach {
      x =>
        val index = recordsFromCassandra.indexWhere {
          y =>
            y.PatientID.equals(x.PatientID) &&
              y.DisplayTime.equals(x.DisplayTime) &&
              y.Name.equals(x.Name) &&
              y.Model.equals(x.Model)
        }
        assert(x.Name === recordsFromCassandra(index).Name)
    }
  }

  test("TC_338 --~> should verify SubType of the EventForPatientByDisplayTime in cassandra is populating properly") {

    //verify the results
    recordsFromCassandra.foreach {
      x =>
        assert(x.SubType !== null)
        assert(x.SubType.isInstanceOf[String])
    }

    recordsFromCSV.foreach {
      x =>
        val index = recordsFromCassandra.indexWhere {
          y =>
            y.PatientID.equals(x.PatientID) &&
              y.DisplayTime.equals(x.DisplayTime) &&
              y.Name.equals(x.Name) &&
              y.Model.equals(x.Model)
        }
        assert(x.Name === recordsFromCassandra(index).Name && nameAndSubtypeMapping.get(x.Name).contains(recordsFromCassandra(index).SubType))
    }
  }

  test("TC_339 --~> should verify Value of the EventForPatientByDisplayTime in cassandra is populating properly") {

    //verify the results
    recordsFromCassandra.foreach {
      x =>
        assert(x.Value !== null)
        assert(x.Value.isInstanceOf[String])
    }

    recordsFromCSV.foreach {
      x =>
        val index = recordsFromCassandra.indexWhere {
          y =>
            y.PatientID.equals(x.PatientID) &&
              y.DisplayTime.equals(x.DisplayTime) &&
              y.Name.equals(x.Name) &&
              y.Model.equals(x.Model)
        }
        if (index != -1) {
          assert(x.Value === recordsFromCassandra(index).Value)
          if (x.Name === "Carbs" || x.Name === "Insulin")
            assert(recordsFromCassandra(index).Value !== "")
          else assert(recordsFromCassandra(index).Value === "")
        }
    }
  }

  test("TC_340 --~> should verify Unit of the EventForPatientByDisplayTime in cassandra is populating properly") {

    //verify the results
    recordsFromCassandra.foreach {
      x =>
        assert(x.Units !== null)
        assert(x.Units.isInstanceOf[String])
    }

    recordsFromCSV.foreach {
      x =>
        val index = recordsFromCassandra.indexWhere {
          y =>
            y.PatientID.equals(x.PatientID) &&
              y.DisplayTime.equals(x.DisplayTime) &&
              y.Name.equals(x.Name) &&
              y.Model.equals(x.Model)
        }
        assert(x.Units === recordsFromCassandra(index).Units)
        if (x.Name === "Carbs")
          assert(recordsFromCassandra(index).Units.equalsIgnoreCase("grams"))
        if (x.Name === "Insulin")
          assert(recordsFromCassandra(index).Units.equalsIgnoreCase("units"))
        if (x.Name === "Exercise")
          assert(recordsFromCassandra(index).Units.equalsIgnoreCase("minutes"))
        if (x.Name === "Health")
          assert(recordsFromCassandra(index).Units === "")
    }
  }

  test("TC_342 --~> should verify Model of the EventForPatientByDisplayTime in cassandra is populating properly") {

    //verify the results
    recordsFromCassandra.foreach {
      x =>
        assert(x.Model !== null)
        assert(x.Model !== "")
        assert(x.Model.isInstanceOf[String])
    }

    recordsFromCSV.foreach {
      x =>
        val index = recordsFromCassandra.indexWhere {
          y =>
            y.PatientID.equals(x.PatientID) &&
              y.DisplayTime.equals(x.DisplayTime) &&
              y.Name.equals(x.Name) &&
              y.Model.equals(x.Model)
        }
        assert(x.Model === recordsFromCassandra(index).Model)
    }
  }
  test("TC_341 --~> should verify DisplayTime of the EventForPatientByDisplayTime in cassandra is populating properly") {

    //verify the results
    recordsFromCassandra.foreach {
      x =>
        assert(x.DisplayTime !== null)
        assert(x.DisplayTime !== "")
        assert(x.DisplayTime.isInstanceOf[Date])
    }

    recordsFromCSV.foreach {
      x =>
        val index = recordsFromCassandra.indexWhere {
          y =>
            y.PatientID.equals(x.PatientID) &&
              y.DisplayTime.equals(x.DisplayTime) &&
              y.Name.equals(x.Name) &&
              y.Model.equals(x.Model)
        }
        assert(x.DisplayTime === recordsFromCassandra(index).DisplayTime)
    }
  }
  //Fri May 16 19:36:11 IST 2014   //Sat May 17 02:36:11 IST 2014
  test("TC_394 --~> should verify SystemTime of the EventForPatientByDisplayTime in cassandra is populating properly") {

    recordsFromCassandra.foreach {
      x =>
        assert(x.DisplayTime !== null)
        assert(x.DisplayTime !== "")
        assert(x.DisplayTime.isInstanceOf[Date])
    }

    recordsFromCSV.foreach {
      x =>
        val index = recordsFromCassandra.indexWhere {
          y =>
            y.PatientID.equals(x.PatientID) &&
              y.DisplayTime.equals(x.DisplayTime) &&
              y.Name.equals(x.Name) &&
              y.Model.equals(x.Model)
        }
        assert(x.SystemTime === recordsFromCassandra(index).SystemTime)
    }

  }

  override def afterAll() = {
    cassandraConnection.closeConnection()
    recordsFromCassandra = null
    recordsFromCSV = null
    cassandraConnection = null
    userEventDataHelper = null
    println("afterAll")
  }
}