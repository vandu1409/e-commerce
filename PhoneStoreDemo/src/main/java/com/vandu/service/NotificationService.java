package com.vandu.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import com.vandu.model.Notification;

public interface NotificationService {

	void deleteAll();

	<S extends Notification> List<S> findAll(Example<S> example, Sort sort);

	<S extends Notification> List<S> findAll(Example<S> example);

	void deleteAll(Iterable<? extends Notification> entities);

	void deleteAllById(Iterable<? extends Long> ids);

	Notification getReferenceById(Long id);

	void delete(Notification entity);

	Notification getById(Long id);

	void deleteById(Long id);

	long count();

	<S extends Notification, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction);

	Notification getOne(Long id);

	void deleteAllInBatch();

	<S extends Notification> boolean exists(Example<S> example);

	<S extends Notification> long count(Example<S> example);

	void deleteAllByIdInBatch(Iterable<Long> ids);

	boolean existsById(Long id);

	void deleteAllInBatch(Iterable<Notification> entities);

	Optional<Notification> findById(Long id);

	<S extends Notification> Page<S> findAll(Example<S> example, Pageable pageable);

	void deleteInBatch(Iterable<Notification> entities);

	List<Notification> findAllById(Iterable<Long> ids);

	List<Notification> findAll();

	<S extends Notification> List<S> saveAllAndFlush(Iterable<S> entities);

	<S extends Notification> S saveAndFlush(S entity);

	Page<Notification> findAll(Pageable pageable);

	void flush();

	List<Notification> findAll(Sort sort);

	<S extends Notification> Optional<S> findOne(Example<S> example);

	<S extends Notification> List<S> saveAll(Iterable<S> entities);

	<S extends Notification> S save(S entity);

}
