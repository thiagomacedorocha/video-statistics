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
	// @Test
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

	// private StatisticsResponse loadVideos_procurando_erro() {
	// LocalDateTime testDate = DateUtils.now();
	// List<BigDecimal> durations =
	// List.of(BigDecimal.valueOf(2064252371.4119806), BigDecimal.valueOf(1284389816.2697136),
	// BigDecimal.valueOf(711294082.7796273), BigDecimal.valueOf(1226533444.230466),
	// BigDecimal.valueOf(1653374729.7533033), BigDecimal.valueOf(26128290.782333903),
	// BigDecimal.valueOf(996613856.1795837), BigDecimal.valueOf(1343992411.8747733),
	// BigDecimal.valueOf(402127314.0219771), BigDecimal.valueOf(282380417.9582148));
	// // List<Double> durations = List.of(14.76, 10.73);
	//
	// long count = durations.size();
	// BigDecimal sum = BigDecimal.valueOf(0.0);
	// BigDecimal max = null;
	// BigDecimal min = null;
	//
	// List<InsertVideoRequest> videoList = new ArrayList<>();
	// for (BigDecimal duration : durations) {
	// sum = sum.add(duration);
	// if (max == null || duration.compareTo(max) > 0) {
	// max = duration;
	// }
	// if (min == null || duration.compareTo(min) < 0) {
	// min = duration;
	// }
	//
	// testDate = testDate.minusSeconds(3);
	// long timestamp = DateUtils.localDateTimeToTimestampMillisecondsUTC(testDate);
	// InsertVideoRequest video = new InsertVideoRequest(duration, timestamp);
	// videoList.add(video);
	// }
	// videoList.parallelStream().forEach(videosService::insertVideo);
	//
	// BigDecimal avg = sum.divide(BigDecimal.valueOf(count), MathContext.DECIMAL128);
	//
	// return StatisticsResponse.builder().sum(sum).avg(avg).max(max).min(min).count(count).build();
	// }

	private StatisticsResponse loadManyRandomVideos_erro() {

		LocalDateTime testDate = DateUtils.now();

		long count = 5; // 100000;
		BigDecimal sum = BigDecimal.valueOf(0.0);
		BigDecimal max = null;
		BigDecimal min = null;

		List<InsertVideoRequest> videoList = new ArrayList<>();
		for (long i = 1; i <= count; i++) {
			int diff = random.nextInt(50);
			long timestamp = DateUtils.localDateTimeToTimestampMillisecondsUTC(testDate.minusSeconds(diff));

			BigDecimal duration1 = generateDuration();
			System.out.println(duration1);
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
			System.out.println(duration2);
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
		int sizeDuration = 100;
		int intergerPart = random.nextInt((int) Math.pow(10, sizeDuration));
		double valor = random.nextDouble();
		valor += intergerPart;
		return BigDecimal.valueOf(valor);
	}

}
