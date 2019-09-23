package io.tmr.videostatistics.repository;

import java.util.SortedMap;

import io.tmr.videostatistics.model.Statistic;
import io.tmr.videostatistics.model.Video;

public interface StatisticsRepository {

	void save(Video video);

	void deleteAll();

	SortedMap<Long, Statistic> listStatisticsAfter(Long timestamp);

}
