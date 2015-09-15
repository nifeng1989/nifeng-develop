package com.nifeng.server.server;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.*;

/**
 * Created by wangwj on 30/07/14.
 */
public class SystemConfig {
    private static Properties props;
    static{
        //初始化配置到System环境变量中
      //loadConfig("smc-nsq-config.properties");
      //loadConfig("smc-api-memcached.properties");
    }
    SystemConfig(String fileName){
        loadConfig(fileName);
    }

    public static Properties loadConfig(String fileName){
        Properties properties = new Properties();
        try{
            InputStream ins=Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (ins!=null){
                properties.load(ins);
                ins.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        Iterator keys=properties.keySet().iterator();
        while(keys.hasNext()){
            String key=(String)keys.next();
            String value=properties.getProperty(key);
            System.setProperty(key,value);
        }
        return properties;
    }

    public static void setProps(Properties properties) {
        props = properties;
    }

    /**
     * 获取key对应的字符串值
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        if (props != null) {
            return props.getProperty(key);
        }
        return "";
    }

    /**
     * 获取key对应的字符串值，如果为空，返回默认值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(String key, String defaultValue) {
        if (props != null) {
            String value = props.getProperty(key);
            if (StringUtils.isBlank(value)) {
                return defaultValue;
            } else {
                return value;
            }
        }
        return defaultValue;
    }

    /**
     * 获取key对应的整型值
     *
     * @param key
     * @return
     */
    public static int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * 获取key对应的整型值，如果为空，返回默认值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(String key, int defaultValue) {
        if (props == null) return defaultValue;
        String value = props.getProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 获取bool值
     *
     * @param key
     * @return
     */
    public static Boolean getBoolean(String key) {
        if (props == null) return false;
        return Boolean.parseBoolean(props.getProperty(key));
    }

    /**
     * 把逗号分隔的值以List方式返回
     *
     * @param key
     * @return
     */
    public static List<String> getList(String key) {
        List<String> list = new ArrayList<String>();
        if (props == null) return list;
        String values = props.getProperty(key);
        if (StringUtils.isNotBlank(values)) {
            String[] items = values.split(",");
            for (String item : items) {
                list.add(item);
            }
        }
        return list;
    }

    /**
     * 把逗号分隔的值以Set方式返回，滤重
     *
     * @param key
     * @return
     */
    public static HashSet<String> getSet(String key) {
        HashSet<String> set = new HashSet<String>();
        if (props == null) return set;
        String values = props.getProperty(key);
        if (StringUtils.isNotBlank(values)) {
            String[] items = values.split(",");
            for (String item : items) {
                set.add(item);
            }

        }
        return set;
    }

}
