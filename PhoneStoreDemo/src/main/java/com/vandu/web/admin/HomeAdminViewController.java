package com.vandu.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeAdminViewController {

	@GetMapping("admin/home")
	public String home() {
		return "admin/index";
	}
}
