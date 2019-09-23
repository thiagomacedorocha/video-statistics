package io.tmr.videostatistics.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Video {

	private BigDecimal duration;
	private Long timestamp;

}
