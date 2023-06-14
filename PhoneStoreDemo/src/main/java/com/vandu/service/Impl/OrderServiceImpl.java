package com.vandu.service.Impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;

import com.vandu.dto.response.AddressResponseDto;
import com.vandu.dto.response.OrderItemResponseDto;
import com.vandu.dto.response.OrderResponseDto;
import com.vandu.dto.response.PaymentsResponseDto;
import com.vandu.dto.response.UserResponseDto;
import com.vandu.dto.response.VoucherResponseDto;
import com.vandu.enums.OrderStatus;
import com.vandu.model.Cart;
import com.vandu.model.Order;
import com.vandu.model.ProductDetails;
import com.vandu.repository.OrderRepository;
import com.vandu.repository.ProductDetailsRepository;
import com.vandu.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Override
	public List<Order> findByStatusAndMonthAndYear(int month, int year, OrderStatus orderStatus) {
		return orderRepository.findByStatusAndMonthAndYear(month, year, orderStatus);
	}

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ProductDetailsRepository productDetailsRepository;

	@Override
	public List<Order> findByStatusAndMonth(int month, OrderStatus orderStatus) {
		return orderRepository.findByStatusAndMonth(month, orderStatus);
	}

	@Override
	public List<Order> findByMonth(int month) {
		return orderRepository.findByMonth(month);
	}

	@Override
	public Page<Order> findByUser(Long uid, Pageable pageable) {
	
		return orderRepository.findByUser(uid, pageable);
	}
	
	@Override
	public Order cancelOrder(Order order) {
	
		order.getOrderItems().stream().forEach(orderItem->{
			ProductDetails productDetails = orderItem.getProductDetails();
			productDetails.setQuantity(productDetails.getQuantity()+orderItem.getQuantity());
			
			productDetailsRepository.save(productDetails);
		});
		
		order.setStatus(OrderStatus.CANELED);
		return save(order);
	}
	
	@Override
	public Order restoreOrder(Order order) {

		if (order != null && order.getStatus().equals(OrderStatus.CANELED)) {
			order.getOrderItems().stream().forEach(orderItem->{
				ProductDetails productDetails = orderItem.getProductDetails();
				productDetails.setQuantity(productDetails.getQuantity()-orderItem.getQuantity());
				
				productDetailsRepository.save(productDetails);
			});
			
			order.setStatus(OrderStatus.PENDING);
			return save(order);
		}
		
		return null;
	}

	public boolean checkQuantity(List<Cart> list) {
	
		boolean check = false;
		
		for (Cart cart : list) {
			Long quantity = cart.getProductDetails().getQuantity() - cart.getQuantity();
			if(quantity<0) {
				check= true;
			}
		}
		
		return check;
		
	}
	
	@Override
	public OrderResponseDto convertToOrderResponseDto(Order item) {
		OrderResponseDto orderResponseDto = new OrderResponseDto();

		BeanUtils.copyProperties(item, orderResponseDto);

		if (item.getVoucher() != null) {
			orderResponseDto.setVoucher(new VoucherResponseDto());
			BeanUtils.copyProperties(item.getVoucher(), orderResponseDto.getVoucher());
		}

		orderResponseDto.setUser(new UserResponseDto());
		BeanUtils.copyProperties(item.getUser(), orderResponseDto.getUser());

		orderResponseDto.setAddress(new AddressResponseDto());
		BeanUtils.copyProperties(item.getAddress(), orderResponseDto.getAddress());

		List<OrderItemResponseDto> orderItems = item.getOrderItems().stream().map(orderItem -> {
			OrderItemResponseDto orderItemResponseDto = new OrderItemResponseDto();

			BeanUtils.copyProperties(orderItem, orderItemResponseDto);
			orderItemResponseDto.setProductDetailsName(orderItem.getProductDetails().toString());
			orderItemResponseDto.setPrice(orderItem.getTotalPrice() / orderItem.getQuantity());
			orderItemResponseDto.setImage(orderItem.getProductDetails().getImage());

			return orderItemResponseDto;
		}).toList();

		orderResponseDto.setOrderItems(orderItems);

		if (item.getPayments().size() > 0) {
			orderResponseDto.setPayments(new PaymentsResponseDto());
			BeanUtils.copyProperties(item.getPayments().get(0), orderResponseDto.getPayments());
		}

// 

		return orderResponseDto;
	}
	
	@Override
	public Page<Order> findAllOrdersWithSpecificationAndPageable(Specification<Order> specification, Pageable pageable) {
	    return orderRepository.findAll(specification, pageable);
	}
	
	@Override
	public <S extends Order> S save(S entity) {
		return orderRepository.save(entity);
	}

	@Override
	public <S extends Order> List<S> saveAll(Iterable<S> entities) {
		return orderRepository.saveAll(entities);
	}

	@Override
	public <S extends Order> Optional<S> findOne(Example<S> example) {
		return orderRepository.findOne(example);
	}

	@Override
	public List<Order> findAll(Sort sort) {
		return orderRepository.findAll(sort);
	}

	@Override
	public void flush() {
		orderRepository.flush();
	}

	@Override
	public Page<Order> findAll(Pageable pageable) {
		return orderRepository.findAll(pageable);
	}

	@Override
	public <S extends Order> S saveAndFlush(S entity) {
		return orderRepository.saveAndFlush(entity);
	}

	@Override
	public <S extends Order> List<S> saveAllAndFlush(Iterable<S> entities) {
		return orderRepository.saveAllAndFlush(entities);
	}

	@Override
	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	@Override
	public List<Order> findAllById(Iterable<Long> ids) {
		return orderRepository.findAllById(ids);
	}

	@Override
	public void deleteInBatch(Iterable<Order> entities) {
		orderRepository.deleteInBatch(entities);
	}

	@Override
	public <S extends Order> Page<S> findAll(Example<S> example, Pageable pageable) {
		return orderRepository.findAll(example, pageable);
	}

	@Override
	public Optional<Order> findById(Long id) {
		return orderRepository.findById(id);
	}

	@Override
	public void deleteAllInBatch(Iterable<Order> entities) {
		orderRepository.deleteAllInBatch(entities);
	}

	@Override
	public boolean existsById(Long id) {
		return orderRepository.existsById(id);
	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Long> ids) {
		orderRepository.deleteAllByIdInBatch(ids);
	}

	@Override
	public <S extends Order> long count(Example<S> example) {
		return orderRepository.count(example);
	}

	@Override
	public <S extends Order> boolean exists(Example<S> example) {
		return orderRepository.exists(example);
	}

	@Override
	public void deleteAllInBatch() {
		orderRepository.deleteAllInBatch();
	}

	@Override
	public Order getOne(Long id) {
		return orderRepository.getOne(id);
	}

	@Override
	public <S extends Order, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
		return orderRepository.findBy(example, queryFunction);
	}

	@Override
	public long count() {
		return orderRepository.count();
	}

	@Override
	public void deleteById(Long id) {
		orderRepository.deleteById(id);
	}

	@Override
	public Order getById(Long id) {
		return orderRepository.getById(id);
	}

	@Override
	public void delete(Order entity) {
		orderRepository.delete(entity);
	}

	@Override
	public Order getReferenceById(Long id) {
		return orderRepository.getReferenceById(id);
	}

	@Override
	public void deleteAllById(Iterable<? extends Long> ids) {
		orderRepository.deleteAllById(ids);
	}

	@Override
	public List<Order> findByStatus(int status) {
		OrderStatus orderStatus = null;
		
		switch (status) {
		case 0: {
			orderStatus = null;
			break;
		}
		case 1: {
			orderStatus = OrderStatus.PENDING;
			break;
		}
		case 2: {
			orderStatus = OrderStatus.SHIPING;
			break;
		}
		case 3: {
			orderStatus = OrderStatus.DELIVERED;
			break;
		}
		case 4: {
			orderStatus = OrderStatus.CANELED;
			break;
		}

		}
		if(orderStatus==null) {
			return findAll();
		}
		
		return orderRepository.findByStatus(orderStatus);
	}

	@Override
	public void deleteAll(Iterable<? extends Order> entities) {
		orderRepository.deleteAll(entities);
	}

	@Override
	public <S extends Order> List<S> findAll(Example<S> example) {
		return orderRepository.findAll(example);
	}

	@Override
	public <S extends Order> List<S> findAll(Example<S> example, Sort sort) {
		return orderRepository.findAll(example, sort);
	}

	@Override
	public void deleteAll() {
		orderRepository.deleteAll();
	}
	
	public Page<Order> findAll(Specification<Order> specification,Pageable pageable){
		return orderRepository.findAll(specification, pageable);
	}
	
	
}
