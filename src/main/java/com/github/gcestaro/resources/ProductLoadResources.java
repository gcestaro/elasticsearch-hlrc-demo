package com.github.gcestaro.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.gcestaro.services.ProductService;

@RestController
@RequestMapping("/products/load")
public class ProductLoadResources {

	private ProductService service;

	public ProductLoadResources(ProductService service) {
		this.service = service;
	}

	@PostMapping("/full")
	public ResponseEntity<?> loadDocuments() {
		service.load();

		return ResponseEntity.accepted().build();
	}

	@PostMapping
	public ResponseEntity<?> loadTestingDocuments() {
		service.loadSimpleTest();
		
		return ResponseEntity.accepted().build();
	}

	@DeleteMapping
	public ResponseEntity<?> dropProducts() {
		service.deleteAll();

		return ResponseEntity.noContent().build();
	}
}
