package com.cisco.webex.cassandramonitor.jmx;


import java.io.BufferedReader;
import java.io.InputStreamReader;

public class JMXTest
{

    public static void main(String[] args) throws Exception
    {
        CassNodeProbe prode = new CassNodeProbe("10.224.57.244", 7199, "cassandra", "cassandra");
        BufferedReader reader = new BufferedReader(new InputStreamReader(JMXTest.class.getResourceAsStream("/mbean.cnf")));
        String str = null;
        while (true) {
            str = reader.readLine();
            if(str!=null) {
                prode.logMBeanInfo(str);
            } else
                break;
        }
        reader.close();
        prode.close();
    }

}
