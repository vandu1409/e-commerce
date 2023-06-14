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

import com.vandu.model.Role;
import com.vandu.model.User;
import com.vandu.model.UserRole;
import com.vandu.repository.UserRoleRepository;
import com.vandu.service.UserRoleService;

@Service
public class UserRoleServiceImpl implements UserRoleService {

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Override
	public long count() {
		return userRoleRepository.count();
	}

	@Override
	public UserRole saveUserRoles(User user, Role role) {
		UserRole userRole = new UserRole();
		userRole.setUser(user);
		userRole.setRole(role);
		return save(userRole);
	}

	@Override
	public List<Role> getRoleByUser(Long userId) {
		return userRoleRepository.getRoleByUser(userId);
	}



	@Override
	public Optional<UserRole> findByUserAndRole(Long userId, Long roleId) {
		return userRoleRepository.findByUserAndRole(userId, roleId);
	}

	@Override
	public <S extends UserRole> long count(Example<S> example) {
		return userRoleRepository.count(example);
	}

	@Override
	public void delete(UserRole entity) {
		userRoleRepository.delete(entity);
	}

	@Override
	public void deleteAll() {
		userRoleRepository.deleteAll();
	}

	@Override
	public void deleteAll(Iterable<? extends UserRole> entities) {
		userRoleRepository.deleteAll(entities);
	}

	@Override
	public void deleteAllById(Iterable<? extends Long> ids) {
		userRoleRepository.deleteAllById(ids);
	}

	@Override
	public <S extends UserRole> S saveAndFlush(S entity) {
		return userRoleRepository.saveAndFlush(entity);
	}

	@Override
	public void deleteInBatch(Iterable<UserRole> entities) {
		userRoleRepository.deleteInBatch(entities);
	}

	@Override
	public void deleteAllInBatch(Iterable<UserRole> entities) {
		userRoleRepository.deleteAllInBatch(entities);
	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Long> ids) {
		userRoleRepository.deleteAllByIdInBatch(ids);
	}

	@Override
	public void deleteAllInBatch() {
		userRoleRepository.deleteAllInBatch();
	}

	@Override
	public void deleteById(Long id) {
		userRoleRepository.deleteById(id);
	}

	@Override
	public <S extends UserRole> boolean exists(Example<S> example) {
		return userRoleRepository.exists(example);
	}

	@Override
	public boolean existsById(Long id) {
		return userRoleRepository.existsById(id);
	}

	@Override
	public List<UserRole> findAll() {
		return userRoleRepository.findAll();
	}

	@Override
	public <S extends UserRole> Page<S> findAll(Example<S> example, Pageable pageable) {
		return userRoleRepository.findAll(example, pageable);
	}

	@Override
	public <S extends UserRole> List<S> findAll(Example<S> example) {
		return userRoleRepository.findAll(example);
	}

	@Override
	public <S extends UserRole> List<S> findAll(Example<S> example, Sort sort) {
		return userRoleRepository.findAll(example, sort);
	}

	@Override
	public Page<UserRole> findAll(Pageable pageable) {
		return userRoleRepository.findAll(pageable);
	}

	@Override
	public List<UserRole> findAll(Sort sort) {
		return userRoleRepository.findAll(sort);
	}

	@Override
	public List<UserRole> findAllById(Iterable<Long> ids) {
		return userRoleRepository.findAllById(ids);
	}

	@Override
	public <S extends UserRole, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
		return userRoleRepository.findBy(example, queryFunction);
	}

	@Override
	public Optional<UserRole> findById(Long id) {
		return userRoleRepository.findById(id);
	}

	@Override
	public <S extends UserRole> Optional<S> findOne(Example<S> example) {
		return userRoleRepository.findOne(example);
	}

	@Override
	public void flush() {
		userRoleRepository.flush();
	}

	@Override
	public <S extends UserRole> List<S> saveAllAndFlush(Iterable<S> entities) {
		return userRoleRepository.saveAllAndFlush(entities);
	}

	@Override
	public UserRole getOne(Long id) {
		return userRoleRepository.getOne(id);
	}

	@Override
	public UserRole getById(Long id) {
		return userRoleRepository.getById(id);
	}

	@Override
	public UserRole getReferenceById(Long id) {
		return userRoleRepository.getReferenceById(id);
	}

	@Override
	public <S extends UserRole> S save(S entity) {

		UserRole userRole = findByUserAndRole(entity.getUser().getUsername(), entity.getRole().getName()).orElse(null);

		if (userRole == null) {
			return userRoleRepository.save(entity);
		}

		return null;
	}

	public void deleteRoleByUser(String username, String rolename) {
		UserRole userRole = findByUserAndRole(username, rolename).orElse(null);

		if (userRole != null) {
			userRoleRepository.delete(userRole);
		}

	}

	public <S extends UserRole> S update(S entity) {

		UserRole userRole = findByUserAndRole(entity.getUser().getUsername(), entity.getRole().getName()).orElse(null);

		return userRoleRepository.save(entity);
	}

	@Override
	public <S extends UserRole> List<S> saveAll(Iterable<S> entities) {
		return userRoleRepository.saveAll(entities);
	}

	@Override
	public Optional<UserRole> findByUserAndRole(String username, String rolename) {
		return userRoleRepository.findByUserAndRole(username, rolename);
	}

}
