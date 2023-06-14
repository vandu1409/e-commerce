package com.vandu.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vandu.dto.AttributesDto;
import com.vandu.dto.ProductDto;
import com.vandu.exception.DuplicateDataException;
import com.vandu.exception.ProductNotFoundException;
import com.vandu.model.Attributes;
import com.vandu.model.Brand;
import com.vandu.model.Category;
import com.vandu.model.CategoryAttributes;
import com.vandu.model.Product;
import com.vandu.model.ProductAttributes;
import com.vandu.model.ProductImage;
import com.vandu.model.ProductSeries;
import com.vandu.service.BrandService;
import com.vandu.service.FileSystemStorageService;
import com.vandu.service.ProductAttributesService;
import com.vandu.service.ProductImageService;
import com.vandu.service.ProductSeriesService;
import com.vandu.service.ProductService;

import jakarta.servlet.annotation.MultipartConfig;

@RestController
@RequestMapping("/admin/product")
@MultipartConfig
public class ProductApiController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductSeriesService productSeriesService;

	@Autowired
	private FileSystemStorageService fileSystemStorageService;

	@Autowired
	private ProductImageService productImageService;

	@Autowired
	private BrandService brandService;

	@Autowired
	ProductAttributesService productAttributesService;

	@PostMapping(value = "insert")
	public ResponseEntity<ProductDto> insert(@ModelAttribute ProductDto productDto) {

		try {

			checkDuplicateData(productDto);
			
			Product dbProduct = new Product();
			
			if(productDto.getProductId()!=null|| productDto.getProductId().compareTo(0L) != 0) {
					dbProduct = productService.findById(productDto.getProductId()).orElse(null);
			}

			Product entity = productService.saveOrUpdate(productDto);

			BeanUtils.copyProperties(entity, productDto);

			if(dbProduct==null) {
				productAttributesService.addToAttributes(productDto.getListAttributes(), entity);
			}else {
				List<Attributes> listAttributes = dbProduct.getCategory().getCategoryAttributes().stream().map(CategoryAttributes::getAttributes).toList(); 
				productAttributesService.deleteAttributesByCategory(listAttributes, dbProduct);
				
				productAttributesService.addToAttributes(productDto.getListAttributes(), entity);
				
			}

			productImageService.saveImages(productDto.getListImages(), entity);

			return new ResponseEntity<>(productDto, HttpStatus.CREATED);
		} catch (DuplicateDataException e) {
			throw e;
		}
	}

	private void checkDuplicateData(ProductDto productDto) {
		Boolean check = true;

		if (productDto.getProductId() != null && !productDto.getProductId().equals("")) {
			System.err.println("đã vào");
			Product currentProduct = productService.findById(productDto.getProductId()).orElse(null);

			check = false;

			if (!currentProduct.getProductCode().equalsIgnoreCase(productDto.getProductCode())) {
				check = true;
			}

		}

		if (check) {
			if (productService.checkDuplicateCode(productDto.getProductCode())) {
				throw new DuplicateDataException("Mã định danh sản phẩm đã tồn tại");
			}

		}
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<String> delete(@PathVariable("id") Long id) {
		Product product = productService.findById(id).orElse(new Product());

		if (product != null) {
			productService.delete(product);
		}

		return ResponseEntity.ok("Success");
	}

	@GetMapping("getAll")
	public ResponseEntity<Page<Product>> getAll(@RequestParam("keyword") String keyword,
			@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(3);

		Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("createDate").descending());

		Page<Product> list = keyword.equals("") ? productService.findAll(pageable)
				: productService.findByNameContaining(keyword, pageable);

		for (Product product : list) {
			System.err.println("name" + product.getName());
		}

		return ResponseEntity.ok(list);
	}

	@GetMapping("sell/{id}")
	public ResponseEntity<String> sell(@PathVariable("id") Long id) throws ProductNotFoundException {
		Product product = productService.findById(id).orElse(null);

		if (product == null) {
			throw new ProductNotFoundException("Không tìm thấy sản phẩm");
		}
		product.setAvailable(!product.isAvailable());
		productService.save(product);

		return ResponseEntity.ok("Success");
	}

	@GetMapping(value = { "getAttributesByBrand/{id}", "getAttributesByBrand/{id}/{productId}" })
	public ResponseEntity<?> getAttributesByBrand(@PathVariable("id") Long id,
			@PathVariable("productId") Optional<Long> productId) {
		Brand brand = brandService.findById(id).orElse(null);

		System.err.println(productId.orElse(null));
		List<AttributesDto> list = brand.getCategory().getCategoryAttributes().stream().map(item -> {
			AttributesDto attributesDto = new AttributesDto();
			BeanUtils.copyProperties(item.getAttributes(), attributesDto);

			if (productId.isPresent() && productId.get() != null) {
				System.err.println("đã vaia fisdfsfgshdvf");
				ProductAttributes productAttributes = productAttributesService
						.findByProductAndAttributes(productId.get(), item.getAttributes().getId());
				if (productAttributes != null) {
					attributesDto.setProductAttributesValue(productAttributes.getValue());
					System.err.println(productAttributes.getValue());
				}
			}

			return attributesDto;

		}).toList();

		return ResponseEntity.ok(list);
	}
}
