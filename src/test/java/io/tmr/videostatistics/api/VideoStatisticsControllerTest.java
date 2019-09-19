package io.tmr.videostatistics.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class VideoStatisticsControllerTest {

	@Autowired
	private MockMvc mockMvc;

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
	public void test_deleteAllVideos() throws Exception {

		// @formatter:off
		mockMvc.perform(delete("/videos"))
		    .andDo(print())
	  		.andExpect(status().isNoContent());
		// @formatter:on
	}

	@Test
	public void test_statistics() throws Exception {

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

}
