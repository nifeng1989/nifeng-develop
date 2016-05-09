package net.fengni.server.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParseUtil {

	public static int paseInt(String s,int defaultV){
		int r = defaultV;
		try{
			r = Integer.parseInt(s);
		}catch(Exception e){}
		return r;
	}

	public static long paseLong(String s,long defaultV){
		long r = defaultV;
		try{
			r = Long.parseLong(s);
		}catch(Exception e){}
		return r;
	}
	
	public static float parseFloat(String s, float defaultV){
		float r = defaultV;
		try{
			r = Float.parseFloat(s);
		}catch(Exception e){}
		return r;
	}
	
	public static String parseStrTemp(String str){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
		String date = sdf.format(new Date(System.currentTimeMillis()));
		String[] dateInfo = date.split("-");
		str = str.replace("{yyyy}", dateInfo[0])
				.replace("{mm}", dateInfo[1])
				.replace("{dd}", dateInfo[2]);
		return str;
	}
	
	
	public static String formatDate(Date date,String formatStr){
		SimpleDateFormat sdf=new SimpleDateFormat(formatStr);
		return sdf.format(date);
	}
	
	public static int[] parseIntArray(String[] s, int defaultValue){
		if(s == null){
			return null;
		}
		int[] x_ret = new int[s.length];
		for(int i = 0; i < s.length; i ++){
			x_ret[i] = paseInt(s[i], defaultValue);
		}
		return x_ret;
	}
	
	public static String parseDoubleToPercent(double d){
		NumberFormat percentFormat = NumberFormat.getPercentInstance();
		percentFormat.setMaximumFractionDigits(2); //最大小数位数
		percentFormat.setMaximumIntegerDigits(3);//最大整数位数
		percentFormat.setMinimumFractionDigits(2); //最小小数位数
		percentFormat.setMinimumIntegerDigits(1);//最小整数位数
		String percent = percentFormat.format(d);//自动转换成百分比显示..
		return percent;
	}
	
	public static void main(String[] args) {
		double d = (double)12/100;
		System.out.println(ParseUtil.parseDoubleToPercent(d));
	}
}
