package com.github.gcestaro.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "products-elasticsearch-demo-index")
public class Product implements Persistable<String> {

	@Id
	private String id;

	@MultiField(
			mainField = @Field(type = FieldType.Text, fielddata = true),
			otherFields = {
					@InnerField(suffix = "raw", type = FieldType.Keyword)
			})
	private String description;

	@CreatedDate
	@Field(type = FieldType.Date, format = DateFormat.basic_date_time)
	private LocalDateTime createdAt;

	@Override
	@JsonIgnore
	public boolean isNew() {
		return id == null;
	}
}
