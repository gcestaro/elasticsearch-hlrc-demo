package com.github.gcestaro.resources;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.gcestaro.models.Product;
import com.github.gcestaro.resources.filters.ProductFilter;
import com.github.gcestaro.services.ProductService;

@RestController
@RequestMapping("/products")
public class ProductSearchResources {

	private ProductService service;

	public ProductSearchResources(ProductService service) {
		this.service = service;
	}

	@GetMapping("/full-text-search")
	public ResponseEntity<List<Product>> fullTextSearch(@RequestParam String text) {

		return ResponseEntity.ok(service.fullTextSearch(text));
	}

	@GetMapping("/phrase-search")
	public ResponseEntity<List<Product>> phraseSearch(@RequestParam String description) {

		return ResponseEntity.ok(service.phraseSearch(description));
	}

	@GetMapping("/fuzzy-search")
	public ResponseEntity<List<Product>> fuzzySearch(@RequestParam String description) {

		return ResponseEntity.ok(service.fuzzySearch(description));
	}

	@GetMapping("/partial-matching-wildcard-search")
	public ResponseEntity<List<Product>> partialMatchWildcardSearch(@RequestParam String description) {

		return ResponseEntity.ok(service.partialMatchWildcardSearch(description));
	}

	@GetMapping("/partial-matching-regex-search")
	public ResponseEntity<List<Product>> partialMatchRegularExpressionSearch(@RequestParam String description) {

		return ResponseEntity.ok(service.partialMatchRegularExpressionSearch(description));
	}

	@GetMapping("/search-as-you-type")
	public ResponseEntity<List<Product>> searchAsYouType(@RequestParam String description) {

		return ResponseEntity.ok(service.searchAsYouType(description));
	}

	@GetMapping
	public ResponseEntity<Page<Product>> searchPagingAndSorting(ProductFilter filter,
			@PageableDefault(size = 100) Pageable pageable) {

		return ResponseEntity.ok(service.searchPagingAndSorting(filter, pageable));
	}
}
