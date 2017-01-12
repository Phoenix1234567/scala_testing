package com.dexcom.configuration;

import java.io.File;

/**
 * Created by sarvaraj on 12/01/17.
 */
public class tests {

    String sf() {
        try

        {
            String s = new File("./src/main/resources/cassandra/cassandra-truststore.jks").getCanonicalPath();
            return s.toString();
        } catch (Exception IoException) {
        return "";
        }
    }
}


