package io.tmr.videostatistics.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import io.tmr.videostatistics.dto.InsertVideoRequest;
import io.tmr.videostatistics.dto.StatisticsResponse;
import io.tmr.videostatistics.repository.StatisticsRepositoryImpl;
import io.tmr.videostatistics.utils.DateUtils;

public class VideosServiceStatisticsTest {

	private static final Random random = new Random();

	private VideosService videosService;

	public VideosServiceStatisticsTest() {
		this.videosService = new VideosServiceImpl(new StatisticsRepositoryImpl());
	}

	/**
	 * This test is important to validate the double precision error. That's why BigDecimal replaces the double type for
	 * statistics properties. Post that describe the problem:
	 * https://stackoverflow.com/questions/10786587/java-double-precision-sum-trouble
	 */
	@Test
	public void test_statistics_detect_precision_error() {
		loadVideos();
		StatisticsResponse statistics = videosService.statistics();

		BigDecimal expectedSum = BigDecimal.valueOf(25.49);
		BigDecimal expectedMax = BigDecimal.valueOf(8.11);
		BigDecimal expectedMin = BigDecimal.valueOf(2.62);
		BigDecimal expectedAvg = BigDecimal.valueOf(6.3725);

		assertThat(statistics).isNotNull();
		assertThat(statistics.getSum()).isEqualTo(expectedSum);
		assertThat(statistics.getCount()).isEqualTo(4L);
		assertThat(statistics.getMax()).isEqualTo(expectedMax);
		assertThat(statistics.getMin()).isEqualTo(expectedMin);
		assertThat(statistics.getAvg()).isEqualTo(expectedAvg);

		videosService.deleteAllVideos();
	}

	private void loadVideos() {
		LocalDateTime testDate = DateUtils.now();

		long timestamp1 = DateUtils.localDateTimeToTimestampMillisecondsUTC(testDate.minusSeconds(20));
		InsertVideoRequest video1 = new InsertVideoRequest(BigDecimal.valueOf(7.7), timestamp1);
		InsertVideoRequest video2 = new InsertVideoRequest(BigDecimal.valueOf(7.06), timestamp1);

		long timestamp2 = DateUtils.localDateTimeToTimestampMillisecondsUTC(testDate.minusSeconds(40));
		InsertVideoRequest video3 = new InsertVideoRequest(BigDecimal.valueOf(8.11), timestamp2);
		InsertVideoRequest video4 = new InsertVideoRequest(BigDecimal.valueOf(2.62), timestamp2);

		videosService.insertVideo(video1);
		videosService.insertVideo(video2);
		videosService.insertVideo(video3);
		videosService.insertVideo(video4);
	}

	@Test
	public void test_statistics_manyRandomVideos() {
		StatisticsResponse generatedStatistics = loadManyRandomVideos_erro();
		System.out.println(generatedStatistics);

		StatisticsResponse statistics = videosService.statistics();

		assertThat(statistics).isNotNull();
		assertThat(statistics.getSum()).isEqualTo(generatedStatistics.getSum());
		assertThat(statistics.getCount()).isEqualTo(generatedStatistics.getCount());
		assertThat(statistics.getMax()).isEqualTo(generatedStatistics.getMax());
		assertThat(statistics.getMin()).isEqualTo(generatedStatistics.getMin());
		assertThat(statistics.getAvg()).isEqualTo(generatedStatistics.getAvg());

		videosService.deleteAllVideos();
	}

	private StatisticsResponse loadManyRandomVideos_erro() {

		LocalDateTime testDate = DateUtils.now();

		long count = 1000000;
		BigDecimal sum = BigDecimal.valueOf(0.0);
		BigDecimal max = null;
		BigDecimal min = null;

		List<InsertVideoRequest> videoList = new ArrayList<>();
		for (long i = 1; i <= count; i++) {
			int diff = random.nextInt(50);
			long timestamp = DateUtils.localDateTimeToTimestampMillisecondsUTC(testDate.minusSeconds(diff));

			BigDecimal duration1 = generateDuration();
			sum = sum.add(duration1);
			if (max == null || duration1.compareTo(max) > 0) {
				max = duration1;
			}
			if (min == null || duration1.compareTo(min) < 0) {
				min = duration1;
			}
			InsertVideoRequest video1 = new InsertVideoRequest(duration1, timestamp);
			videoList.add(video1);

			BigDecimal duration2 = generateDuration();
			sum = sum.add(duration2);
			if (max == null || duration2.compareTo(max) > 0) {
				max = duration2;
			}
			if (min == null || duration2.compareTo(min) < 0) {
				min = duration2;
			}
			InsertVideoRequest video2 = new InsertVideoRequest(duration2, timestamp);
			videoList.add(video2);
		}
		videoList.parallelStream().forEach(videosService::insertVideo);

		count = videoList.size();
		BigDecimal avg = sum.divide(BigDecimal.valueOf(count), MathContext.DECIMAL128);

		return StatisticsResponse.builder().sum(sum).avg(avg).max(max).min(min).count(count).build();
	}

	private static BigDecimal generateDuration() {
		int intergerPart = random.nextInt(Integer.MAX_VALUE);
		double decimalValue = random.nextDouble();
		int decimalLimit = 10000;
		decimalValue = Math.floor(decimalValue * decimalLimit) / decimalLimit;
		decimalValue += intergerPart;
		return BigDecimal.valueOf(decimalValue);
	}

}
