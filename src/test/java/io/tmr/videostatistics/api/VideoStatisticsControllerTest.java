package io.tmr.videostatistics.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import io.tmr.videostatistics.dto.StatisticsResponse;
import io.tmr.videostatistics.exception.InvalidInputData;
import io.tmr.videostatistics.service.VideosService;

@RunWith(SpringRunner.class)
@WebMvcTest(VideoStatisticsController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets", uriHost = "localhost:8080", uriScheme = "https",
		uriPort = 443)
public class VideoStatisticsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private VideosService videosService;

	private static final OperationResponsePreprocessor PREPROCESS_RESPONSE = preprocessResponse(prettyPrint(),
			removeHeaders("X-Content-Type-Options", "X-XSS-Protection", "Cache-Control", "Pragma", "Expires",
					"Strict-Transport-Security", "X-Frame-Options", "X-Application-Context"));

	private static final OperationRequestPreprocessor PREPROCESS_REQUEST = preprocessRequest(prettyPrint(),
			removeHeaders("X-Content-Type-Options", "X-XSS-Protection", "Cache-Control", "Pragma", "Expires",
					"Strict-Transport-Security", "X-Frame-Options", "X-Application-Context"));

	@Test
	public void test_insertVideo() throws Exception {
		String body = "{ \"duration\": 200.3, \"timestamp\": 1478192204000 }";

		// @formatter:off
		ResultActions result = mockMvc.perform(post("/videos").content(body).contentType(MediaType.APPLICATION_JSON))
		    .andDo(print())
	  		.andExpect(status().isCreated());
		
		result.andDo(
				document("insert-video", PREPROCESS_REQUEST, PREPROCESS_RESPONSE,
					requestFields(
							fieldWithPath("duration").description("Video duration"),
							fieldWithPath("timestamp").description("Time the video was added")
							)
					));
		// @formatter:on
	}

	@Test
	public void test_insertVideo_Error_MissingInputData() throws Exception {
		doThrow(new InvalidInputData("Invalid input data")).when(videosService).insertVideo(any());

		String body = "{ }";

		// @formatter:off
		ResultActions result = mockMvc.perform(
				post("/videos").content(body).contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNoContent());
		
		result.andDo(
				document("error-massage", PREPROCESS_REQUEST, PREPROCESS_RESPONSE,
				  responseFields(
						  fieldWithPath("serverTime").description("Error Server Time"),
						  fieldWithPath("message").description("Error message")
						  )
				 ));
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
		ResultActions result = mockMvc.perform(delete("/videos"))
		    .andDo(print())
	  		.andExpect(status().isNoContent());
		
		result.andDo(document("delete-allVideos", PREPROCESS_REQUEST, PREPROCESS_RESPONSE));
		// @formatter:on
	}

	@Test
	public void test_statistics_unknowError() throws Exception {
		when(videosService.statistics()).thenThrow(new RuntimeException("unknow error"));

		// @formatter:off
		mockMvc.perform(get("/statistics"))
			.andDo(print())
	  		.andExpect(status().isInternalServerError());
		// @formatter:on
	}

	@Test
	public void test_statistics() throws Exception {
		when(videosService.statistics()).thenReturn(createStatistics());

		// @formatter:off
		ResultActions result = mockMvc.perform(get("/statistics"))
		    .andDo(print())
	  		.andExpect(status().isOk())
	  		.andExpect(jsonPath("$.sum").isNotEmpty())
	  		.andExpect(jsonPath("$.avg").isNotEmpty())
	  		.andExpect(jsonPath("$.max").isNotEmpty())
	  		.andExpect(jsonPath("$.min").isNotEmpty())
	  		.andExpect(jsonPath("$.count").isNotEmpty());
		
		result.andDo(
				document("statistics", PREPROCESS_REQUEST, PREPROCESS_RESPONSE,
				  responseFields(
						  fieldWithPath("sum").description("Sum of all video lengths inserted in the last 60 seconds"),
						  fieldWithPath("avg").description("Average length of videos uploaded in the last 60 seconds"),
						  fieldWithPath("max").description("Longer duration of a video inserted in the last 60 seconds"),
						  fieldWithPath("min").description("Shorter length of inserted video in last 60 seconds"),
						  fieldWithPath("count").description("Number of videos inserted in the last 60 seconds")
						  )
				 ));
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
