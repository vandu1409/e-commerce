package com.vandu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vandu.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Page<Role> findByNameContaining(String name,Pageable pageable);
	
	Optional<Role> findByName(String name);
}
