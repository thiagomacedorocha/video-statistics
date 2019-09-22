package io.tmr.videostatistics.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Statistic {

	private BigDecimal sum;
	private BigDecimal max;
	private BigDecimal min;
	private Long count;

	public Statistic() {
		super();
		sum = BigDecimal.valueOf(0.0);
		count = 0L;
		max = null;
		min = null;
	}

	public Statistic(BigDecimal duration) {
		super();
		sum = duration;
		count = 1L;
		max = duration;
		min = duration;
	}

	public synchronized void addNewVideoDuration(BigDecimal duration) {
		sum = sum.add(duration);
		count++;

		if (max == null || max.compareTo(duration) < 0) {
			max = duration;
		}

		if (min == null || min.compareTo(duration) > 0) {
			min = duration;
		}
	}
}
