package com.vandu.service.Impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;

import com.vandu.dto.ProductDto;
import com.vandu.model.Brand;
import com.vandu.model.Category;
import com.vandu.model.Product;
import com.vandu.model.ProductSeries;
import com.vandu.repository.ProductRepository;
import com.vandu.service.BrandService;
import com.vandu.service.CategoryService;
import com.vandu.service.FileSystemStorageService;
import com.vandu.service.ProductSeriesService;
import com.vandu.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private BrandService brandService;

	@Autowired
	private ProductSeriesService productSeriesService;

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private FileSystemStorageService fileSystemStorageService;

	@Override
	public Page<Product> findByKeyword(String key, Pageable pageable) {
		return productRepository.findByKeyword(key, pageable);
	}

	@Override
	public Boolean checkDuplicateCode(String code) {
		return productRepository.findByProductCode(code).isPresent() || brandService.findByBrandCode(code).isPresent()
				|| productSeriesService.findBySeriesCode(code).isPresent()
				|| categoryService.findByCategoryCode(code).isPresent();

	}
	
	
	@Override
	public Product saveOrUpdate(ProductDto productDto) {
		Product product = new Product();
		BeanUtils.copyProperties(productDto, product);
	

		ProductSeries productSeries = new ProductSeries();

		productSeries.setProductSeriesId(productDto.getProductSeriesId());

		if (productDto.getProductSeriesId() != null) {
			Category category = productSeriesService.findById(productDto.getProductSeriesId()).get().getBrand()
					.getCategory();
			if (category.getId() != null) {
				product.setCategory(category);
			}
		}

		System.err.println(productSeries.getProductSeriesId());

		if (productDto.getProductSeriesId() != null) {
			product.setProductSeries(productSeries);
		}

		UUID uuid = UUID.randomUUID();

		if (productDto.getFileImage() != null) {

			String name = fileSystemStorageService.getStorageFileName(productDto.getFileImage(), uuid.toString());
			fileSystemStorageService.saveFile(productDto.getFileImage(), name);

			product.setImage(name);
		} else {
			if (product.getProductId() != null) {
				Product currentProduct = productRepository.findById(productDto.getProductId()).orElse(null);
				if (currentProduct != null) {
					product.setImage(currentProduct.getImage());
				}
			}
		}
		

		if (productDto.getProductId() == null) {
			product.setCreateDate(new Date());
		}else {	
			product.setUpdateDate(new Date());
			Product currentProduct = productRepository.findById(productDto.getProductId()).orElse(null);
			if (currentProduct != null) {
				product.setCreateDate(currentProduct.getCreateDate());
			}
		}
		
		Product entity = productRepository.save(product);
		
		return entity;
	}

	@Override
	public long count() {
		return productRepository.count();
	}

	@Override
	public <S extends Product> long count(Example<S> example) {
		return productRepository.count(example);
	}

	public List<Product> findByCategoryCode(String categoryCode) {
		return productRepository.findByCategoryCode(categoryCode);
	}

	@Override
	public Optional<Product> findByProductCode(String productCode) {
		return productRepository.findByProductCode(productCode);
	}

	@Override
	public List<Product> findRandomProducts(int n) {

		List<Product> randomProducts = findAll();
		Collections.shuffle(randomProducts);

		return randomProducts.stream().limit(n).collect(Collectors.toList());
	}
	
	@Override
	public List<Product> findRandomByCategoryCode(int n,String categoryCode) {

		List<Product> randomProducts = findByCategoryCode(categoryCode);
		Collections.shuffle(randomProducts);

		return randomProducts.stream().limit(n).collect(Collectors.toList());
	}

	@Override
	public Page<Product> findAll(Specification<Product> spec, Pageable pageable) {
		return productRepository.findAll(spec, pageable);
	}

	@Override
	public void delete(Product entity) {
		productRepository.delete(entity);
	}

	@Override
	public Page<Product> findByNameContaining(String name, Pageable pageable) {
		return productRepository.findByNameContaining(name, pageable);
	}

	@Override
	public void deleteAll() {
		productRepository.deleteAll();
	}

	@Override
	public void deleteAll(Iterable<? extends Product> entities) {
		productRepository.deleteAll(entities);
	}

	@Override
	public void deleteAllById(Iterable<? extends Long> ids) {
		productRepository.deleteAllById(ids);
	}

	@Override
	public <S extends Product> S saveAndFlush(S entity) {
		return productRepository.saveAndFlush(entity);
	}

	@Override
	public void deleteInBatch(Iterable<Product> entities) {
		productRepository.deleteInBatch(entities);
	}

	@Override
	public void deleteAllInBatch(Iterable<Product> entities) {
		productRepository.deleteAllInBatch(entities);
	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Long> ids) {
		productRepository.deleteAllByIdInBatch(ids);
	}

	@Override
	public void deleteAllInBatch() {
		productRepository.deleteAllInBatch();
	}

	@Override
	public void deleteById(Long id) {
		productRepository.deleteById(id);
	}

	@Override
	public <S extends Product> boolean exists(Example<S> example) {
		return productRepository.exists(example);
	}

	@Override
	public boolean existsById(Long id) {
		return productRepository.existsById(id);
	}

	@Override
	public List<Product> findAll() {
		return productRepository.findAll();
	}

	@Override
	public <S extends Product> Page<S> findAll(Example<S> example, Pageable pageable) {
		return productRepository.findAll(example, pageable);
	}

	@Override
	public <S extends Product> List<S> findAll(Example<S> example) {
		return productRepository.findAll(example);
	}

	@Override
	public <S extends Product> List<S> findAll(Example<S> example, Sort sort) {
		return productRepository.findAll(example, sort);
	}

	@Override
	public Page<Product> findAll(Pageable pageable) {
		return productRepository.findAll(pageable);
	}

	@Override
	public List<Product> findAll(Sort sort) {
		return productRepository.findAll(sort);
	}

	@Override
	public List<Product> findAllById(Iterable<Long> ids) {
		return productRepository.findAllById(ids);
	}

	@Override
	public <S extends Product, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
		return productRepository.findBy(example, queryFunction);
	}

	@Override
	public Optional<Product> findById(Long id) {
		return productRepository.findById(id);
	}

	@Override
	public <S extends Product> Optional<S> findOne(Example<S> example) {
		return productRepository.findOne(example);
	}

	@Override
	public void flush() {
		productRepository.flush();
	}

	@Override
	public <S extends Product> List<S> saveAllAndFlush(Iterable<S> entities) {
		return productRepository.saveAllAndFlush(entities);
	}

	@Override
	public Product getOne(Long id) {
		return productRepository.getOne(id);
	}

	@Override
	public Product getById(Long id) {
		return productRepository.getById(id);
	}

	@Override
	public Product getReferenceById(Long id) {
		return productRepository.getReferenceById(id);
	}

	@Override
	public <S extends Product> S save(S entity) {
		return productRepository.save(entity);
	}

	@Override
	public <S extends Product> List<S> saveAll(Iterable<S> entities) {
		return productRepository.saveAll(entities);
	}

}
