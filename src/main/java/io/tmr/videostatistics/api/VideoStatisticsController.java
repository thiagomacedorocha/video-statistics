package io.tmr.videostatistics.api;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.tmr.videostatistics.dto.InsertVideoRequest;
import io.tmr.videostatistics.dto.StatisticsResponse;
import lombok.extern.slf4j.Slf4j;

@RestController()
@Slf4j
public class VideoStatisticsController {

	private static final String VIDEOS = "videos";
	private static final String STATISTICS = "statistics";

	@PostMapping(value = VIDEOS)
	@ResponseStatus(HttpStatus.CREATED)
	public void insertVideo(@Valid @RequestBody InsertVideoRequest insertVideo) {
		log.info("insertVideo - " + insertVideo);

	}

	@DeleteMapping(value = VIDEOS)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAllVideos() {
		log.info("deleteAllVideos");

	}

	@GetMapping(value = STATISTICS)
	@ResponseStatus(HttpStatus.OK)
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
