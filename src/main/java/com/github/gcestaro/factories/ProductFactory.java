package com.github.gcestaro.factories;

import java.time.LocalDateTime;
import java.util.UUID;

import com.github.gcestaro.models.Product;

public final class ProductFactory {

	private ProductFactory() {
	}

	public static Product oldProduct() {
		return Product
				.builder()
				.createdAt(LocalDateTime.now().minusMonths(15L))
				.description("Old Product")
				.id(UUID.randomUUID().toString())
				.build();

	}

	public static Product teenProduct() {
		return Product
				.builder()
				.createdAt(LocalDateTime.now().minusMonths(6L))
				.description("Teen Product")
				.id(UUID.randomUUID().toString())
				.build();

	}

	public static Product babyProduct() {
		return Product
				.builder()
				.createdAt(LocalDateTime.now())
				.description("Baby Product")
				.id(UUID.randomUUID().toString())
				.build();

	}
}
