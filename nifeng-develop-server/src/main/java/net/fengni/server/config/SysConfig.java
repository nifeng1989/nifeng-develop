package net.fengni.server.config;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by fengni on 2015/9/3.
 */
public class SysConfig {
    private static final Properties properties = new Properties();
    static{
        try{
            InputStream ins=Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
            if (ins!=null){
                properties.load(ins);
                ins.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static String getProperty(String key){
        return properties.getProperty(key);
    }
}
