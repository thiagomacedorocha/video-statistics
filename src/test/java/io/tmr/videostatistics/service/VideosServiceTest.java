package io.tmr.videostatistics.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.tmr.videostatistics.InvalidInputData;
import io.tmr.videostatistics.dto.InsertVideoRequest;
import io.tmr.videostatistics.utils.DateUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
	VideosServiceImpl.class
})
public class VideosServiceTest {

	@Autowired
	private VideosService videosService;

	@Test
	public void test_insertVideo_timestamp_lessThan60s() {
		InsertVideoRequest insertVideo = new InsertVideoRequest();
		insertVideo.setDuration(200.3);
		LocalDateTime testDate = DateUtils.now().minusSeconds(60);
		insertVideo.setTimestamp(DateUtils.localDateTimeToTimestampMillisecondsUTC(testDate));

		videosService.insertVideo(insertVideo);
	}

	@Test
	public void test_insertVideo_timestamp_moreThan60s() {
		InsertVideoRequest insertVideo = new InsertVideoRequest();
		insertVideo.setDuration(200.3);
		LocalDateTime testDate = DateUtils.now().minusSeconds(61);
		insertVideo.setTimestamp(DateUtils.localDateTimeToTimestampMillisecondsUTC(testDate));

		assertThatThrownBy(() -> videosService.insertVideo(insertVideo)).isInstanceOf(InvalidInputData.class);
	}

}
