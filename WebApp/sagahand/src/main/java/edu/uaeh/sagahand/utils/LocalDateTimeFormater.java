package edu.uaeh.sagahand.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeFormater {
	private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a");

	public static String dateFormat(LocalDate localDate) {
		return dateFormat.format(localDate);
	}
	
	public static String dateTimeFormat(LocalDateTime localDateTime) {
		return dateTimeFormat.format(localDateTime);
	}
}
