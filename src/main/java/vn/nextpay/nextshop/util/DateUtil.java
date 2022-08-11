package vn.nextpay.nextshop.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class DateUtil {

    public static ZonedDateTime now() {
        ZoneId zoneId = ZoneOffset.UTC;
        return ZonedDateTime.now(zoneId);
    }

    public static ZonedDateTime millisecondToDate(long mini) {
        Instant instant = Instant.ofEpochMilli(mini);
        return ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    public static Long zonedDateTimeToMillisecond(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toInstant().toEpochMilli();
    }
}
