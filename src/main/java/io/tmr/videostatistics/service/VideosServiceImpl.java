package io.tmr.videostatistics.service;

import org.springframework.stereotype.Service;

import io.tmr.videostatistics.dto.InsertVideoRequest;
import io.tmr.videostatistics.dto.StatisticsResponse;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VideosServiceImpl implements VideosService {

	@Override
	public void insertVideo(InsertVideoRequest insertVideo) {
		log.info("insertVideo");
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
