package com.dexcom.testCase

import java.util
import java.util.UUID

import com.dexcom.common.CassandraQueries
import com.dexcom.connection.CassandraConnection
import com.dexcom.dto.UserEventRecord
import com.dexcom.helper.UserEventDataHelper
import org.scalatest.{BeforeAndAfterAll, FunSuite}

/**
  * Created by sarvaraj on 17/01/17.
  */
class EventForPatientBySystemTime extends FunSuite with CassandraQueries with BeforeAndAfterAll {
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


  test("TC_378 --~> should verify PatientId of the EventForPatientBySystemTime in cassandra is populating properly") {

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
              y.SystemTimes.equals(x.SystemTimes) &&
              y.PostID.equals(x.PostID) &&
              y.Model.equals(x.Model)
        }
        assert(x.PatientID === recordsFromCassandra(index).PatientID)
    }
  }


  test("TC_337 --~> should verify Name of the EventForPatientBySystemTime in cassandra is populating properly") {

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
              y.SystemTimes.equals(x.SystemTimes) &&
              y.Name.equals(x.Name) &&
              y.Model.equals(x.Model)
        }
        assert(x.Name === recordsFromCassandra(index).Name)
    }
  }

  test("TC_338 --~> should verify SubType of the EventForPatientBySystemTime in cassandra is populating properly") {

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
              y.SystemTimes.equals(x.SystemTimes) &&
              y.Name.equals(x.Name) &&
              y.Model.equals(x.Model)
        }
        assert(x.Name === recordsFromCassandra(index).Name && nameAndSubtypeMapping.get(x.Name).contains(recordsFromCassandra(index).SubType))
    }
  }

  test("TC_339 --~> should verify Value of the EventForPatientBySystemTime in cassandra is populating properly") {

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
              y.SystemTimes.equals(x.SystemTimes) &&
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

  test("TC_340 --~> should verify Unit of the EventForPatientBySystemTime in cassandra is populating properly") {

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
              y.SystemTimes.equals(x.SystemTimes) &&
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

  override def afterAll() = {
    cassandraConnection.closeConnection()
    recordsFromCassandra = null
    recordsFromCSV = null
    cassandraConnection = null
    userEventDataHelper = null
    println("afterAll")
  }
}
