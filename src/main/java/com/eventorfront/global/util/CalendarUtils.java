package com.eventorfront.global.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

	private CalendarUtils() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	// 오늘 00:00 반환 (String)
	public static String getDate() {
		Calendar calendar = getMidnightCalendar();
		return formatDate(calendar.getTime());
	}

	// N일 후 00:00 반환 (String)
	public static String getPlusDate(int amount) {
		return formatDate(getPlusDateAsDate(amount));
	}

	// 오늘 00:00 반환 (Date)
	public static Date getDateAsDate() {
		return getPlusDateAsDate(0);
	}

	// N일 후 1분전 오후 11:59 반환 (Date)
	public static Date getPlusDateAsDate(int amount) {
		Calendar calendar = getMidnightCalendar();
		calendar.add(Calendar.DAY_OF_YEAR, amount);
		calendar.add(Calendar.MINUTE, -1);
		return calendar.getTime();
	}

	// 00:00으로 초기화된 Calendar 객체 반환
	private static Calendar getMidnightCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

	// Date → "yyyy-MM-dd'T'HH:mm" 포맷으로 변환
	private static String formatDate(Date date) {
		return SDF.format(date);
	}
}
