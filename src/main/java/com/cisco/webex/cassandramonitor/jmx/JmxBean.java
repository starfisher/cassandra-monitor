package com.cisco.webex.cassandramonitor.jmx;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.management.AttributeList;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class JmxBean {
    String objectName;
    AttributeList attributeList;
}


