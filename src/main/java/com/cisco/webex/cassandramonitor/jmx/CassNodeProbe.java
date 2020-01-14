package com.cisco.webex.cassandramonitor.jmx;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.io.IOException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMISocketFactory;
import java.util.HashMap;
import java.util.Map;


@Component
public class CassNodeProbe implements AutoCloseable
{
    private final Logger logger = LoggerFactory.getLogger(CassNodeProbe.class);
    private static final String fmtUrl = "service:jmx:rmi:///jndi/rmi://[%s]:%d/jmxrmi";

    private CassConfig cassConfig;
    private JMXConnector jmxConnector;
    private MBeanServerConnection mBeanServerConnection;

    public CassNodeProbe(@Qualifier("cassConfig") CassConfig cassConfig) throws Exception {
        this.cassConfig = cassConfig;
        connect();
    }


    private RMIClientSocketFactory getRMIClientSocketFactory() throws IOException
    {
        if (Boolean.parseBoolean(System.getProperty("ssl.enable")))
            return new SslRMIClientSocketFactory();
        else
            return RMISocketFactory.getDefaultSocketFactory();
    }



    private void connect() throws IOException, ClassNotFoundException {
        String url = String.format(fmtUrl, cassConfig.getHost(), cassConfig.getPort());
        JMXServiceURL jmxUrl = new JMXServiceURL(url);
        Map<String,Object> env = new HashMap<String,Object>();
        if (cassConfig.getUsername() != null)
        {
            String[] creds = { cassConfig.getUsername(), cassConfig.getPassword() };
            env.put(JMXConnector.CREDENTIALS, creds);
        }
        env.put("com.sun.jndi.rmi.factory.socket", getRMIClientSocketFactory());
        jmxConnector = JMXConnectorFactory.connect(jmxUrl, env);
        mBeanServerConnection = jmxConnector.getMBeanServerConnection();
    }

    public void close() throws Exception
    {
        jmxConnector.close();
    }

    public void logMBeanInfo(String name) throws Exception {
        ObjectName objectName = new ObjectName(name);
        MBeanInfo mBeanInfo = mBeanServerConnection.getMBeanInfo(objectName);
        MBeanAttributeInfo[] mBeanAttributeInfos = mBeanInfo.getAttributes();
        String[] attributes = new String[mBeanAttributeInfos.length];
        int index = 0;
        for(MBeanAttributeInfo info : mBeanAttributeInfos) {
            attributes[index++] = info.getName();
        }
        AttributeList list = mBeanServerConnection.getAttributes(objectName, attributes);
        JmxBean jmxBean = new JmxBean(name, list);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(jmxBean);
        //System.out.println(jsonStr);
        logger.info(jsonStr);
    }


}
