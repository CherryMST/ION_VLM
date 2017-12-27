package com.qx.framelib.utlis;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtils {


	public final static long SECOND = 1000;

	public final static long MINUTE = 60 * SECOND;

	public final static long HOUR = 60 * MINUTE;

	public final static long DAY = 24 * HOUR;

	/**
	 * 获取当前时间，返回yyyy-MM-dd HH:mm:ss格式
	 *
	 * @return
	 */
	public static String getNowTime(){
		return getDateandSecondFromMillisecond(System.currentTimeMillis());
	}

	/**
	 * 获取当前时间，返回yyyy-MM-dd HH:mm:ss.SSS格式
	 *
	 * @return
	 */
	public static String getNowTimeMills(){
		try
		{
			return getDateandMillisecondFromMillisecond(System.currentTimeMillis());
		}
		catch (Exception ex){
			return "";
		}
	}

	/**
	 * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14  16:09:00"）
	 *
	 * @param time
	 * @return
	 */
	public static String timedate(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		@SuppressWarnings("unused")
		long lcc = Long.valueOf(time);
		int i = Integer.parseInt(time);
		String times = sdr.format(new Date(i * 1000L));
		return times;

	}


	/**
	 *
	 * 将毫秒换为年月日 时分秒
	 *
	 * @param time
	 * @return
	 */

	public static String getDateandSecondFromMillisecond(Long time)
	{
		if (null == time)
		{
			return null;
		}
		final SimpleDateFormat df = yyyy_MM_dd_HH_mm_ssTimeFormat.get();
		return df.format(time);
	}


	/**
	 *
	 * 将毫秒换为年月日 时分秒
	 *
	 * @param time
	 * @return
	 */

	public static String getDateandMillisecondFromMillisecond(Long time)
	{
		if (null == time)
		{
			return null;
		}
		final SimpleDateFormat df = yyyy_MM_dd_HH_mm_ssSSSTimeFormat.get();
		return df.format(time);
	}



	/*
	 * 将毫秒换算为年月日
	 */
	public static String getDateFromMillisecond(Long time)
	{
		if (null == time)
		{
			return null;
		}
		final SimpleDateFormat df = yyyy_MM_ddTimeFormat.get();
		return df.format(time);
	}

	public static String getDateFromMillisecondMD(Long time)
	{
		if (null == time)
		{
			return null;
		}
		final SimpleDateFormat df = MM_ddTimeFormat.get();
		return df.format(time);
	}

	/**
	 * 判断是否为同一天
	 * @param time 支持非实时获取的时间
	 * @return
	 * */
	public static boolean isInToday(long time) {
		long todayTime = getTruncateTimeToday();
		//XLog.d("MyTime", "time:" + time + " " + todayTime + " " + (time - todayTime) + " " + getHMSDataFromMillisecond(time - todayTime));
		if (time - todayTime >= 0 && time - todayTime < 24 * 60 * 60 * 1000) {
			return true;
		}
		return false;
	}

	/**
	 * 判断该时间是否在这个星期内
	 * @param time
	 * @return
	 */
	public static boolean isInThisWeek(long time) {
		if(getDayFromCurrent(time) < 7 ) {
			return true;
		}
		return false;
	}

	/**
	 * 获取当前属于今年的第几周,每周的起始位周一到周日
	 * */
	public static int getWeekOfYear() {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTimeInMillis(System.currentTimeMillis());

		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 是否是相同的第几周，{@link #isInThisWeek} 是判断相隔是否为一周
	 * */
	public static boolean isEqualWeekOfYear(int weekOfYear) {
		return getWeekOfYear() == weekOfYear;
	}

	/**
	 * 判断该时间是否在制定的周期内
	 * @param time
	 * @param period
	 * @return
	 */
	public static boolean isInThePeriod(long time, int period) {
		if(getDayFromCurrent(time) < period ) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否在一个月以前
	 * @param time
	 * @return
	 */
	public static boolean beforeAMonth(long time) {
		if(getDayFromCurrent(time) > 30) {
			return true;
		}
		return false;
	}

	/**
	 * 得到与time距离的时间，因为getTruncateTimeToday()比较耗时，为大量调用这个方法的函数做优化
	 * 0为今天以内，n 大于 0 即表示n天前
	 * @param time
	 * @param todayTime
	 * @return
	 */
	public static int getDayFromCurrent(long time,long todayTime) {
		if(time - todayTime > 0) {
			return 0;
		}

		long dayTime = 1000 * 60 * 60 * 24;
		long deltaTime = todayTime - time;
		return (int)(deltaTime/(float)dayTime) + 1;
	}

	/**
	 * 得到与time距离的时间
	 * 0为今天以内，n 大于 0 即表示n天前
	 * @param time
	 * @return
	 */
	public static int getDayFromCurrent(long time) {
		long todayTime = getTruncateTimeToday();
		if(time - todayTime > 0) {
			return 0;
		}

		long dayTime = 1000 * 60 * 60 * 24;
		long deltaTime = todayTime - time;
		return (int)(deltaTime/(float)dayTime) + 1;
	}

	/**
	 * get current day
	 * formate is yyyyMMdd
	 * @return
	 */
	public static String getCurrentDay() {
		Date dt = new Date();
		SimpleDateFormat df = yyyyMMddTimeFormat.get();
		String timestamp = df.format(dt);
		return timestamp;
	}

	public static String getCurrentDayLogTest() {
		return "launche_time_log_" + TimeUtils.getCurrentDay() + ".txt";
	}

	/**
	 * 得到今天0点0时0分0秒0毫秒的时间值
	 * @return
	 */
	public static long getTruncateTimeToday() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime().getTime();
	}

	public static long getTimeBeforeDays(int days) {
		long currentTime = System.currentTimeMillis();
		long resultTime = currentTime - 1000l * 60 * 60 *24 * days;
		if(resultTime < 0) {
			return 0;
		}
		return resultTime;
	}


//	/**
//	 * 日期格式化。今天的显示格式
//	 */
//	private static ThreadLocal<SimpleDateFormat> todayTimeFormat =new ThreadLocal<SimpleDateFormat>(){
//		@Override
//		protected SimpleDateFormat initialValue()
//		{
//			return new SimpleDateFormat("今天 HH:mm");
//		}
//	};

	private static ThreadLocal<SimpleDateFormat> currentYearTimeFormat =new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("MM月dd日");
		}
	};

	private static ThreadLocal<SimpleDateFormat> otherYearTimeFormat =new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("yy年MM月dd日");
		}
	};

	private static ThreadLocal<SimpleDateFormat> yyyyMMddTimeFormat =new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("yyyyMMdd");
		}
	};

	private static ThreadLocal<SimpleDateFormat> yyyy_MM_ddTimeFormat =new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	private static ThreadLocal<SimpleDateFormat> MM_ddTimeFormat =new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("MM-dd");
		}
	};

	private static ThreadLocal<SimpleDateFormat> yyyy_MM_dd_HH_mm_ssTimeFormat =new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private static ThreadLocal<SimpleDateFormat> yyyy_MM_dd_HH_mm_ssSSSTimeFormat =new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		}
	};


	/** 获取time与当前时间相隔的天数
	 * @param time
	 * @return
	 */
	public static long getDifferDay(long time) {
		long currentTime = System.currentTimeMillis();
		long differ = currentTime - time;
		return differ/(1000*60*60*24);
	}


	public static int getYear(String time){
		if (!TextUtil.isEmpty(time)){
			String year=time.substring(0, 4);
			return Integer.parseInt(year);
		}
		return 0;

	}

	public static  int getMonth(String time){
		if (!TextUtil.isEmpty(time)){
			String month=time.substring(5, 7);
			return Integer.parseInt(month)-1;
		}
		return 0;
	}

	public  static int getDay(String time){
		if (!TextUtil.isEmpty(time)){
			String day=time.substring(8, time.length());
			return Integer.parseInt(day);
		}
		return 0;
	}



}
