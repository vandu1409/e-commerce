package com.vandu.dto;

import java.util.Set;

import com.vandu.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRolesDto {

//	private Set<RoleDto> roles; 
	
	private Long id;
	
	private Long userId;
	
	private Long roleId;
}
