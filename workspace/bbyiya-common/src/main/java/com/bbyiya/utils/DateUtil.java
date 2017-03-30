package com.bbyiya.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

	/**
     *
     */
	public static String FORMAT_SHORT = "yyyy-MM-dd";

	/**
     *
     */
	public static String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";

	/**
     *
     */
	public static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S";

	/**
     * 
     */
	public static String FORMAT_SHORT_CN = "yyyy年MM月dd";

	/**
     * 
     */
	public static String FORMAT_LONG_CN = "yyyy年MM月dd日  HH时mm分ss秒";

	/**
     * 
     */
	public static String FORMAT_FULL_CN = "yyyy年MM月dd日  HH时mm分ss秒";
	
	private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

	/**
	 * 时间格式化
	 * 
	 * @param date
	 * @return
	 */

	public static String getTimeString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat(FORMAT_LONG);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return df.format(calendar.getTime());
	}

	/**
	 * 格式化成相应的时间格式
	 * 
	 * @param date
	 * @param Format
	 * @return
	 */
	public static String getTimeStr(Date date, String Format) {
		SimpleDateFormat df = new SimpleDateFormat(Format);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return df.format(calendar.getTime());
	}

	/**
   *
   */
	public static String getTimeString() {
		SimpleDateFormat df = new SimpleDateFormat(FORMAT_FULL);
		Calendar calendar = Calendar.getInstance();
		return df.format(calendar.getTime());
	}

	public static String getTimeString(String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		return df.format(calendar.getTime());
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	// public static String getYear(Date date) {
	// return format(date).substring(0, 4);
	// }
	/**
	 * 
	 *
	 * @param date
	 *            Date
	 * @return
	 */
	public static int getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 
	 *
	 * @param date
	 *            Date ����
	 * @return
	 */
	public static int getDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	public static String getShortDateString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat(FORMAT_SHORT);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return df.format(calendar.getTime());
	}

	public static String getHour(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String m = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
		if (m.length() <= 1) {
			m = "0" + m;
		}
		return m;
	}

	public static String getMinute(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String m = String.valueOf(calendar.get(Calendar.MINUTE));
		if (m.length() <= 1) {
			m = "0" + m;
		}
		return m;
	}

	public static int getSecond(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.SECOND);
	}

	public static long getMillis(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}

	/**
	 * 获取条件时间
	 * 
	 * @param date
	 * @param m
	 *            为正为后几天 负为前面几天
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static Date getDate(Date date, int m) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, m);
		return calendar.getTime();
	}

	public static Date getDateByString(String format, String date) {
		DateFormat fmt = new SimpleDateFormat(format);
		Date returnDate = null;
		try {
			returnDate = fmt.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnDate;
	}

	/**
	 * 获得本周一0点时间
	 * 
	 * @return
	 */
	public static Date getTimesWeekmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return cal.getTime();
	}

	/**
	 * 获得本周日24点时间
	 * 
	 * @return
	 */
	public static Date getTimesWeeknight() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getTimesWeekmorning());
		cal.add(Calendar.DAY_OF_WEEK, 7);
		return cal.getTime();
	}

	/**
	 * 获得本月第一天0点时间
	 * 
	 * @return
	 */
	public static Date getTimesMonthmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	/**
	 * 获得本月最后一天24点时间
	 * 
	 * @return
	 */
	public static Date getTimesMonthnight() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 24);
		return cal.getTime();
	}

	/**
	 * 获得当天0点时间
	 * 
	 * @return
	 */
	public static Date getTimesmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();

	}

	/**
	 * 获得昨天0点时间
	 * 
	 * @return
	 */
	public static Date getYesterdaymorning() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(getTimesmorning().getTime() - 3600 * 24 * 1000);
		return cal.getTime();
	}

	public static boolean isDate(String strDate) {
		Pattern pattern = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
		Matcher m = pattern.matcher(strDate);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 字符串的日期格式的计算
	 */
	public static int daysBetween(String smdate, String bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}
	/**
	 * 获取日期的最后结束时间
	 * @param endTimeStr
	 * @return
	 * @throws ParseException 
	 */
	public static String getEndTime(String endTimeStr){
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.setTime(stringToDate(endTimeStr,new SimpleDateFormat("yyyy-MM-dd")));
		todayEnd.set(Calendar.HOUR,23);
		todayEnd.set(Calendar.MINUTE,59);
		todayEnd.set(Calendar.SECOND,59);
		todayEnd.set(Calendar.MILLISECOND,999);
		return DateUtil.getTimeString(todayEnd.getTime());
	}
	
	/**
	* 把指定的日期格式的字符串转换成Date类型
	* 
	* @author：tuzongxun
	* @Title: StringToDate
	* @param @param string
	* @return void
	* @date May 3, 2016 9:16:38 AM
	* @throws
	*/
	public static Date stringToDate(String string,SimpleDateFormat format ) {
		Date date = new Date();
		try {
			date = format.parse(string);	
		} catch (Exception e) {
			e.getStackTrace();
		}
		return date;
	}
	
	
 
    /**
     * 返回文字描述的日期
     * 
     * @param date
     * @return
     */
    public static String getTimeFormatText(Date date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }
	
}
