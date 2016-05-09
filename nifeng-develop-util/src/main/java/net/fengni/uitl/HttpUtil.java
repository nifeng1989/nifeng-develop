package net.fengni.uitl;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;


public class HttpUtil {

    private static final String CONTENT_CHARSET = "UTF-8";
    // 设置连接超时时间(单位毫秒) 
    public static int HTTP_CONNECTION_TIMEOUT = 1000;
    // 设置读数据超时时间(单位毫秒) 
    public static int HTTP_READ_TIMEOUT = 2000;
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private static MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();

    static{
    	connectionManager.getParams().setDefaultMaxConnectionsPerHost(100);
        connectionManager.getParams().setMaxTotalConnections(500);
        connectionManager.getParams().setConnectionTimeout(HTTP_CONNECTION_TIMEOUT);
        connectionManager.getParams().setSoTimeout(HTTP_READ_TIMEOUT);
    }
    
    private static HttpClient client = new HttpClient(connectionManager);
    


    public static boolean exists(String URLName) {
        try {
            // 设置此类是否应该自动执行 HTTP 重定向（响应代码为 3xx 的请求）。
            HttpURLConnection.setFollowRedirects(false);
            // 到 URL 所引用的远程对象的连接
            HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
            /*
             * 设置 URL 请求的方法， GET POST HEAD OPTIONS PUT DELETE TRACE
             * 以上方法之一是合法的，具体取决于协议的限制。
             */
            con.setRequestMethod("HEAD");

            // 从 HTTP 响应消息获取状态码
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);

        } catch (Exception e) {
            logger.error("[exists] occur Exception URLName=" + URLName, e);
            return false;
        }
    }

    /**
     * 
     * @param url
     * @param charset
     * @param poststrings
     *            a=a&b=b类型
     * @return
     */
    public static String getUrlContent(String url, String charset, String[] poststrings) {
        // URL url1 = new URL(url);//internal site
        // InputStream inr = null;
        HttpURLConnection huc = null;
        long time = System.currentTimeMillis();
        try {
            URLConnection UrlConnObj1 = new URL(url).openConnection();
            huc = (HttpURLConnection) UrlConnObj1;
            huc.setRequestMethod("POST");
            huc.setDoOutput(true);
            huc.setDoInput(true);
            huc.setUseCaches(false);
            huc.setDefaultUseCaches(false);
            BufferedOutputStream out = new BufferedOutputStream(huc.getOutputStream());
            for (int i = 0; i < poststrings.length; i++) {
                out.write(poststrings[i].getBytes(charset));
                if (i < poststrings.length - 1)
                    out.write('&');
            }
            out.flush();
            out.close();

            InputStream inr = huc.getInputStream();
            char[] buf = new char[1000];
            int length = -1;
            // String result="";
            StringBuffer res = new StringBuffer();
            InputStreamReader reader = new InputStreamReader(inr, charset);
            while ((length = reader.read(buf)) != -1) {
                res.append(buf, 0, length);
            }
            reader.close();
            // inr.close();
            return res.toString();

        } catch (Exception e) {
            logger.error("[getUrlContent] occur Exception url=" + url + " charset=" + charset + " poststrings="
                    + poststrings + " time=" + (System.currentTimeMillis() - time), e);
            return null;
        } finally {
            if (huc != null)
                huc.disconnect();
        }

    }

    /**
     * @param url
     * @param charset
     * @param binary
     * @return
     */
    public static String postBinaryToUrl(String url, String charset, byte[] binary) {
        // URL url1 = new URL(url);//internal site
        // InputStream inr = null;
        HttpURLConnection huc = null;
        try {
            URLConnection UrlConnObj1 = new URL(url).openConnection();
            huc = (HttpURLConnection) UrlConnObj1;
            huc.setRequestMethod("POST");
            huc.setRequestProperty("content-type", "binarry/data");
            huc.setDoOutput(true);
            huc.setDoInput(true);
            huc.setUseCaches(false);
            huc.setDefaultUseCaches(false);
            BufferedOutputStream out = new BufferedOutputStream(huc.getOutputStream());
            out.write(binary);
            out.flush();
            out.close();

            InputStream inr = huc.getInputStream();
            char[] buf = new char[1000];
            int length = -1;
            // String result="";
            StringBuffer res = new StringBuffer();
            InputStreamReader reader = new InputStreamReader(inr, charset);
            while ((length = reader.read(buf)) != -1) {
                res.append(buf, 0, length);
            }
            reader.close();
            // inr.close();
            return res.toString();

        } catch (Exception e) {
            logger.error("[postBinaryToUrl] occur Exception url=" + url + " charset=" + charset, e);
            return null;
        } finally {
            if (huc != null)
                huc.disconnect();
        }

    }

    /**
     * 以Post方式访问http主机
     * 
     * @param url
     * @param charset
     * @param headers
     * @return
     */
    public static String getUrlContentWithHeaders(String url, String charset, Map headers) {
        // URL url1 = new URL(url);//internal site
        // InputStream inr = null;
        HttpURLConnection huc = null;
        try {
            URLConnection UrlConnObj1 = new URL(url).openConnection();
            huc = (HttpURLConnection) UrlConnObj1;

            for (Iterator iter = headers.entrySet().iterator(); iter.hasNext();) {
                Entry entry = (Entry) iter.next();
                huc.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
            }
            huc.connect();
            InputStream inr = huc.getInputStream();
            char[] buf = new char[300];
            int length = -1;
            // String result="";
            StringBuffer res = new StringBuffer(3000);
            InputStreamReader reader = new InputStreamReader(inr, charset);
            while ((length = reader.read(buf)) != -1) {
                res.append(buf, 0, length);
            }
            reader.close();
            return res.toString();
        } catch (Exception e) {
            logger.error(
                    "[postBinaryToUrl] occur Exception url=" + url + " charset=" + charset + " headers=" + headers, e);
            return null;
        } finally {
            if (huc != null)
                try {
                    huc.disconnect();
                } catch (Exception e) {
                }
            ;
        }

    }

    /**
     * 按 iso8859-1字符集取得url内容
     * 
     * @param url
     * @return
     */
    public static String getUrlContent(String url) {
        return getUrlContent(url, "ISO8859-1");
    }

    /**
     * 按指定字符集取得url的内容
     * 
     * @param newurl
     *            url
     * @param charset
     *            指定url字符集
     * @return
     */

    public static String getUrlContent(String newurl, String charset) {
        InputStream inr = null;
        HttpURLConnection conn = null;
        long time = System.currentTimeMillis();
        try {
            conn = (HttpURLConnection) (new URL(newurl)).openConnection();
            inr = conn.getInputStream();
            char[] buf = new char[300];
            int length = -1;
            StringBuffer res = new StringBuffer(3000);
            InputStreamReader reader = new InputStreamReader(inr, charset);
            while ((length = reader.read(buf)) != -1) {
                res.append(buf, 0, length);
            }
            reader.close();
            String content = res.toString();
            return content;
        } catch (Exception e) {
            logger.error(
                    "[getUrlContent] occur Exception url=" + newurl + " charset=" + charset + " time="
                            + (System.currentTimeMillis() - time), e);
            return null;
        } finally {
            try {
                if (inr != null)
                    inr.close();
                if (conn != null)
                    conn.disconnect();
            } catch (IOException e) {
            }
        }
    }

    public static String getUrlContentWithTimeOut(String newurl, String charset, int timeOut) {
        InputStream inr = null;
        HttpURLConnection conn = null;
        StringBuffer res = new StringBuffer();
        try {
            conn = (HttpURLConnection) (new URL(newurl)).openConnection();
            conn.setConnectTimeout(timeOut);
            conn.setReadTimeout(timeOut * 60);
            inr = conn.getInputStream();
            char[] buf = new char[300];
            int length = -1;
            res = new StringBuffer(3000);
            InputStreamReader reader = new InputStreamReader(inr, charset);
            while ((length = reader.read(buf)) != -1) {
                res.append(buf, 0, length);
            }
            reader.close();
        } catch (IOException e) {
            logger.error("[getUrlContentWithTimeOut] occur Exception url=" + newurl + " charset=" + charset, e);
        } finally {
            try {
                if (inr != null)
                    inr.close();
                if (conn != null)
                    conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String content = res.toString();
        return content;
    }

    /**
     * 抓一个http页面
     */
    public static String getResponseFromHttp(String address) {
        InputStream inr = null;
        HttpURLConnection conn = null;

        try {

            conn = (HttpURLConnection) (new URL(address)).openConnection();
            inr = conn.getInputStream();

            BufferedReader d = new BufferedReader(new InputStreamReader(inr));
            String line = "";
            StringBuffer sb = new StringBuffer();

            while ((line = d.readLine()) != null) {
                sb.append(line + "\n");
            }
            d.close();
            return sb.toString();
        } catch (Exception e) {
            logger.error("[getResponseFromHttp] occur Exception url=" + address, e);
            return null;
        } finally {
            try {
                if (inr != null)
                    inr.close();
                if (conn != null)
                    conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    /**
     * 抓一个http页面
     */
    public static final String CHROME_USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/534.13 (KHTML, like Gecko) Chrome/9.0.597.107 Safari/534.13";

    @Deprecated
    public static byte[] getBytesFromHttp(String address) {
        InputStream inr = null;
        HttpURLConnection conn = null;
        long startTime = System.currentTimeMillis();
        try {
            conn = (HttpURLConnection) (new URL(address)).openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("User-agent", CHROME_USER_AGENT);
            conn.setRequestProperty("Connection", "Keep-Alive");
            inr = conn.getInputStream();

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            int length = -1;
            int len = 1024;
            byte[] buffer = new byte[len];

            while ((length = (inr.read(buffer, 0, len))) != -1) {
                output.write(buffer, 0, length);
            }
            byte[] result = output.toByteArray();
            if(result != null && result.length > 0){
            	logger.info("[HttpUtil] getBytesFromHttp size" + result.length + ",url=" + address);
            }
            output.close();
            logger.info("[HttpUtil] getBytesFromHttp costTime" + (System.currentTimeMillis() - startTime) + "," + address);
            return result;
        } catch (Exception e) {
            logger.error("[getBytesFromHttp] occur Exception url=" + address + " time="  + (System.currentTimeMillis() - startTime), e);
            return null;
        } finally {
            try {
                if (inr != null)
                    inr.close();
                if (conn != null)
                    conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    public static byte[] getResponseBodyAsByte(String url, String charSet) {
        HttpClient httpClient = new HttpClient();
        GetMethod get = new GetMethod(url);
        // httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(2000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);
        get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(1, false));
        try {
            httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charSet);
            httpClient.executeMethod(get);
            return get.getResponseBody();
        } catch (Exception e) {
            logger.error("[getResponseBodyAsByte] occur Exception url=" + url + " charSet=" + charSet, e);
        } finally {
            get.releaseConnection();
        }
        return null;
    }

    /**
     * @author gaojiechen 2013/6/28
     * @param url
     * @return
     */
    public static String getResponseBodyAsString(String url) {
        return getResponseBodyAsString(url, CONTENT_CHARSET);
    }

    /**
     * @author gaojiechen 2013/6/28
     * @param url
     * @param charSet
     * @return
     */
    public static String getResponseBodyAsString(String url, String charSet) {
        GetMethod get = new GetMethod(url);
        get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
        try {
            client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charSet);
            client.executeMethod(get);
            InputStreamReader inputReader = new InputStreamReader(get.getResponseBodyAsStream(), charSet);
            BufferedReader bufferReader = new BufferedReader(inputReader);
            String content = "";
            StringBuilder sb = new StringBuilder();
            while ((content = bufferReader.readLine()) != null) {
                sb.append(content);
            }
            bufferReader.close();
            inputReader.close();
            return sb.toString();
        } catch (Exception e) {
            logger.error("[getResponseBodyAsString] occur Exception url=" + url + " charSet=" + charSet, e);
        } finally {
            get.releaseConnection();
        }
        return null;
    }
    
    /**
     * @author qingqiangli 2014/11/21
     * @param url
     * @return
     */
    public static byte[] getResponseBodyAsBytes(String url) {
    	long startTime = System.currentTimeMillis();
        GetMethod get = new GetMethod(url);
        get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        get.setRequestHeader("User-agent", CHROME_USER_AGENT);
        get.setRequestHeader("Connection", "Keep-Alive");
        try {
            client.executeMethod(get);
            byte[] bytes = get.getResponseBody();
            if(bytes != null && bytes.length > 0) {
            	logger.info("[getResponseBodyAsBytes] size" + bytes.length + ", url=" + url);
            }
            logger.info("[getResponseBodyAsBytes] costTime" + (System.currentTimeMillis()-startTime) + ", url=" + url);
            return bytes;
        } catch (Exception e) {
            logger.error("[getResponseBodyAsBytes] occur Exception url=" + url + "costTime" + (System.currentTimeMillis()-startTime), e);
        } finally {
            get.releaseConnection();
        }
        return null;
    }
    
    /**
     * @author gaojiechen 2013/12/16
     * @param url
     * @return
     */
    public static String getResponseBodyAsStringMulti(String url) {
        return getResponseBodyAsStringMulti(url, CONTENT_CHARSET);
    }

    /**
     * @author gaojiechen 2013/12/16
     * @param url
     * @param charSet
     * @return
     */
    public static String getResponseBodyAsStringMulti(String url, String charSet) {
        GetMethod get = new GetMethod(url);
        get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
        try {
        	HttpClient client = new HttpClient();
            client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charSet);
            client.executeMethod(get);
            return analyseResponse(get, charSet);
        } catch (Exception e) {
            logger.error("[getResponseBodyAsStringMulti] occur Exception url=" + url + " charSet=" + charSet, e);
        } finally {
            get.releaseConnection();
        }
        return null;
    }

    //获得请求结果
    private static String analyseResponse(HttpMethodBase method, String encoding) {
        try {
            // 检查是否是通过 gzip 压缩
            org.apache.commons.httpclient.Header header = method.getResponseHeader("Content-Encoding");
            if (header != null && header.getValue().toLowerCase().indexOf("gzip") > -1) {
                // 如果是gizp压缩，进行Gzip解压
                InputStream is = method.getResponseBodyAsStream();
                GZIPInputStream gzipInputStream = new GZIPInputStream(is);
                InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream, method.getResponseCharSet());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer gzipStrBuff = new StringBuffer();
                String tempbf;
                while ((tempbf = bufferedReader.readLine()) != null) {
                    gzipStrBuff.append(tempbf);
                    gzipStrBuff.append("\r\n");
                }
                inputStreamReader.close();
                gzipInputStream.close();
                return gzipStrBuff.toString();
            } else {
                // 如果不是gzip压缩，直接转
                if (StringUtils.isBlank(encoding)) encoding = CONTENT_CHARSET;
                return new String(method.getResponseBody(), encoding);
            }
        } catch (Exception e) {
            if (logger.isInfoEnabled()) {
                logger.error("ApacheHttpServiceImpl : analyse failed.", e);
            }
        }
        return null;
    }


    /**
     * 通过postResponseBodyByStream获取请求内容，推荐使用。
     * @param url
     * @param params
     * @return
     */
    public static String postResponseBodyByStream(String url, Map<String, String> params) {
        PostMethod post = new PostMethod(url);
        StringBuilder sb = new StringBuilder();
        if(params != null){
            Iterator<String> it = params.keySet().iterator();
            while(it.hasNext()){
                String key = it.next();
                String value = params.get(key);
                post.addParameter(key, value);
                sb.append(key).append("=").append(value).append("&");
            }
        }
        post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, CONTENT_CHARSET);
            client.executeMethod(post);

            inputStream = post.getResponseBodyAsStream();
            if(inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer res = new StringBuffer();
                String tmp = "";
                while((tmp = bufferedReader.readLine()) != null) {
                    res.append(tmp);
                }
                return res.toString();
            }
        } catch (Exception e) {
        	logger.error("postResponseBodyByStream error, url=" + url + ", params=" + params, e);
        } finally {
            try {
                if(bufferedReader != null) {
                	bufferedReader.close();
                }
                if(inputStream != null) {
                	inputStream.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
            post.releaseConnection();
        }
        return null;
    }
    
    
    /**
     * 
     * @param url
     * @param charset
     * @param poststrings
     *            a=a&b=b类型
     * @return
     */
    public static String postUrlContentWithTimeOut(String url, String charset, String[] poststrings,int timeOut) {
        // URL url1 = new URL(url);//internal site
        // InputStream inr = null;
        HttpURLConnection huc = null;
        long time = System.currentTimeMillis();
        try {
            URLConnection UrlConnObj1 = new URL(url).openConnection();
           
            huc = (HttpURLConnection) UrlConnObj1;
            huc.setConnectTimeout(timeOut);
            huc.setReadTimeout(timeOut * 60);
            huc.setRequestMethod("POST");
            huc.setDoOutput(true);
            huc.setDoInput(true);
            huc.setUseCaches(false);
            huc.setDefaultUseCaches(false);
            BufferedOutputStream out = new BufferedOutputStream(huc.getOutputStream());
            for (int i = 0; i < poststrings.length; i++) {
                out.write(poststrings[i].getBytes(charset));
                if (i < poststrings.length - 1)
                    out.write('&');
            }
            out.flush();
            out.close();

            InputStream inr = huc.getInputStream();
            char[] buf = new char[1000];
            int length = -1;
            // String result="";
            StringBuffer res = new StringBuffer();
            InputStreamReader reader = new InputStreamReader(inr, charset);
            while ((length = reader.read(buf)) != -1) {
                res.append(buf, 0, length);
            }
            reader.close();
            // inr.close();
            return res.toString();

        } catch (Exception e) {
            logger.error("[getUrlContent] occur Exception url=" + url + " charset=" + charset + " poststrings="
                    + poststrings + " time=" + (System.currentTimeMillis() - time), e);
            return null;
        } finally {
            if (huc != null)
                huc.disconnect();
        }

    }
}
