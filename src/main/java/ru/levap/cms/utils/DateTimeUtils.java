package ru.levap.cms.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private static DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
	private static DateTimeFormatter formatterDateTimeWithTimezone = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm Z");
	private static DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
	
	private DateTimeUtils() {
	}
	
	public static DatePeriod getPeriodFromString(String from, String to) {
		LocalDateTime fromDay = LocalDate.parse(from, formatter).atStartOfDay();
		LocalDateTime toDay = LocalDate.parse(to, formatter).plusDays(1).atStartOfDay();
		return new DatePeriod(fromDay.toEpochSecond(ZoneOffset.UTC), toDay.toEpochSecond(ZoneOffset.UTC));
	}
	
	public static Long getLongTimeNow() {
		LocalDateTime timeNow = LocalDateTime.now();
		return timeNow.toEpochSecond(ZoneOffset.UTC);
	}
	
	public static String convertDateLongToString(Long date, Integer timezone) {
		if(timezone == null) {
			timezone = 0;
		}
		if(date == null) {
			return "-";
		}
		LocalDateTime timeNowDay = LocalDateTime.ofEpochSecond(date, 0, ZoneOffset.ofHours(timezone));
		return timeNowDay.format(formatter);
	}
	
	public static String convertDateTimeLongToString(Long date, Integer timezone) {
		if(timezone == null) {
			timezone = 0;
		}
		if(date == null) {
			return "-";
		}
		LocalDateTime timeNowDay = LocalDateTime.ofEpochSecond(date, 0, ZoneOffset.ofHours(timezone));
		return timeNowDay.format(formatterDateTime);
	}
	
	public static String convertDateTimeLongToStringWithTimezone(Long date, Integer timezone) {
		if(timezone == null) {
			timezone = 0;
		}
		if(date == null) {
			return "-";
		}
		LocalDateTime timeNowDay = LocalDateTime.ofEpochSecond(date, 0, ZoneOffset.ofHours(timezone));
		ZonedDateTime zoned = ZonedDateTime.of(timeNowDay, ZoneId.ofOffset("UTC", ZoneOffset.ofHours(timezone)));

		return zoned.format(formatterDateTimeWithTimezone);
	}
	
	public static String convertTimeLongToString(Long date, Integer timezone) {
		if(timezone == null) {
			timezone = 0;
		}
		if(date == null) {
			return "-";
		}
		LocalDateTime timeNowDay = LocalDateTime.ofEpochSecond(date, 0, ZoneOffset.ofHours(timezone));
		return timeNowDay.format(formatterTime);
	}
	
	public static Long convertStringDateToLong(String dateTime, Integer timezone) {
		if(timezone == null) {
			timezone = 0;
		}
		LocalDateTime timeNowDay = LocalDate.parse(dateTime, formatter).atStartOfDay();
		return timeNowDay.toEpochSecond(ZoneOffset.ofHours(timezone));
	}
	
	public static Long convertStringDateTimeToLong(String dateTime, Integer timezone) {
		if(timezone == null) {
			timezone = 0;
		}
		LocalDateTime timeNowDay = LocalDateTime.parse(dateTime, formatterDateTime);
		return timeNowDay.toEpochSecond(ZoneOffset.ofHours(timezone));
	}
}
