package io.tmr.videostatistics.repository;

import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.stereotype.Repository;

import io.tmr.videostatistics.dto.InsertVideoRequest;
import io.tmr.videostatistics.model.Statistic;

@Repository
public class StatisticsRepositoryImpl implements StatisticsRepository {

	private SortedMap<Long, Statistic> statistics;

	public StatisticsRepositoryImpl() {
		this.statistics = Collections.synchronizedSortedMap(new TreeMap<Long, Statistic>());
	}

	public void save(InsertVideoRequest insertVideo) {
		Statistic statistic = statistics.get(insertVideo.getTimestamp());
		if (statistic != null) {
			statistic.addNewVideoDuration(insertVideo.getDuration());
		} else {
			Statistic newStatistic = new Statistic(insertVideo.getDuration());
			statistics.put(insertVideo.getTimestamp(), newStatistic);
		}
	}

	public void deleteAll() {
		statistics = Collections.synchronizedSortedMap(new TreeMap<Long, Statistic>());
	}

	public SortedMap<Long, Statistic> listStatisticsAfter(Long timestamp) {
		return statistics.tailMap(timestamp);
	}
}
