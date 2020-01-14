package com.cisco.webex.cassandramonitor.jmx;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Configuration
public class CassConfig {

    @Value("${monitor.host}")
    private String host;
    @Value("${monitor.port}")
    private int port;
    @Value("${monitor.username}")
    private String username;
    @Value("${monitor.password}")
    private String password;
    @Value("${monitor.fixedRate}")
    private int fixedRate;


}
