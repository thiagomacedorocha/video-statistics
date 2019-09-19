package io.tmr.videostatistics.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class InsertVideoRequest {

	@NotNull
	private Double duration;

	@NotNull
	private Long timestamp;

}
