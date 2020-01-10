package com.cisco.webex.cassandramonitor.jmx;


import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

@Component
@EnableScheduling
public class JmxMonitor {

    private static CassNodeProbe probe;
    private static ArrayList<String> list = new ArrayList<>();

    static void init() throws Exception {
        probe = new CassNodeProbe("10.224.57.244", 7199, "cassandra", "cassandra");
        BufferedReader reader = new BufferedReader(new InputStreamReader(JmxMonitor.class.getResourceAsStream("/mbean.cnf")));
        while (true) {
            String str = reader.readLine();
            if(str!=null) {
                list.add(str);
            } else
                break;
        }
        reader.close();
    }


    static {
        try {
            init();
        } catch (Exception e) {
            throw new RuntimeException("init error");
        }
    }

//    @Scheduled(cron = "0/1 * * * * ? ")
//    public void schedule() throws Exception {
//        try {
//            for(String s : list) {
//                probe.printInfo(s);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            probe.close();
//        }
//    }

}
