/*
 *  Created by: HieuPV
 *  Mail: hieupv@mpos.vn
 */

package vn.nextpay.nextshop.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    public static DateTimeFormatter ddMMyyyyFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static DateTimeFormatter yyyyMMddFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final String strFormat = "dd-MM-yyyy HH:mm:ss.SSS";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
    private static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS");

    public DateTimeUtil() {
    }

    public static String toDDMMYYYYString(ZonedDateTime time) {
        return time == null ? null : time.format(ddMMyyyyFormatter);
    }

    public static String toYYYYMMDDString(ZonedDateTime time) {
        return time == null ? null : time.format(yyyyMMddFormatter);
    }

    public static Date parseISODate(String text) throws ParseException {
        return sdf.parse(text);
    }

    public static ZonedDateTime currentUTCTime() {
        return Instant.now().atZone(ZoneOffset.UTC);
    }

    public static ZonedDateTime currentVNTime() {
        return Instant.now().atZone(ZoneId.of("+07:00"));
    }

    public static String date2String(Date date) {
        return simpleDateFormat.format(date);
    }

    public static String zoneDate2String(ZonedDateTime date) {
        return date.format(DATE_TIME_FORMATTER);
    }

    public static ZonedDateTime string2Date(String str) {
        return ZonedDateTime.parse(str);
    }

    public static ZonedDateTime now() {
        ZoneId zoneId = ZoneOffset.UTC;
        return ZonedDateTime.now(zoneId);
    }

    public static long nowMillisecond() {
        ZoneId zoneId = ZoneOffset.UTC;
        return ZonedDateTime.now(zoneId).toInstant().toEpochMilli();
    }

    public static long getDuration(ZonedDateTime d1, ZonedDateTime d2) {
        return d1.toInstant().toEpochMilli() - d2.toInstant().toEpochMilli();
    }

    public static ZonedDateTime minisecondToDate(long mini) {
        Instant instant = Instant.ofEpochMilli(mini);
        return ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}

