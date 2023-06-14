package com.vandu.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vandu.dto.RoleDto;
import com.vandu.dto.UserDto;
import com.vandu.model.Role;
import com.vandu.service.RoleService;
import com.vandu.service.UserRoleService;

@RestController
@RequestMapping("/admin/role")
public class RoleApiController {
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserRoleService userRoleService;

	@PostMapping("saveOrUpdate")
	public ResponseEntity<RoleDto> saveOrUpdate(@RequestBody RoleDto roleDto) {
		Role role = new Role();

		BeanUtils.copyProperties(roleDto, role);

		Role entity = roleService.save(role);

		BeanUtils.copyProperties(entity, roleDto);

		return ResponseEntity.ok(roleDto);
	}

	@GetMapping("delete/{id}")
	public ResponseEntity<String> delete(@PathVariable("id") Long id) {
		Role role = roleService.findById(id).orElse(new Role());

		if (role != null) {
			roleService.delete(role);
		}

		return ResponseEntity.ok("Success");

	}

	@GetMapping("find/{id}")
	public ResponseEntity<RoleDto> find(@PathVariable("id") Long id) {
		Role role = roleService.findById(id).orElse(null);
		RoleDto roleDto = new RoleDto();

		BeanUtils.copyProperties(role, roleDto);

		return ResponseEntity.ok(roleDto);

	}

	@GetMapping("getAll")
	public ResponseEntity<Page<Role>> getAll(@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size, @RequestParam("keyword") String keyword) {

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(3);

		Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

		Page<Role> list = keyword.equals("") ? roleService.findAll(pageable)
				: roleService.findByName(keyword, pageable);

		return ResponseEntity.ok(list);

	}

	@GetMapping("getRoleByUser/{userId}")
	public ResponseEntity<List<RoleDto>> getRoleByUser(@PathVariable("userId") Long userId) {
		return ResponseEntity.ok(userRoleService.getRoleByUser(userId).stream().map(item -> {
			RoleDto roleDto = new RoleDto();

			BeanUtils.copyProperties(item, roleDto);

			return roleDto;
		}).toList());

	}

}
