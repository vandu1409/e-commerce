package com.vandu.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vandu.enums.OrderStatus;
import com.vandu.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>,JpaSpecificationExecutor<Order>  {
	
	List<Order> findByStatus(OrderStatus orderStatus);
	
	@Query("select o from Order o where o.user.userId = :uid")
	Page<Order> findByUser(Long uid,Pageable pageable);
	
	@Query("select o from Order o where o.createDate = :date ")
	List<Order> findByDate(Date date);
	
	@Query("select o from Order o where MONTH(o.createDate) = :month and o.status = :orderStatus")
	List<Order> findByStatusAndMonth(int month,OrderStatus orderStatus);
	
	@Query("select o from Order o where MONTH(o.createDate) = :month ")
	List<Order> findByMonth(int month);
	
	@Query("select o from Order o where MONTH(o.createDate) = :month and YEAR(o.createDate) = :year and o.status = :orderStatus")
	List<Order> findByStatusAndMonthAndYear(int month,int year,OrderStatus orderStatus);
	
//	@Query("select o from Order o where o.")
	
}
