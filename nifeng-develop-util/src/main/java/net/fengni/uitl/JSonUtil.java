package net.fengni.uitl;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


public class JSonUtil {
    private static ThreadLocal<StringBuilder> local = new ThreadLocal<StringBuilder>();

    private static Map<Class, BeanInfo> beanInfoMap = Collections.synchronizedMap(new WeakHashMap<Class, BeanInfo>());

    public static String jsonCharFilter(String sourceStr) {
        if (sourceStr == null) {
            return "";
        }
        sourceStr = sourceStr.replace("\n", "");
        sourceStr = sourceStr.replace("\r", "");
        return sourceStr;
    }
    /**
     * 将Java对象转换为JSON格式的字符串
     *
     * @param javaObj POJO,例如日志的model
     * @return JSON格式的String字符串
     */
    public static String getJsonStringFromJavaPOJO(Object javaObj) {
        return new JSONObject(javaObj).toString(1);
    }

    /**
     * 将JavaBen，其属性包含集合、map的复杂对象转换成JSON字符串；
     *
     * @param obj
     * @return
     */

    public static String getJsonStringFromObject(Object obj) {
        fromObject(obj);
        StringBuilder builder = builder();
        local.remove();

        return builder.toString();
    }

    private static StringBuilder builder() {
        StringBuilder builder = local.get();
        if (builder == null) {
            builder = new StringBuilder();
            local.set(builder);
        }
        return builder;
    }

