package io.tmr.videostatistics.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import io.tmr.videostatistics.InvalidInputData;
import io.tmr.videostatistics.dto.StatisticsResponse;
import io.tmr.videostatistics.service.VideosService;

@RunWith(SpringRunner.class)
@WebMvcTest(VideoStatisticsController.class)
@AutoConfigureMockMvc
public class VideoStatisticsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private VideosService videosService;

	@Test
	public void test_insertVideo() throws Exception {
		String body = "{ \"duration\": 200.3, \"timestamp\": 1478192204000 }";

		// @formatter:off
		mockMvc.perform(post("/videos").content(body).contentType(MediaType.APPLICATION_JSON))
		    .andDo(print())
	  		.andExpect(status().isCreated());
		// @formatter:on
	}

	@Test
	public void test_insertVideo_Error() throws Exception {
		doThrow(new InvalidInputData("Invalid input data")).when(videosService).insertVideo(any());

		String body = "{ \"duration\": 200.3, \"timestamp\": 1478192204000 }";

		// @formatter:off
		mockMvc.perform(post("/videos").content(body).contentType(MediaType.APPLICATION_JSON))
		    .andDo(print())
	  		.andExpect(status().isNoContent());
		// @formatter:on
	}

	@Test
	public void test_deleteAllVideos() throws Exception {

		// @formatter:off
		mockMvc.perform(delete("/videos"))
		    .andDo(print())
	  		.andExpect(status().isNoContent());
		// @formatter:on
	}

	@Test
	public void test_statistics() throws Exception {
		when(videosService.statistics()).thenReturn(createStatistics());

		// @formatter:off
		mockMvc.perform(get("/statistics"))
		    .andDo(print())
	  		.andExpect(status().isOk())
	  		.andExpect(jsonPath("$.sum").isNotEmpty())
	  		.andExpect(jsonPath("$.avg").isNotEmpty())
	  		.andExpect(jsonPath("$.max").isNotEmpty())
	  		.andExpect(jsonPath("$.min").isNotEmpty())
	  		.andExpect(jsonPath("$.count").isNotEmpty());
		// @formatter:on
	}

	private StatisticsResponse createStatistics() {
		// @formatter:off
		return StatisticsResponse.builder()
				.sum(BigDecimal.valueOf(200.0))
				.avg(BigDecimal.valueOf(100.0))
				.max(BigDecimal.valueOf(150.0))
				.min(BigDecimal.valueOf(50.0))
				.count(2L)
				.build();
		// @formatter:on

	}

}
