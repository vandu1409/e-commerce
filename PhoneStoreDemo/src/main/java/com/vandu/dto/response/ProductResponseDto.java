package com.vandu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {

	private Long productId;

	private String name;

	private Double price;
	
	private String image;

	private boolean isAvailable;

	
	private String productCode;

	
	private String description;

	private String categoryCode;
	
	private Double discounts;
	
	private Double discountedPrice;
	


	
	


	

	
	
	

}
