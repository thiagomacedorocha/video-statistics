package io.tmr.videostatistics.api;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import io.tmr.videostatistics.InvalidInputData;
import io.tmr.videostatistics.dto.ErrorMessageDTO;
import io.tmr.videostatistics.dto.InsertVideoRequest;
import io.tmr.videostatistics.dto.StatisticsResponse;
import io.tmr.videostatistics.service.VideosService;
import io.tmr.videostatistics.utils.DateUtils;

@RestController()
public class VideoStatisticsController {

	private static final String VIDEOS = "videos";
	private static final String STATISTICS = "statistics";

	private final VideosService videosService;

	public VideoStatisticsController(VideosService videosService) {
		super();
		this.videosService = videosService;
	}

	@PostMapping(value = VIDEOS)
	@ResponseStatus(HttpStatus.CREATED)
	public void insertVideo(@Valid @RequestBody InsertVideoRequest insertVideo) {
		videosService.insertVideo(Transformator.transform(insertVideo));
	}

	@DeleteMapping(value = VIDEOS)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAllVideos() {
		videosService.deleteAllVideos();
	}

	@GetMapping(value = STATISTICS)
	@ResponseStatus(HttpStatus.OK)
	public StatisticsResponse statistics() {
		return videosService.statistics();
	}

	@ExceptionHandler(InvalidInputData.class)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ErrorMessageDTO invalidInputData(Exception ex, WebRequest req) {
		// @formatter:off
		return ErrorMessageDTO.builder()
				.message(ex.getMessage())
				.serverTime(DateUtils.now())
				.build();
		// @formatter:on
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorMessageDTO unknownException(Exception ex, WebRequest req) {
		String debugMsg = ex.getCause() != null ? ex.getCause().getCause().getMessage() : null;
		// @formatter:off
		return ErrorMessageDTO.builder()
				.message(ex.getMessage())
				.serverTime(DateUtils.now())
				.debugMessage(debugMsg)
				.build();
		// @formatter:on
	}

}
