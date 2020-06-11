package com.github.gcestaro.resources.filters;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilter {

	private String description;

	private LocalDateTime createdFrom;

	private LocalDateTime createdTo;
}
