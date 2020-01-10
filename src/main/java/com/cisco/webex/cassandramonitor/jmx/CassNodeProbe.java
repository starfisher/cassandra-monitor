package com.cisco.webex.cassandramonitor.jmx;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMISocketFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CassNodeProbe implements AutoCloseable
{
    private final Logger logger = LoggerFactory.getLogger(CassNodeProbe.class);

    private static final String fmtUrl = "service:jmx:rmi:///jndi/rmi://[%s]:%d/jmxrmi";
    private static final int defaultPort = 7199;
    private final String host;
    private final int port;
    private String username;
    private String password;

    private JMXConnector jmxConnector;
    private MBeanServerConnection mBeanServerConnection;

    public CassNodeProbe(String host, int port, String username, String password)
            throws IOException, ClassNotFoundException {
        assert username != null && username != ""  && password != null && password != ""
                : "neither username nor password can be blank";
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
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
        JMXServiceURL jmxUrl = new JMXServiceURL(String.format(fmtUrl, host, port));
        Map<String,Object> env = new HashMap<String,Object>();
        if (username != null)
        {
            String[] creds = { username, password };
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
        System.out.println(jsonStr);
        logger.info(jsonStr);
    }


}
