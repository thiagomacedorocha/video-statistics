package io.tmr.videostatistics.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.Test;

public class DateUtilsTest {

	@Test
	public void test_convertTimestampToLocalDateTime() {
		long timestamp = 1478192204000L;
		LocalDateTime dateTime = DateUtils.convertTimestampToLocalDateTimeUTC(timestamp);

		assertThat(dateTime).isNotNull();
		assertThat(dateTime.getDayOfMonth()).isEqualTo(3);
		assertThat(dateTime.getMonthValue()).isEqualTo(11);
		assertThat(dateTime.getYear()).isEqualTo(2016);
		assertThat(dateTime.getHour()).isEqualTo(16);
		assertThat(dateTime.getMinute()).isEqualTo(56);
		assertThat(dateTime.getSecond()).isEqualTo(44);
	}

}
