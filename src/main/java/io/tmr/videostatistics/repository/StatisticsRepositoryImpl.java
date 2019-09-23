package io.tmr.videostatistics.repository;

import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.stereotype.Repository;

import io.tmr.videostatistics.model.Statistic;
import io.tmr.videostatistics.model.Video;

@Repository
public class StatisticsRepositoryImpl implements StatisticsRepository {

	private SortedMap<Long, Statistic> statistics;

	public StatisticsRepositoryImpl() {
		this.statistics = Collections.synchronizedSortedMap(new TreeMap<Long, Statistic>());
	}

	@Override
	public synchronized void save(Video video) {
		Statistic statistic = statistics.get(video.getTimestamp());
		if (statistic != null) {
			statistic.addNewVideoDuration(video.getDuration());
		} else {
			Statistic newStatistic = new Statistic(video.getDuration());
			statistics.put(video.getTimestamp(), newStatistic);
		}
	}

	@Override
	public void deleteAll() {
		statistics = Collections.synchronizedSortedMap(new TreeMap<Long, Statistic>());
	}

	@Override
	public SortedMap<Long, Statistic> listStatisticsAfter(Long timestamp) {
		return statistics.tailMap(timestamp);
	}
}
