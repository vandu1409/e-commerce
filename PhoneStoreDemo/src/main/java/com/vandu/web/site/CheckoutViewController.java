package com.vandu.web.site;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.vandu.model.Brand;
import com.vandu.model.Category;
import com.vandu.service.BrandService;
import com.vandu.service.CategoryService;

@Controller
public class CheckoutViewController {
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;
	
	@ModelAttribute("listCategory")
	public List<Category> getAllCategory(){
		return categoryService.findAll();
	}
	
	@ModelAttribute("listBrand")
	public List<Brand> getAllBrand() {
		return brandService.findAll();
	}
	
	@GetMapping("thanh-toan")
	public String checkOut() {
		return "/site/checkout";
	}
}
