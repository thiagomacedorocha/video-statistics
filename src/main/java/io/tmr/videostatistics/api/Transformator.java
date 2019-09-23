package io.tmr.videostatistics.api;

import java.math.BigDecimal;
import java.math.RoundingMode;

import io.tmr.videostatistics.dto.InsertVideoRequest;
import io.tmr.videostatistics.model.Video;

public class Transformator {

	public static final Integer DURATION_SCALE = 4;

	private Transformator() {
	}

	public static Video transform(InsertVideoRequest insertVideoRequest) {
		BigDecimal duration = insertVideoRequest.getDuration().setScale(DURATION_SCALE, RoundingMode.DOWN);
		// @formatter:off
		return Video.builder()
					.timestamp(insertVideoRequest.getTimestamp())
					.duration(duration)
					.build();
		// @formatter:on
	}

}
