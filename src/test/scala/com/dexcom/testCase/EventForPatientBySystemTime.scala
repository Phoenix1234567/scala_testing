package com.dexcom.testCase

import java.util.{Date, UUID}

import com.datastax.driver.core.Session
import com.dexcom.common.CassandraQueries
import com.dexcom.connection.CassandraConnection
import com.dexcom.dto.{UserEventR, UserEventRecord}
import com.dexcom.helper.UserEventDataHelper
import org.scalatest.{BeforeAndAfterAll, FunSuite}

/**
  * Created by sarvaraj on 17/01/17.
  */
class EventForPatientBySystemTime extends FunSuite with CassandraQueries with BeforeAndAfterAll {
  var recordsFromCassandra: List[UserEventR] = null
  var recordsFromCSV: List[UserEventR] = null
  var cassandraConnection: CassandraConnection = null

  override def beforeAll() = {
    cassandraConnection = new CassandraConnection()
    val userEventDataHelper = new UserEventDataHelper(cassandraConnection.getConnection)
    recordsFromCassandra = userEventDataHelper.getRecordFromCassandra()
    recordsFromCSV = userEventDataHelper.getRecordsFromCSV()
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
              y.PostID.equals(x.PostID)
        }
        assert(x.PatientID === recordsFromCassandra(index).PatientID)
    }
  }



  override def afterAll() = {
    cassandraConnection.closeConnection()
    recordsFromCassandra = null
    recordsFromCSV = null
    cassandraConnection = null
  }
}