    private static void fromObject(Object obj) {
        if (obj == null) {
            builder().append("null");
        } else if (obj instanceof String) {
            builder().append(quote((String) obj));
        } else if (obj instanceof Number || obj instanceof Character || obj instanceof Boolean) {
            fromPrimitive(obj);
        } else if (obj instanceof Date) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = format.format((Date) obj);
            builder().append("\"").append(date).append("\"");
        } else if (obj.getClass().isArray()) {
            fromArray(obj);
        } else if (obj instanceof Collection) {
            fromCollection((Collection) obj);
        } else if (obj instanceof Map) {
            fromMap((Map) obj);
        } else {
            fromBean(obj);
        }
    }

    private static void fromPrimitive(Object obj) {
        if (obj instanceof Character) {
            Character c = (Character) obj;
            char[] carr = {c};
            builder().append(quote(new String(carr)));
        } else {
            builder().append(obj);
        }
    }

    // 该方法拷贝自net.sf.json.util.JSONUtils
    private static String quote(String string) {
        char b;
        char c = 0;
        int i;
        int len = string.length();
        StringBuffer sb = new StringBuffer(len * 2);
        String t;
        char[] chars = string.toCharArray();
        char[] buffer = new char[1030];
        int bufferIndex = 0;
        sb.append('"');
        for (i = 0; i < len; i += 1) {
            if (bufferIndex > 1024) {
                sb.append(buffer, 0, bufferIndex);
                bufferIndex = 0;
            }
            b = c;
            c = chars[i];
            switch (c) {
                case '\\':
                case '"':
                    buffer[bufferIndex++] = '\\';
                    buffer[bufferIndex++] = c;
                    break;
                case '/':
                    if (b == '<') {
                        buffer[bufferIndex++] = '\\';
                    }
                    buffer[bufferIndex++] = c;
                    break;
                default:
                    if (c < ' ') {
                        switch (c) {
                            case '\b':
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 'b';
                                break;
                            case '\t':
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 't';
                                break;
                            case '\n':
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 'n';
                                break;
                            case '\f':
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 'f';
                                break;
                            case '\r':
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 'r';
                                break;
                            default:
                                t = "000" + Integer.toHexString(c);
                                int tLength = t.length();
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 'u';
                                buffer[bufferIndex++] = t.charAt(tLength - 4);
                                buffer[bufferIndex++] = t.charAt(tLength - 3);
                                buffer[bufferIndex++] = t.charAt(tLength - 2);
                                buffer[bufferIndex++] = t.charAt(tLength - 1);
                        }
                    } else {
                        buffer[bufferIndex++] = c;
                    }
            }
        }
        sb.append(buffer, 0, bufferIndex);
        sb.append('"');
        return sb.toString();
    }

    private static void fromArray(Object array) {
        StringBuilder builder = builder();
        builder.append("[");
        Class type = array.getClass().getComponentType();
        if (!type.isPrimitive()) {
            Object[] objArr = (Object[]) array;
            for (int i = 0; i < objArr.length; i++) {
                fromObject(objArr[i]);
                if (i != (objArr.length - 1)) {
                    builder.append(",");
                }
            }
        } else if (type == Boolean.TYPE) {
            boolean[] boolArr = (boolean[]) array;
            for (int i = 0; i < boolArr.length; i++) {
                builder.append(boolArr[i]);
                if (i != (boolArr.length - 1)) {
                    builder.append(",");
                }
            }
        } else if (type == Byte.TYPE) {
            byte[] byteArr = (byte[]) array;
            for (int i = 0; i < byteArr.length; i++) {
                builder.append(byteArr[i]);
                if (i != (byteArr.length - 1)) {
                    builder.append(",");
                }
            }
        } else if (type == Short.TYPE) {
            short[] shortArr = (short[]) array;
            for (int i = 0; i < shortArr.length; i++) {
                builder.append(shortArr[i]);
                if (i != (shortArr.length - 1)) {
                    builder.append(",");
                }
            }
        } else if (type == Integer.TYPE) {
            int[] intArr = (int[]) array;
            for (int i = 0; i < intArr.length; i++) {
                builder.append(intArr[i]);
                if (i != (intArr.length - 1)) {
                    builder.append(",");
                }
            }
        } else if (type == Long.TYPE) {
            long[] longArr = (long[]) array;
            for (int i = 0; i < longArr.length; i++) {
                builder.append(longArr[i]);
                if (i != (longArr.length - 1)) {
                    builder.append(",");
                }
            }
        } else if (type == Float.TYPE) {
            float[] floatArr = (float[]) array;
            for (int i = 0; i < floatArr.length; i++) {
                builder.append(floatArr[i]);
                if (i != (floatArr.length - 1)) {
                    builder.append(",");
                }
            }
        } else if (type == Double.TYPE) {
            double[] doubleArr = (double[]) array;
            for (int i = 0; i < doubleArr.length; i++) {
                builder.append(doubleArr[i]);
                if (i != (doubleArr.length - 1)) {
                    builder.append(",");
                }
            }
        } else if (type == Character.TYPE) {
            char[] charArr = (char[]) array;
            for (int i = 0; i < charArr.length; i++) {
                char[] carr = {charArr[i]};
                builder.append(quote(new String(carr)));
                if (i != (charArr.length - 1)) {
                    builder.append(",");
                }
            }
        }
        builder.append("]");
    }

    private static void fromCollection(Collection coll) {
        StringBuilder builder = builder();
        builder.append("[");
        Iterator iterator = coll.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            fromObject(obj);
            if (iterator.hasNext()) {
                builder.append(",");
            }
        }
        builder.append("]");
    }

    private static void fromMap(Map map) {
        StringBuilder builder = builder();
        builder.append("{");
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();
            builder.append("\"").append(key).append("\":");
            fromObject(map.get(key));
            if (iterator.hasNext()) {
                builder.append(",");
            }
        }
        builder.append("}");
    }

    private static void fromBean(Object bean) {
        StringBuilder builder = builder();
        builder.append("{");

        try {
            // BeanInfo beanInfo = getBeanInfo(bean.getClass(), bean
            // .getClass().getSuperclass());
            BeanInfo beanInfo = getBeanInfo(bean.getClass(), Object.class);

            PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < props.length; i++) {
                PropertyDescriptor pdesc = props[i];
                String pname = pdesc.getName();
                Object[] args = {};
                Object pvalue = pdesc.getReadMethod().invoke(bean, args);
                if (pvalue == null) {
                    if (i == (props.length - 1)) {
                        builder.deleteCharAt(builder.length() - 1);
                    }
                    continue;
                }
                builder.append("\"").append(pname).append("\":");
                fromObject(pvalue);
                if (i != (props.length - 1)) {
                    builder.append(",");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        builder.append("}");
    }

    private static BeanInfo getBeanInfo(Class beanClass, Class stopClass) throws IntrospectionException {
        BeanInfo beanInfo = beanInfoMap.get(beanClass);
        if (beanInfo == null) {
            beanInfo = Introspector.getBeanInfo(beanClass, stopClass);
            beanInfoMap.put(beanClass, beanInfo);
        }

        return beanInfo;
    }

    /**
     * 将json串转换为一个map对象
     *
     * @param jsonString
     * @return
     */
    public static Map<String, Object> getMapFromJson(String jsonString) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        JSONObject jsonObject = new JSONObject(jsonString);
        Iterator<String> keyIter = jsonObject.keys();
        String key;
        Object value;
        Map<String, Object> valueMap = new HashMap<String, Object>();

        while (keyIter.hasNext()) {
            key = (String) keyIter.next();
            value = jsonObject.get(key);
            valueMap.put(key, value);
        }

        return valueMap;
    }


    public static Map<String, String> transBean2Map(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 得到property对应的getter方法
                Method getter = property.getReadMethod();
                Object value = getter.invoke(obj);
                if (!key.equals("class")) {
                    if (value == null)
                        map.put(key, null);
                    else
                        map.put(key, String.valueOf(value));
                }
            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }
        return map;
    }
}
