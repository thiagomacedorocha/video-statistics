package io.tmr.videostatistics.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StatisticsResponse {

	private Double sum;
	private Double avg;
	private Double max;
	private Double min;
	private Long count;

}
