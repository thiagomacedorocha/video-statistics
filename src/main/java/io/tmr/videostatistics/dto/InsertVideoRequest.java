package io.tmr.videostatistics.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InsertVideoRequest {

	@NotNull
	private BigDecimal duration;

	@NotNull
	private Long timestamp;

}
