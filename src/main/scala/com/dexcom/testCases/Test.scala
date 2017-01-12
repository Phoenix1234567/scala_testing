package com.dexcom.testCases

import com.dexcom.connection.CassandraConnection

/**
  * Created by gaurav.garg on 05-01-2017.
  */
object Test extends App {

  val cassandraConnection = new CassandraConnection
  val session = cassandraConnection.getConnection

  val resultSet = session.execute("SELECT * FROM gg_test.egv_for_patient_by_system_time")

  while(!resultSet.isExhausted) {
    val row = resultSet.one()
    println(row.getString("trend"))
  }

  cassandraConnection.closeConnection()
}

