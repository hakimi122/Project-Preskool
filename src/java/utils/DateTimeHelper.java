
package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DateTimeHelper {

    public static Date toDate(String value, String format) throws ParseException {
        return new SimpleDateFormat(format).parse(value);
    }

    public static String dateFormat(java.sql.Date d, String format) {
        return new SimpleDateFormat(format).format(d);
    }

    public static String dateFormat2(java.util.Date d, String format) {
        return new SimpleDateFormat(format).format(d);
    }

    public static int getDayofWeek(java.util.Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

    public static Date addDays(java.util.Date d, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public static java.sql.Date currentDateToSat(java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        System.out.println("day of week" + dayOfWeek);
        cal.add(Calendar.DATE, Calendar.DAY_OF_WEEK - dayOfWeek);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date date2 = java.sql.Date.valueOf(formatter.format(cal.getTime()));
        return date2;
    }

    public static java.sql.Date sunToCurrentDate(java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, -(dayOfWeek) + 1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date date2 = java.sql.Date.valueOf(formatter.format(cal.getTime()));
        return date2;
    }

    public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static java.util.Date toDateUtil(java.sql.Date d) {
        java.util.Date x = new java.util.Date(d.getTime());
        x = removeTime(x);
        return x;
    }

    public static java.sql.Date toDateSql(java.util.Date d) {
        d = removeTime(d);
        return new java.sql.Date(d.getTime());
    }

    public static ArrayList<java.sql.Date>
            getDateList(java.sql.Date from, java.sql.Date to) {
        ArrayList<java.sql.Date> dates = new ArrayList<>();
        int days = 0;
        java.util.Date e_from = toDateUtil(from);
        java.util.Date e_to = toDateUtil(to);
        while (true) {
            java.util.Date d = DateTimeHelper.addDays(e_from, days);
            dates.add(toDateSql(d));
            days++;
            if (d.compareTo(e_to) >= 0) {
                break;
            }
        }
        return dates;
    }

    public static String getDayNameofWeek(java.sql.Date s) {
        java.util.Date d = toDateUtil(s);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
        }
        return "Error";
    }

    public static int compare(java.sql.Date a, java.sql.Date b) {
        Date e_a = toDateUtil(a);
        Date e_b = toDateUtil(b);
        return e_a.compareTo(e_b);
    }
}
