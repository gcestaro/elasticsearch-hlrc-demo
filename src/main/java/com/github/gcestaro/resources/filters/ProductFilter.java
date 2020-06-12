package com.github.gcestaro.resources.filters;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime createdFrom;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime createdTo;

	public boolean filterByDescription() {
		return description != null;
	}

	@JsonIgnore
	public boolean filterByRange() {
		return filterByCreatedFrom() && filterByCreatedTo();
	}

	@JsonIgnore
	public boolean filterByCreatedFrom() {
		return createdFrom != null;
	}

	@JsonIgnore
	public boolean filterByCreatedTo() {
		return createdTo != null;
	}
}
