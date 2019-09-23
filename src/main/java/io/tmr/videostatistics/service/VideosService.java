package io.tmr.videostatistics.service;

import io.tmr.videostatistics.dto.StatisticsResponse;
import io.tmr.videostatistics.model.Video;

public interface VideosService {

	void insertVideo(Video video);

	void deleteAllVideos();

	StatisticsResponse statistics();

}
