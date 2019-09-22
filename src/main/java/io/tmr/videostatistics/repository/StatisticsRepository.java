package io.tmr.videostatistics.repository;

import java.util.SortedMap;

import io.tmr.videostatistics.dto.InsertVideoRequest;
import io.tmr.videostatistics.model.Statistic;

public interface StatisticsRepository {

	void save(InsertVideoRequest insertVideo);

	void deleteAll();

	SortedMap<Long, Statistic> listStatisticsAfter(Long timestamp);

}
