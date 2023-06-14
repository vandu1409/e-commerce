package com.vandu.controller.admin;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vandu.dto.ProductDiscountDto;
import com.vandu.model.Discount;
import com.vandu.model.Product;
import com.vandu.model.ProductDiscount;
import com.vandu.service.ProductDiscountService;

@RestController
@RequestMapping("admin/product-discount")
public class ProductDiscountApiController {
	
	@Autowired
	private ProductDiscountService productDiscountService;
	
	
	@PostMapping("saveOrUpdate")
	public ResponseEntity<ProductDiscountDto> saveOrUpdate(@RequestBody ProductDiscountDto productDiscountDto){
		Product product = new Product();
		product.setProductId(productDiscountDto.getProductId());
		
		Discount discount = new Discount();
		discount.setId(productDiscountDto.getDiscountId());
		
		ProductDiscount productDiscount = new ProductDiscount();
		productDiscount.setDiscount(discount);
		productDiscount.setProduct(product);
		
		ProductDiscount entity = productDiscountService.save(productDiscount);
		
		BeanUtils.copyProperties(entity, productDiscountDto);
		
		
		return ResponseEntity.ok(productDiscountDto);
		
		
	}
	
	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id){
		productDiscountService.deleteById(id);
		
		return ResponseEntity.ok("Success");
	}
	
	@DeleteMapping("deleteByProductAndDiscount/{pid}/{did}")
	public ResponseEntity<?> deleteByProductAndDiscount(@PathVariable("pid") Long pid,@PathVariable("did")Long did){
		productDiscountService.deleteByProductAndDiscount(pid,did);
		
		return ResponseEntity.ok("Success");
	}

}
