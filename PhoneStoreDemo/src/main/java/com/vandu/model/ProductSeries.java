package com.vandu.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.vandu.helper.Views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
//@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
//@JsonView(Views.Public.class)
public class ProductSeries {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productSeriesId;
	
	@Column(columnDefinition = "nvarchar(500)")
	private String name;
	
	@Column(columnDefinition = "nvarchar(500)")
	private String seriesCode;
	
	
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "brandId")
//	@JsonIgnore
	
	private Brand brand;
	
	@OneToMany(mappedBy = "productSeries")
//	@JsonIgnore
	private List<Product> products;
	
//	public Product getProduct() {
//		return this.getProducts().get(0);
//	}
	
//	public Brand getBrandBySeries() {
//		return this.getBrand();
//	}
//	
//	public List<Product> getProductBySeries(){
//		return this.getProducts();
//	}
}
