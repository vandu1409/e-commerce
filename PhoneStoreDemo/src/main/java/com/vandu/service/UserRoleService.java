package com.vandu.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import com.vandu.model.Role;
import com.vandu.model.User;
import com.vandu.model.UserRole;

public interface UserRoleService {

	<S extends UserRole> List<S> saveAll(Iterable<S> entities);

	<S extends UserRole> S save(S entity);

	UserRole getReferenceById(Long id);

	UserRole getById(Long id);

	UserRole getOne(Long id);

	<S extends UserRole> List<S> saveAllAndFlush(Iterable<S> entities);

	void flush();

	<S extends UserRole> Optional<S> findOne(Example<S> example);

	Optional<UserRole> findById(Long id);

	<S extends UserRole, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction);

	List<UserRole> findAllById(Iterable<Long> ids);

	List<UserRole> findAll(Sort sort);

	Page<UserRole> findAll(Pageable pageable);

	<S extends UserRole> List<S> findAll(Example<S> example, Sort sort);

	<S extends UserRole> List<S> findAll(Example<S> example);

	<S extends UserRole> Page<S> findAll(Example<S> example, Pageable pageable);

	List<UserRole> findAll();

	boolean existsById(Long id);

	<S extends UserRole> boolean exists(Example<S> example);

	void deleteById(Long id);

	void deleteAllInBatch();

	void deleteAllByIdInBatch(Iterable<Long> ids);

	void deleteAllInBatch(Iterable<UserRole> entities);

	void deleteInBatch(Iterable<UserRole> entities);

	<S extends UserRole> S saveAndFlush(S entity);

	void deleteAllById(Iterable<? extends Long> ids);

	void deleteAll(Iterable<? extends UserRole> entities);

	void deleteAll();

	void delete(UserRole entity);

	<S extends UserRole> long count(Example<S> example);

	long count();

	Optional<UserRole> findByUserAndRole(String username, String rolename);

	List<Role> getRoleByUser(Long userId);

	UserRole saveUserRoles(User user, Role role);

	Optional<UserRole> findByUserAndRole(Long userId, Long roleId);



}
