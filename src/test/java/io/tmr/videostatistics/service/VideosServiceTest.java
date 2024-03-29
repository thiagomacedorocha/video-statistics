package io.tmr.videostatistics.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.tmr.videostatistics.exception.InvalidInputData;
import io.tmr.videostatistics.model.Video;
import io.tmr.videostatistics.repository.StatisticsRepositoryImpl;
import io.tmr.videostatistics.utils.DateUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
	VideosServiceImpl.class, StatisticsRepositoryImpl.class
})
public class VideosServiceTest {

	@Autowired
	private VideosService videosService;

	@Test
	public void test_insertVideo_timestamp_lessThan60s() {
		LocalDateTime testDate = DateUtils.now().minusSeconds(60);

		// @formatter:off
		videosService.insertVideo(Video.builder()
										.timestamp(DateUtils.localDateTimeToTimestampMillisecondsUTC(testDate))
										.duration(BigDecimal.valueOf(200.3))
										.build());
		// @formatter:on
	}

	@Test
	public void test_insertVideo_timestamp_moreThan60s() {
		LocalDateTime testDate = DateUtils.now().minusSeconds(61);

		// @formatter:off
		assertThatThrownBy(() -> videosService.insertVideo(Video.builder()
										.timestamp(DateUtils.localDateTimeToTimestampMillisecondsUTC(testDate))
										.duration(BigDecimal.valueOf(200.3))
										.build()))
							.isInstanceOf(InvalidInputData.class);
		// @formatter:on
	}

	@Test
	public void test_insertVideo_timestamp_Now() {
		LocalDateTime testDate = DateUtils.now();

		// @formatter:off
		videosService.insertVideo(Video.builder()
										.timestamp(DateUtils.localDateTimeToTimestampMillisecondsUTC(testDate))
										.duration(BigDecimal.valueOf(200.3))
										.build());
		// @formatter:on
	}

	@Test
	public void test_insertVideo_timestamp_inTheFuture() {
		LocalDateTime testDate = DateUtils.now().plusSeconds(1);

		// @formatter:off
		assertThatThrownBy(() -> videosService.insertVideo(Video.builder()
										.timestamp(DateUtils.localDateTimeToTimestampMillisecondsUTC(testDate))
										.duration(BigDecimal.valueOf(200.3))
										.build()))
							.isInstanceOf(InvalidInputData.class);
		// @formatter:on
	}

	@Test
	public void test_insertVideo_duration_MinimusValue() {
		LocalDateTime testDate = DateUtils.now();

		// @formatter:off
		videosService.insertVideo(Video.builder()
										.timestamp(DateUtils.localDateTimeToTimestampMillisecondsUTC(testDate))
										.duration(BigDecimal.valueOf(0.0001))
										.build());
		// @formatter:on
	}

	@Test
	public void test_insertVideo_duration_Negative() {
		LocalDateTime testDate = DateUtils.now();

		// @formatter:off
		assertThatThrownBy(() -> videosService.insertVideo(Video.builder()
										.timestamp(DateUtils.localDateTimeToTimestampMillisecondsUTC(testDate))
										.duration(BigDecimal.valueOf(0.0))
										.build()))
							.isInstanceOf(InvalidInputData.class);
		// @formatter:on
	}

	@Test
	public void test_insertVideo_duration_Zero() {
		LocalDateTime testDate = DateUtils.now();

		// @formatter:off
		assertThatThrownBy(() -> videosService.insertVideo(Video.builder()
										.timestamp(DateUtils.localDateTimeToTimestampMillisecondsUTC(testDate))
										.duration(BigDecimal.valueOf(-0.3))
										.build()))
							.isInstanceOf(InvalidInputData.class);
		// @formatter:on
	}

}
