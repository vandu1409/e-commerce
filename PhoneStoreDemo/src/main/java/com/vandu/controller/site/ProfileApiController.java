package com.vandu.controller.site;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vandu.dto.response.UserResponseDto;
import com.vandu.helper.UserHelper;
import com.vandu.model.User;
import com.vandu.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("profile")
public class ProfileApiController {
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private UserService userService;
	
//	@PostMapping("change-password")
//	public ResponseEntity<?> changePassword(@)

	@GetMapping("getCurrentUser")
	public ResponseEntity<?> getCurrentUser(){
		User user  = UserHelper.getCurrentUser(request, userService);
		
		UserResponseDto userResponseDto = new UserResponseDto();
		
		BeanUtils.copyProperties(user, userResponseDto);
		
		return ResponseEntity.ok(userResponseDto);
	}
}
