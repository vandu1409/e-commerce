package com.vandu.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.vandu.enums.DiscountType;
import com.vandu.enums.OrderStatus;
import com.vandu.helper.Views;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonView(Views.Public.class)
public class ProductDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@JsonView(Views.Admin.class)
	private Long productDetailsId;

	@JsonView(Views.Admin.class)
	private Long quantity;

	@JsonView(Views.Admin.class)
	private Double importPrice;

	private Double price;
	
	private String image;

	@JsonView(Views.Admin.class)
	private Date createDate;

	@JsonView(Views.Admin.class)
	private Date updateDate;
	
	
	@JsonView(Views.Admin.class)
	private boolean isAvailable;

	@ManyToOne
	@JoinColumn(name = "productVersionId")
	@JsonManagedReference
	private ProductVersion productVersion;

	@ManyToOne
	@JoinColumn(name = "colorId")
	@JsonManagedReference
	private Color color;

	@OneToMany(mappedBy = "productDetails")
	@JsonIgnore
	private List<Cart> carts;
	
	@OneToMany(mappedBy = "productDetails")
	@JsonIgnore
	private List<OrderItem> orderItems;
	
	private boolean checkOutOfStockProducts() {
		return this.getQuantity()<=0 ? true:false;
	}
	
	public String toString() {
		return this.getProductVersion().getProduct().getName() + " "+this.getProductVersion().getVersionName() + " " + this.getColor().getName();
	}
	
	public Double getDiscountedPrice() {
		Double discountedPrice = this.getPrice(); // Lấy giá gốc của sản phẩm
		LocalDate today = LocalDate.now();
		for (ProductDiscount pd : this.getProductVersion().getProduct().getProductDiscounts()) {

			
			if (!today.isBefore(pd.getDiscount().getStartDate()) && !today.isAfter(pd.getDiscount().getEndDate())) {
				
				if (pd.getDiscount().getDiscountType().equals(DiscountType.DISCOUNTBYPERCENT)) {
					discountedPrice = discountedPrice - (discountedPrice * (pd.getDiscount().getDiscountValue() / 100));
					
					
					
				} else {
					discountedPrice = discountedPrice - pd.getDiscount().getDiscountValue();
				}
			}
		}

		return discountedPrice;

	}
	
	public int getQuantitySold() {
		return (int) this.getOrderItems().stream().filter(item->item.getOrder().getStatus().equals(OrderStatus.DELIVERED)).	mapToLong(OrderItem :: getQuantity).sum();
	}

}
