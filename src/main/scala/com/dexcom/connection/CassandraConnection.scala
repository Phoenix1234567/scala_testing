package com.dexcom.connection

import java.io.{FileInputStream, InputStream}
import java.security.KeyStore
import javax.net.ssl.TrustManagerFactory

import com.datastax.driver.core.{Cluster, NettySSLOptions, SSLOptions, Session}
import com.dexcom.configuration.DexVictoriaConfigurations
import io.netty.handler.ssl.SslContextBuilder

/**
  * Created by gaurav.garg on 05-01-2017.
  */
class CassandraConnection extends DexVictoriaConfigurations {

    var cluster: Cluster = null
  /**
    * Getting cassandra connection
    * @return session of the cassandra
    */
  def getConnection : Session = {
    val ks = KeyStore.getInstance("JKS")
    // make sure you close this stream properly (not shown here for brevity)
    val trustStore: InputStream = new FileInputStream(trueStorePath)
    ks.load(trustStore, trueStorePassword.toCharArray)
    val tmf: TrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
    tmf.init(ks)

    val builder: SslContextBuilder = SslContextBuilder
      .forClient()
      .trustManager(tmf)

    val sslOptions: SSLOptions = new NettySSLOptions(builder.build())

    cluster = Cluster.builder()
        .addContactPoint(hostname)
        .withCredentials(userName, password)
        .withPort(port)
        .withSSL(sslOptions)
        .build()

    cluster.connect()
  }

  /**
    * To close cassandra connection
    */
  def closeConnection() {
        cluster.close()
    }
}