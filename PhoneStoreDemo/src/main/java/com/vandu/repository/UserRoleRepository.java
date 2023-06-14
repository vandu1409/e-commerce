package com.vandu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vandu.model.Role;
import com.vandu.model.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long>{

	@Query("select u from UserRole u where u.user.username= :username and u.role.name=:rolename")
	Optional<UserRole> findByUserAndRole(String username,String rolename);
	
	@Query("select u.role from UserRole u where u.user.userId= :userId")
	List<Role> getRoleByUser(Long userId);
	
	@Query("select u from UserRole u where u.user.userId= :userId and u.role.id=:roleId")
	Optional<UserRole> findByUserAndRole(Long userId,Long roleId);
}
