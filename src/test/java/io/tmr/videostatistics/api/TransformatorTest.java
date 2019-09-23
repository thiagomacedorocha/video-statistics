package io.tmr.videostatistics.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

import io.tmr.videostatistics.dto.InsertVideoRequest;
import io.tmr.videostatistics.model.Video;

public class TransformatorTest {

	@Test
	public void test_transform_InsertVideoRequest_Scale() {
		InsertVideoRequest insertVideoRequest = new InsertVideoRequest();
		insertVideoRequest.setTimestamp(1478192204000L);

		double duration = 100.123456;
		insertVideoRequest.setDuration(BigDecimal.valueOf(duration));

		Video video = Transformator.transform(insertVideoRequest);
		double limitedDuration = 100.1234;
		assertThat(video).isNotNull();
		assertThat(video.getDuration()).isEqualTo(BigDecimal.valueOf(limitedDuration));
	}

}
