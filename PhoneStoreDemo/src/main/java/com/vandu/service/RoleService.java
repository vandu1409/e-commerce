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

public interface RoleService {

	<S extends Role> List<S> saveAll(Iterable<S> entities);

	<S extends Role> S save(S entity);

	Role getReferenceById(Long id);

	Role getById(Long id);

	Role getOne(Long id);

	<S extends Role> List<S> saveAllAndFlush(Iterable<S> entities);

	void flush();

	<S extends Role> Optional<S> findOne(Example<S> example);

	Optional<Role> findById(Long id);

	<S extends Role, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction);

	List<Role> findAllById(Iterable<Long> ids);

	List<Role> findAll(Sort sort);

	Page<Role> findAll(Pageable pageable);

	<S extends Role> List<S> findAll(Example<S> example, Sort sort);

	<S extends Role> List<S> findAll(Example<S> example);

	<S extends Role> Page<S> findAll(Example<S> example, Pageable pageable);

	List<Role> findAll();

	boolean existsById(Long id);

	<S extends Role> boolean exists(Example<S> example);

	void deleteById(Long id);

	void deleteAllInBatch();

	void deleteAllByIdInBatch(Iterable<Long> ids);

	void deleteAllInBatch(Iterable<Role> entities);

	void deleteInBatch(Iterable<Role> entities);

	<S extends Role> S saveAndFlush(S entity);

	void deleteAllById(Iterable<? extends Long> ids);

	void deleteAll(Iterable<? extends Role> entities);

	void deleteAll();

	void delete(Role entity);

	<S extends Role> long count(Example<S> example);

	long count();


	Page<Role> findByName(String name, Pageable pageable);

	Optional<Role> findByName(String name);



}
