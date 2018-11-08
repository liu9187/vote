package com.minxing365.vote.util;

import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    private final static Logger logger = Logger.getLogger(PropertiesUtil.class);
    private static Properties prop = null;

    static {
        prop = new Properties();
        try {
            String confPath = System.getProperty("conf", "config/application.properties");
            InputStream in = new BufferedInputStream(new FileInputStream(confPath));
            prop.load(in); /// 加载属性列表
            in.close();
        } catch (Exception e) {
            logger.error("PropertiesUtil error>>>", e);
        }
    }

    public static String getValue(String urlName) {
        return prop.getProperty(urlName);
    }

    public static void main(String[] args) {

        String urlValue = PropertiesUtil.getValue("server.port");
        System.out.println("urlValue==" + urlValue);
    }
}
