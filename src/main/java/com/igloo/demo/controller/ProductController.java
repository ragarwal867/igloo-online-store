package com.igloo.demo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.igloo.demo.entity.Product;

@RestController
@RequestMapping("/api")
public class ProductController {
	

	private List<Product> allProducts;
	
	
	@PostConstruct
	public void loadData() {
		allProducts = new ArrayList<Product>();
		allProducts.add(new Product(101L, 
								"Himalaya Herbals Purifying Neem Face Wash, 300ml", 
								250.00, 
								"A herbal face wash for both men and women having the goodness of neem. A"
								+ "Apply Intense Oil Clear Face Wash on a moist face and massage. "
								+ "Gently work up a lather using a circular motion. ", 
								"RK World Infocom"));
		allProducts.add(new Product(102L, 
									"BAHAMAS mens Yellow Flip Flops", 
									150.00, 
									"A good all weather rubber made sleeper by bahamas",
									"Lotta Enterprises"));
		allProducts.add(new Product(103L, 
									"Samsung Galaxy M21 (Arctic Blue, 4GB RAM, 64GB Storage) | FHD+ sAMOLED)", 
									11500.00, 
									"16.21 centimeters (6.4-inch) Super AMOLED , 60Hz Refresh rate, protected by Gorilla Glass, "
									+"48MP+8MP+5MP Triple camera, 20MP front camera, Monster 6000 mAh Battery, " 
									+ "Memory, Storage & SIM: 4GB RAM | 64GB internal memory expandable up to 512GB| SIM 1 + SIM 2 + MicroSD, " 
									+ "Android v11.0, One UI 3.1 operating system with Exynos 9611 Octa Core Processor 2.3GHz,1.7GHz",
									"Samsung Official Store"));
		
	}
	

    @GetMapping("/products")
    public List<Product> getAllProducts() {
    	return allProducts;
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable Long id) {
    	Product requestedProduct = allProducts.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElse(null);
        
        if (requestedProduct == null) {
        	throw new ProductNotFoundException("Request Product id not found - " + id);
        } else {
        	return requestedProduct;
        }
        
    }
    
    @ExceptionHandler
	public ResponseEntity<ProductErrorResponse> handleException(ProductNotFoundException exc) {
		
		// create ProductErrorResponse
		ProductErrorResponse error = new ProductErrorResponse();
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage(exc.getMessage());
		error.setTimeStamp(System.currentTimeMillis());
		
		// return ResponseEnitity
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	// add another exception handler ... to catch ant exception
	@ExceptionHandler
	public ResponseEntity<ProductErrorResponse> handleException(Exception exc) {
		
		// create ProductErrorResponse
		ProductErrorResponse error = new ProductErrorResponse();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(exc.getMessage());
		error.setTimeStamp(System.currentTimeMillis());
		
		// return ResponseEnitity
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
}
