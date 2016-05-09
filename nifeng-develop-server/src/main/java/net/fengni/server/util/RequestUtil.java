package net.fengni.server.util;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class RequestUtil {
	public static Map<String, String> getRequestHeaders(HttpServletRequest request) {
		HashMap<String, String> res = new HashMap<String, String>();
		Enumeration<String> emu = request.getHeaderNames();
		while (emu.hasMoreElements()) {
			String key = emu.nextElement();
			String value = request.getHeader(key);
			res.put(key, value);
		}
		return res;
	}

	public static String getSessionString(HttpServletRequest request, final String name, String defaultValue) {
		String str = defaultValue;
		Object value = request.getSession().getAttribute(name);
		if (value != null) {
			str = (String) value;
		}
		return str;
	}

	// public static String getSessionVerifyCode(HttpServletRequest request,
	// final String name,String defaultValue) {
	// String code = getSessionString(request, name, defaultValue);
	// request.getSession().removeAttribute(name);
	// return code;
	// }

	public static String getRequestString(HttpServletRequest request, final String name) {
		return getRequestString(request, name, null);
	}

	public static String getRequestString(HttpServletRequest request, final String name, final String defaultValue) {
		String value = request.getParameter(name);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

	public static int getRequestInt(HttpServletRequest request, final String name) {
		return getRequestInt(request, name, 0);
	}

	public static int getRequestInt(HttpServletRequest request, final String name, int defaultValue) {
		String value = getRequestString(request, name, null);
		if (value == null)
			return defaultValue;

		try {
			return NumberUtils.toInt(value.trim(), defaultValue);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static int getRequestFromModel(HttpServletRequest request, final String name, int defaultValue) {
		String value = getRequestString(request, name, null);
		if (value == null)
			return defaultValue;

		try {
			return NumberUtils.toInt(value.trim(),defaultValue);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static long getRequestLong(HttpServletRequest request, final String name) {
		return getRequestLong(request, name, 0);
	}

	public static long getRequestLong(HttpServletRequest request, final String name, long defaultValue) {
		String value = getRequestString(request, name, "");
		return ParseUtil.paseLong(value, defaultValue);
	}

	public static String getRemoteIpAddress(HttpServletRequest request) {
		String rip = request.getRemoteAddr();
		String xff = request.getHeader("X-Forwarded-For");
		String ip;
		if (xff != null && xff.length() != 0) {
			int px = xff.indexOf(',');
			if (px != -1) {
				ip = xff.substring(0, px);
			} else {
				ip = xff;
			}
		} else {
			ip = rip;
		}
		return ip.trim();
	}

	/**
	 * 获取用户地区编码（基于国标）
	 * 
	 * @param request
	 * @return
	 */
	public static String getIploc(HttpServletRequest request) {
		final String DEFAULT_LOCAL = "CN0000";
		String iploc = request.getHeader("cmccip");
		if (iploc == null || iploc.length() == 0 || "unknown".equalsIgnoreCase(iploc)) {
			iploc = DEFAULT_LOCAL;
		}
		return iploc;
	}
	
	public static String getGbgodeFromIploc(HttpServletRequest request){
	    String iploc = getIploc(request);
        if (StringUtils.indexOf(iploc, "CN") != -1) {
            iploc = StringUtils.substring(iploc, 2);
            if (iploc.length() > 4) {
                iploc = StringUtils.substring(iploc, 0, 4);
            }
        }
        if("0000".equalsIgnoreCase(iploc)){
            return null;
        }
        return iploc;
	}

	/**
	 *  获取客户端ip地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-source-id");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		return ip;
	}
	
	public static String getCookieValue(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(int i = 0; i < cookies.length; i++) {
				if(cookies[i].getName().equalsIgnoreCase(name)) {
					String value = cookies[i].getValue();
					return value;
				}
			}
		}
		return null;
	}
	
	/*
	 * 通过request中的指定header name获取userId（int）
	 */
	public static String getUserIdByRequestHeaderName(HttpServletRequest request){
		System.out.println("X-SohuPassport-UserId	:   "+request.getHeader("X-SohuPassport-UserId"));
		return request.getHeader("X-SohuPassport-UserId");
	}
	
	
}
