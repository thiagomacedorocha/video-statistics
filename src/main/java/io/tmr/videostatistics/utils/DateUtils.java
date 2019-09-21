package io.tmr.videostatistics.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DateUtils {

	public static LocalDateTime convertTimestampToLocalDateTimeUTC(long timestampMilliseconds) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestampMilliseconds), ZoneOffset.UTC);
	}

}
