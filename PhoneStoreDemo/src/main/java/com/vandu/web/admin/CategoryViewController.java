package com.vandu.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/category")
public class CategoryViewController {
	
	@GetMapping("list")
	public String list() {
		return "admin/category";
	}

}
