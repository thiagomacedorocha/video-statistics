package io.tmr.videostatistics.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.SortedMap;

import org.springframework.stereotype.Service;

import io.tmr.videostatistics.dto.StatisticsResponse;
import io.tmr.videostatistics.exception.InvalidInputData;
import io.tmr.videostatistics.model.Statistic;
import io.tmr.videostatistics.model.Video;
import io.tmr.videostatistics.repository.StatisticsRepository;
import io.tmr.videostatistics.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VideosServiceImpl implements VideosService {

	private static final long MAXIMUM_DIFFERENCE_SECONDS = 61;
	private static final long STATISTICS_PERIOD_SECONDS = 60;

	private final StatisticsRepository statisticsRepository;

	public VideosServiceImpl(StatisticsRepository statisticsRepository) {
		this.statisticsRepository = statisticsRepository;
	}

	@Override
	public void insertVideo(Video video) {
		try {
			validateDate(video.getTimestamp());
			validateDuration(video.getDuration());
			statisticsRepository.save(video);
		} catch (Exception e) {
			log.error("insertVideo", e);
			throw e;
		}
	}

	private void validateDuration(BigDecimal duration) {
		if (duration.compareTo(BigDecimal.ZERO) <= 0) {
			throw new InvalidInputData("Duration nmust be greater than zero.");
		}
	}

	private void validateDate(Long timestamp) {
		LocalDateTime date = DateUtils.timestampToLocalDateTimeUTC(timestamp);
		validateDateMoreThen60Seconds(date);
		validateDateInTheFuture(date);
	}

	private void validateDateInTheFuture(LocalDateTime date) {
		LocalDateTime now = DateUtils.now();

		if (date.isAfter(now)) {
			throw new InvalidInputData("Invalid date in the future.");
		}
	}

	private void validateDateMoreThen60Seconds(LocalDateTime date) {
		LocalDateTime deadline = DateUtils.now().minusSeconds(MAXIMUM_DIFFERENCE_SECONDS);

		if (date.isBefore(deadline)) {
			throw new InvalidInputData("Invalid date older than 60 seconds.");
		}
	}

	@Override
	public void deleteAllVideos() {
		try {
			statisticsRepository.deleteAll();
		} catch (Exception e) {
			log.error("deleteAllVideos", e);
			throw e;
		}
	}

	@Override
	public StatisticsResponse statistics() {
		try {
			LocalDateTime deadline = DateUtils.now().minusSeconds(STATISTICS_PERIOD_SECONDS);
			Long timestamp = DateUtils.localDateTimeToTimestampMillisecondsUTC(deadline);
			SortedMap<Long, Statistic> statistics = statisticsRepository.listStatisticsAfter(timestamp);

			return aggregateStatistics(statistics);
		} catch (Exception e) {
			log.error("statistics", e);
			throw e;
		}
	}

	private StatisticsResponse aggregateStatistics(Map<Long, Statistic> statistics) {
		Statistic finalStatistic = statistics.values().parallelStream().reduce(new Statistic(),
				(total, statistic) -> aggregateStatistic(total, statistic));

		BigDecimal bigDecimalCount = BigDecimal.valueOf(finalStatistic.getCount());
		BigDecimal avg = null;
		if (!bigDecimalCount.equals(BigDecimal.ZERO)) {
			avg = finalStatistic.getSum().divide(bigDecimalCount, MathContext.DECIMAL128);
		}

		// @formatter:off
		return StatisticsResponse.builder()
				.sum(finalStatistic.getSum())
				.avg(avg)
				.max(finalStatistic.getMax())
				.min(finalStatistic.getMin())
				.count(finalStatistic.getCount())
				.build();
		// @formatter:on
	}

	private Statistic aggregateStatistic(Statistic a, Statistic b) {
		BigDecimal newSum = a.getSum().add(b.getSum());
		Long newCount = a.getCount() + b.getCount();
		BigDecimal newMAx = compareMax(a.getMax(), b.getMax());
		BigDecimal newMin = compareMin(a.getMin(), b.getMin());

		return new Statistic(newSum, newMAx, newMin, newCount);
	}

	private BigDecimal compareMax(BigDecimal duration1, BigDecimal duration2) {
		if (duration1 == null) {
			return duration2;
		} else if (duration2 == null) {
			return duration1;
		}
		return duration1.compareTo(duration2) > 0 ? duration1 : duration2;
	}

	private BigDecimal compareMin(BigDecimal duration1, BigDecimal duration2) {
		if (duration1 == null) {
			return duration2;
		} else if (duration2 == null) {
			return duration1;
		}
		return duration1.compareTo(duration2) < 0 ? duration1 : duration2;
	}

}
