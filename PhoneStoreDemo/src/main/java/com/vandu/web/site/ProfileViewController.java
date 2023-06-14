package com.vandu.web.site;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.vandu.model.Brand;
import com.vandu.model.Category;
import com.vandu.model.Product;
import com.vandu.service.CategoryService;

@Controller

public class ProfileViewController {
	
	@Autowired
	private CategoryService categoryService;

	
	
	@ModelAttribute("listCategory")
	public List<Category> getAllCategory(){
		return categoryService.findAll();
	}

	
	@GetMapping("profile")
	public String profile() {
		return "site/profile";
	}
}
