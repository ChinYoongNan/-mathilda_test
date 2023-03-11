package com.caam.mrs.api.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	/**
     * Compare 2 dates using seconds precision.
     * Useful for JPA equality concern when comparing Date from Java world (with millisec precision) with date
     * retrieved from Oracle (second precision only).
     */
    public static boolean equalsWithSecondPrecision(Date d1, Date d2) {
        String s1 = Long.toString(d1.getTime());
        String s2 = Long.toString(d2.getTime());
        s1 = s1.substring(0, s1.length() - 3);
        s2 = s2.substring(0, s2.length() - 3);
        return s1.equals(s2);
    }

    public static int hashCodeWithSecondPrecision(Date d1) {
        String s1 = Long.toString(d1.getTime());
        return s1.substring(0, s1.length() - 3).hashCode();
    }
    
    /**
	   * Convert the date in string to sql date.
	   *
	   * @param date
	   * @return
	   * @throws ParseException
	   */
	  public static Date convertStringToDate(String date)
	          throws Exception {
	      try {
	          DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	          dateFormat.parse(date);
	          Calendar cal2 = dateFormat.getCalendar();
	          return new Date(cal2.getTimeInMillis());
	      }
	      catch (Exception e) {
	          return null;
	      }
	  }
	  
	  public static Date convertStringToDate(String date, String format)
			  throws Exception {
		  try {
		      DateFormat dateFormat = new SimpleDateFormat(format);
		      dateFormat.parse(date);
		      Calendar cal2 = dateFormat.getCalendar();
		      return new Date(cal2.getTimeInMillis());
		  }
		  catch (Exception e) {
		      return null;
		  }
	  }
	  
	  /**
	   * Convert the date in sql date to string
	   *
	   * @param date
	   * @return date in string
	   */
	  public static String convertDateToString(Date date) {
	      try {
	          DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	          return dateFormat.format(date);
	      }
	      catch (Exception e) {
	          return "";
	      }
	  }
	  
	  /**
	   * Convert the date in sql date to string
	   *
	   * @param date
	   * @return date in string
	   */
	  public static String convertDateToString(Date date, String format) {
	      try {
	          DateFormat dateFormat = new SimpleDateFormat(format);
	          return dateFormat.format(date);
	      }
	      catch (Exception e) {
	          return "";
	      }
	  }
	  
	  /**
	   * Convert the date in LocalDateTime to string
	   *
	   * @param date
	   * @return date in string
	   */
	  public static String convertLocalDateTimeToString(LocalDateTime date, String format) {
		  try {
			  DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
	          return date.format(formatter);
	      }
	      catch (Exception e) {
	          return "";
	      }
	  }
	  
	  public static String getMonthName(Integer mth) {
			String mthName = "";
			switch (mth) {
				case 1 : mthName = "JANUARY";
				 break;
				case 2 : mthName = "FEBRUARY";
				 break;
				case 3 : mthName = "MARCH";
				 break;
				case 4 : mthName = "APRIL";
				 break;
				case 5 : mthName = "MAY";
				 break;
				case 6 : mthName = "JUNE";
				 break;
				case 7 : mthName = "JULY";
				 break;
				case 8 : mthName = "AUGUST";
				 break;
				case 9 : mthName = "SEPTEMBER";
				 break;
				case 10 : mthName = "OCTOBER";
				 break;
				case 11 : mthName = "NOVEMBER";
				 break;
				case 12 : mthName = "DECEMBER";
				 break; 
			}
			
			return mthName;
		}
		
		public static String getMonthShort(Integer mth) {
			String mthName = "";
			switch (mth) {
				case 1 : mthName = "JAN";
				 break;
				case 2 : mthName = "FEB";
				 break;
				case 3 : mthName = "MAR";
				 break;
				case 4 : mthName = "APR";
				 break;
				case 5 : mthName = "MAY";
				 break;
				case 6 : mthName = "JUN";
				 break;
				case 7 : mthName = "JUL";
				 break;
				case 8 : mthName = "AUG";
				 break;
				case 9 : mthName = "SEP";
				 break;
				case 10 : mthName = "OCT";
				 break;
				case 11 : mthName = "NOV";
				 break;
				case 12 : mthName = "DEC";
				 break; 
			}
			
			return mthName;
		}
		
		public static String ordinal(int i) {
		    String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
		    switch (i % 100) {
		    case 11:
		    case 12:
		    case 13:
		        return i + "th";
		    default:
		        return i + sufixes[i % 10];

		    }
		}
		
		public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
			return dateToConvert.toInstant()
					.atZone(ZoneId.systemDefault())
					.toLocalDate();
		}
		
		public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
			return dateToConvert.toInstant()
					.atZone(ZoneId.systemDefault())
					.toLocalDateTime();
		}
		
		public static LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
			return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
		}
		
		public static LocalDateTime convertToLocalDateTimeViaSqlTimestamp(Date dateToConvert) {
			return new java.sql.Timestamp(dateToConvert.getTime()).toLocalDateTime();
		}
		
		public static String calculateDateDiff(Timestamp dateTimeStart, Timestamp dateTimeEnd) {
			String duration = "";
			
			LocalDateTime fromDateTime = convertToLocalDateTimeViaSqlTimestamp(dateTimeStart);
			LocalDateTime toDateTime = convertToLocalDateTimeViaSqlTimestamp(dateTimeEnd);

			LocalDateTime tempDateTime = LocalDateTime.from( fromDateTime );

			long years = tempDateTime.until( toDateTime, ChronoUnit.YEARS);
			tempDateTime = tempDateTime.plusYears( years );

			long months = tempDateTime.until( toDateTime, ChronoUnit.MONTHS);
			tempDateTime = tempDateTime.plusMonths( months );

			long days = tempDateTime.until( toDateTime, ChronoUnit.DAYS);
			tempDateTime = tempDateTime.plusDays( days );


			long hours = tempDateTime.until( toDateTime, ChronoUnit.HOURS);
			tempDateTime = tempDateTime.plusHours( hours );

			long minutes = tempDateTime.until( toDateTime, ChronoUnit.MINUTES);
			tempDateTime = tempDateTime.plusMinutes( minutes );

//			long seconds = tempDateTime.until( toDateTime, ChronoUnit.SECONDS);

			if (years > 0)
				duration += years + " years ";
			
			if (months > 0)
				duration += months + " months ";
			
			if (days > 0)
				duration += days + " days ";
			
			if (hours > 0)
				duration += hours + " hours ";
			
			if (minutes > 0)
				duration += minutes + " minutes ";
						
//			System.out.println( years + " years " + 
//			        months + " months " + 
//			        days + " days " +
//			        hours + " hours " +
//			        minutes + " minutes " +
//			        seconds + " seconds.");
			
			return duration;
		}

}
