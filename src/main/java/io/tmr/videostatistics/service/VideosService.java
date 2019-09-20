package io.tmr.videostatistics.service;

import io.tmr.videostatistics.dto.InsertVideoRequest;
import io.tmr.videostatistics.dto.StatisticsResponse;

public interface VideosService {

	void insertVideo(InsertVideoRequest insertVideo);

	void deleteAllVideos();

	StatisticsResponse statistics();

}
