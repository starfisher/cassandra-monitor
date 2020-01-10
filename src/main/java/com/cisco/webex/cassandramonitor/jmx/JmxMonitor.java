package com.cisco.webex.cassandramonitor.jmx;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

@Component
@EnableScheduling
public class JmxMonitor {

    @Autowired
    private CassNodeProbe cassNodeProbe;

    private ArrayList<String> objectNames = new ArrayList<>();

    public JmxMonitor() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(JmxMonitor.class.getResourceAsStream("/mbean.cnf")));
        while (true) {
            String str = reader.readLine();
            if(str!=null) {
                objectNames.add(str);
            } else
                break;
        }
        reader.close();
    }

    @Scheduled(cron = "0/1 * * * * ? ")
    public void schedule() throws Exception {
        try {
            for(String objectName : objectNames) {
                cassNodeProbe.logMBeanInfo(objectName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            cassNodeProbe.close();
        }
    }

}
