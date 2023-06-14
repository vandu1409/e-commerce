package com.vandu.controller.admin;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vandu.dto.RoleDto;
import com.vandu.dto.UserDto;
import com.vandu.dto.UserRolesDto;
import com.vandu.exception.DuplicateDataException;
import com.vandu.helper.AuthenticationType;
import com.vandu.model.Role;
import com.vandu.model.User;
import com.vandu.model.UserRole;
import com.vandu.service.RoleService;
import com.vandu.service.UserRoleService;
import com.vandu.service.UserService;

@RestController
@RequestMapping("admin/user")
public class UserApiController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserRoleService userRoleService;

	@PostMapping("saveOrUpdate")
	public ResponseEntity<UserDto> save(@RequestBody UserDto userDto) {
		User user = new User();

		BeanUtils.copyProperties(userDto, user);
		user.setAuthenticationType(AuthenticationType.LOCAL);

		if (userDto.getUserId() != null) {
			if (userDto.getAvatar() == null) {
				User currentUser = userService.findById(user.getUserId()).orElseThrow();
				user.setAvatar(currentUser.getAvatar());
			}

			user.setUpdateDate(new Date());
		}

		User entity = userService.save(user);

		BeanUtils.copyProperties(entity, userDto);

		return ResponseEntity.ok(userDto);

	}


	@GetMapping("find/{userId}")
	public ResponseEntity<UserDto> findById(@PathVariable("userId") Long userId) {
		User user = userService.findById(userId).orElseThrow();
		UserDto userDto = new UserDto();

		BeanUtils.copyProperties(user, userDto);

		return ResponseEntity.ok(userDto);

	}

	@PutMapping("delete/{userId}")
	public ResponseEntity<String> delete(@PathVariable("userId") Long userId) {
		User user = userService.findById(userId).orElseThrow();

		if(user!=null) {
			user.setDeleted(true);
			userService.save(user);
		}

		return ResponseEntity.ok("Success");
	}

	@GetMapping("getAll")
	public ResponseEntity<Page<User>> getAll(@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size, @RequestParam("keyword") String keyword,@RequestParam("status") int status) {

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(3);
		boolean isDeleted = status==1;

		Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

		Page<User> list = keyword.equals("") ? userService.findAll(isDeleted,pageable)
				: userService.findByUsernameContainingAndIsDeleted(keyword, isDeleted,pageable);

		return ResponseEntity.ok(list);

	}

	@GetMapping("getAllRoles")
	public List<RoleDto> getAllRole() {
		return roleService.findAll().stream().map(item -> {
			RoleDto roleDto = new RoleDto();

			BeanUtils.copyProperties(item, roleDto);
			return roleDto;
		}).toList();
	}

	@PostMapping("saveUserRoles")
	public ResponseEntity<?> saveUserRoles(@RequestBody UserRolesDto userRolesDto) {
		try {

			UserRole userRole = userRoleService.findByUserAndRole(userRolesDto.getUserId(), userRolesDto.getRoleId()).orElse(null);
			
			if(userRole!=null) {
				throw new DuplicateDataException("Vai trò này đã tồn tại!");
			}
					

			User user = new User();
			user.setUserId(userRolesDto.getUserId());

			Role role = new Role();
			role.setId(userRolesDto.getRoleId());

			UserRole entity = userRoleService.saveUserRoles(user, role);

			return ResponseEntity.ok("Success");
		} catch (DuplicateDataException e) {
			throw e;
		}

	}

	@DeleteMapping("deleteRoleByUser/{userId}/{roleId}")
	public ResponseEntity<?> deleteRoleByUser(@PathVariable("userId") Long userId,
			@PathVariable("roleId") Long roleId) {
		UserRole userRole = userRoleService.findByUserAndRole(userId, roleId).orElse(null);

		if (userRole != null) {
			userRoleService.delete(userRole);
		}

		return ResponseEntity.ok("Delete Success");
	}
	
	@PutMapping("restore/{userId}")
	public ResponseEntity<?> restoreUser(@PathVariable("userId") Long userId){
		User user = userService.findById(userId).orElse(null);
		
		if(user!=null && user.isDeleted()) {
			user.setDeleted(false);
			userService.save(user);
		}
		
		return ResponseEntity.ok("Delete Success");
	}

}
