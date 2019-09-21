package io.tmr.videostatistics.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import io.tmr.videostatistics.InvalidInputData;
import io.tmr.videostatistics.dto.InsertVideoRequest;
import io.tmr.videostatistics.dto.StatisticsResponse;
import io.tmr.videostatistics.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VideosServiceImpl implements VideosService {

	private static final long MAXIMUM_DIFFERENCE_SECONDS = 61;

	@Override
	public void insertVideo(InsertVideoRequest insertVideo) {
		log.info("insertVideo");
		LocalDateTime date = DateUtils.convertTimestampToLocalDateTimeUTC(insertVideo.getTimestamp());
		validateDateMoreThen60Seconds(date);
	}

	private void validateDateMoreThen60Seconds(LocalDateTime date) {
		LocalDateTime deadline = DateUtils.now().minusSeconds(MAXIMUM_DIFFERENCE_SECONDS);

		if (date.isBefore(deadline)) {
			throw new InvalidInputData("Invalid date older than 60 seconds.");
		}
	}

	@Override
	public void deleteAllVideos() {
		log.info("deleteAllVideos");
	}

	@Override
	public StatisticsResponse statistics() {
		log.info("statistics");
		// @formatter:off
		return StatisticsResponse.builder()
				.sum(1000.0)
				.avg(100.0)
				.max(200.0)
				.min(50.0)
				.count(10L)
				.build();
		// @formatter:on
	}

}
