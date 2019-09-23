package io.tmr.videostatistics.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(Include.NON_NULL)
public class ErrorMessageDTO {

	private LocalDateTime serverTime;
	private String message;
	private String debugMessage;

}
