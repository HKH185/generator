package com.generator.util;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
* @author houkaihong
* @Date 15:03 2019/10/22
* @description：TODO
*/
public class Config {

    static String configFileName = "generator.ini";
    static String ConfigLocal = System.getProperty("user.dir") + File.separator + "conf" + File.separator + configFileName;


    public static Properties read() {
        Properties properties = new Properties();
        try {
            File pFile = new File(ConfigLocal);
            if (pFile.exists()) {
                InputStream inputStream = new FileInputStream(pFile);
                properties.load(inputStream);
                inputStream.close(); //关闭流
                return properties;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Properties read(String path) {
        Properties properties = new Properties();
        try {
            File pFile = new File(path);
            if (pFile.exists()) {
                InputStream inputStream = new FileInputStream(pFile);
                properties.load(inputStream);
                inputStream.close(); //关闭流
                return properties;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void write(Map paramMap) {
        Properties properties = new Properties();
        try {
            File pFile = new File(ConfigLocal);
            if (!pFile.exists()) {
                pFile.createNewFile();
            } else {
                properties = read();
            }

            OutputStream outputStream = new FileOutputStream(pFile);
            Iterator it = paramMap.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                properties.setProperty(key, (String) paramMap.get(key));
            }
            properties.store(outputStream, "author: http://inhu.net");
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
