package com.cisco.webex.cassandramonitor.jmx;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class JmxMonitor extends TimerTask {

    private static CassNodeProbe cassNodeProbe;

    private static ArrayList<String> objectNames = new ArrayList<>();

    public JmxMonitor(@Qualifier("cassNodeProbe") CassNodeProbe cassNodeProbe) throws Exception {
        JmxMonitor.cassNodeProbe = cassNodeProbe;
        int fixedRate = cassNodeProbe.getFixedRate();
        BufferedReader reader = new BufferedReader(new InputStreamReader(JmxMonitor.class.getResourceAsStream("/mbean.cnf")));
        while (true) {
            String str = reader.readLine();
            if(str!=null) {
                objectNames.add(str);
            } else
                break;
        }
        reader.close();
        Timer timer = new Timer();
        timer.schedule(this, 0,  fixedRate);
    }

    @Override
    public void run() {
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
