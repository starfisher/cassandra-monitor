package com.cisco.webex.cassandramonitor;

import com.cisco.webex.cassandramonitor.jmx.CassConfig;
import com.cisco.webex.cassandramonitor.jmx.CassNodeProbe;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CassandraMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CassandraMonitorApplication.class, args);
    }

//    @Bean(name = "cassNodeProbe")
//    public CassNodeProbe cassNodeProbe(@Qualifier("cassConfig") CassConfig cassConfig) throws Exception {
//        return new CassNodeProbe(cassConfig);
//    }

}
