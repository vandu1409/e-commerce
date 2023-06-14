package com.vandu.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;

import com.vandu.model.Notification;
import com.vandu.repository.NotificationRepository;
import com.vandu.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService{

	@Autowired
	private NotificationRepository notificationRepository;

	@Override
	public <S extends Notification> S save(S entity) {
		return notificationRepository.save(entity);
	}

	@Override
	public <S extends Notification> List<S> saveAll(Iterable<S> entities) {
		return notificationRepository.saveAll(entities);
	}

	@Override
	public <S extends Notification> Optional<S> findOne(Example<S> example) {
		return notificationRepository.findOne(example);
	}

	@Override
	public List<Notification> findAll(Sort sort) {
		return notificationRepository.findAll(sort);
	}

	@Override
	public void flush() {
		notificationRepository.flush();
	}

	@Override
	public Page<Notification> findAll(Pageable pageable) {
		return notificationRepository.findAll(pageable);
	}

	@Override
	public <S extends Notification> S saveAndFlush(S entity) {
		return notificationRepository.saveAndFlush(entity);
	}

	@Override
	public <S extends Notification> List<S> saveAllAndFlush(Iterable<S> entities) {
		return notificationRepository.saveAllAndFlush(entities);
	}

	@Override
	public List<Notification> findAll() {
		return notificationRepository.findAll();
	}

	@Override
	public List<Notification> findAllById(Iterable<Long> ids) {
		return notificationRepository.findAllById(ids);
	}

	@Override
	public void deleteInBatch(Iterable<Notification> entities) {
		notificationRepository.deleteInBatch(entities);
	}

	@Override
	public <S extends Notification> Page<S> findAll(Example<S> example, Pageable pageable) {
		return notificationRepository.findAll(example, pageable);
	}

	@Override
	public Optional<Notification> findById(Long id) {
		return notificationRepository.findById(id);
	}

	@Override
	public void deleteAllInBatch(Iterable<Notification> entities) {
		notificationRepository.deleteAllInBatch(entities);
	}

	@Override
	public boolean existsById(Long id) {
		return notificationRepository.existsById(id);
	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Long> ids) {
		notificationRepository.deleteAllByIdInBatch(ids);
	}

	@Override
	public <S extends Notification> long count(Example<S> example) {
		return notificationRepository.count(example);
	}

	@Override
	public <S extends Notification> boolean exists(Example<S> example) {
		return notificationRepository.exists(example);
	}

	@Override
	public void deleteAllInBatch() {
		notificationRepository.deleteAllInBatch();
	}

	@Override
	public Notification getOne(Long id) {
		return notificationRepository.getOne(id);
	}

	@Override
	public <S extends Notification, R> R findBy(Example<S> example,
			Function<FetchableFluentQuery<S>, R> queryFunction) {
		return notificationRepository.findBy(example, queryFunction);
	}

	@Override
	public long count() {
		return notificationRepository.count();
	}

	@Override
	public void deleteById(Long id) {
		notificationRepository.deleteById(id);
	}

	@Override
	public Notification getById(Long id) {
		return notificationRepository.getById(id);
	}

	@Override
	public void delete(Notification entity) {
		notificationRepository.delete(entity);
	}

	@Override
	public Notification getReferenceById(Long id) {
		return notificationRepository.getReferenceById(id);
	}

	@Override
	public void deleteAllById(Iterable<? extends Long> ids) {
		notificationRepository.deleteAllById(ids);
	}

	@Override
	public void deleteAll(Iterable<? extends Notification> entities) {
		notificationRepository.deleteAll(entities);
	}

	@Override
	public <S extends Notification> List<S> findAll(Example<S> example) {
		return notificationRepository.findAll(example);
	}

	@Override
	public <S extends Notification> List<S> findAll(Example<S> example, Sort sort) {
		return notificationRepository.findAll(example, sort);
	}

	@Override
	public void deleteAll() {
		notificationRepository.deleteAll();
	}
}
