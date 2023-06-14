package com.vandu.controller.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.vandu.dto.BrandDto;
import com.vandu.fillter.FilterProducts;
import com.vandu.helper.Views;
import com.vandu.model.Brand;
import com.vandu.model.Color;
import com.vandu.model.Product;
import com.vandu.model.ProductDetails;
import com.vandu.model.ProductSeries;
import com.vandu.model.ProductVersion;
import com.vandu.service.BrandService;
import com.vandu.service.ProductDetailsService;
import com.vandu.service.ProductSeriesService;
import com.vandu.service.ProductService;
import com.vandu.service.ProductVersionService;

@RestController()
@RequestMapping("dtdd")
public class ProductSiteApiController {
	@Autowired
	private ProductService productService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private ProductSeriesService productSeriesService;

	@Autowired
	private ProductDetailsService productDetailsService;

	@Autowired
	private ProductVersionService productVersionService;

	@GetMapping("getAll")
	public ResponseEntity<Page<Product>> getAll(@RequestParam("page") Optional<Integer> page,
			@RequestParam("brandCode") Optional<String> brandCodeList,
			@RequestParam("seriesCode") Optional<String> sCode, @RequestParam("categoryCode") Optional<String> cCode,
			@RequestParam("sort") Optional<Integer> clientSortOrder,
			@RequestParam(value = "minPrice", required = false) String min,
			@RequestParam(value = "maxPrice", required = false) String max) {
		int pageSize = page.orElse(1);
		String seriesCode = sCode.orElseGet(() -> null);
		String categoryCode = cCode.orElse(null);

		if (seriesCode.equals("")) {
			seriesCode = null;
		}

		int sort = clientSortOrder.orElse(1);
		Double minPrice = null;
		Double maxPrice = null;

		System.err.println(seriesCode + "serr");

		if (min != null && !min.equals("null")) {
			minPrice = Double.parseDouble(min);
		}

		if (max != null && !max.equals("null")) {
			maxPrice = Double.parseDouble(max);
		}

		String brandCodes = brandCodeList.orElse(null);

		Sort sortObj = null;
		if (sort == 1) {
			sortObj = Sort.by("price").ascending();
		} else if (sort == 2) {
			sortObj = Sort.by("price").descending();

		} else {
			sortObj = Sort.by("price").ascending();
		}

		Pageable pageable = PageRequest.of(0, pageSize * 8, sortObj);
		Specification<Product> spec = FilterProducts.filterProducts(categoryCode, brandCodes, seriesCode, minPrice,
				maxPrice);

		Page<Product> list = productService.findAll(spec, pageable);

		return ResponseEntity.ok(list);

	}

	@GetMapping("getAllBrand")
//	@JsonView(Views.Public.class)
	public ResponseEntity<List<Brand>> getAllBrand() {
		return ResponseEntity.ok(brandService.findAll());
	}

	@GetMapping("getAllByCategory/{categoryCode}")
	public ResponseEntity<List<BrandDto>> getAllByCategory(@PathVariable("categoryCode") String categoryCode) {
		return ResponseEntity.ok(brandService.findAllByCategoryCode(categoryCode).stream().map(item -> {
			BrandDto brandDto = new BrandDto();

			BeanUtils.copyProperties(item, brandDto);

			return brandDto;
		}).toList());
	}

	@GetMapping("getSeries/{brandCode}")
	public ResponseEntity<List<ProductSeries>> getAllSeries(@PathVariable("brandCode") String brandCode) {

		return ResponseEntity.ok(productSeriesService.findAllByBrandCode(brandCode));
	}

	@GetMapping("getColorByVersion/{versionId}")
	public ResponseEntity<List<Color>> getColorByVersion(@PathVariable("versionId") Long versionId) {

		return ResponseEntity.ok(productDetailsService.findColorByVersion(versionId));
	}

	@GetMapping("getVersionByProduct/{productId}")
	public ResponseEntity<List<ProductVersion>> getVersionByProduct(@PathVariable("productId") Long id) {
		Product product = productService.findById(id).orElse(null);

		List<ProductVersion> list = product.getProductVersions();

		return ResponseEntity.ok(list);

	}

	@GetMapping("getProductDetailsByVersion/{versionId}")
	@JsonView(Views.Public.class)
	public ResponseEntity<List<ProductDetails>> getProductDetailsByVersion(@PathVariable("versionId") Long versionId) {

		System.err.println(versionId);

		ProductVersion productVersion = productVersionService.findById(versionId).orElse(null);

		List<ProductDetails> list = new ArrayList<>();

		if (productVersion != null) {
			list = productVersion.getProductDetails();

		}

		return ResponseEntity.ok(list);
	}

	@GetMapping("getProduct/{productCode}")
//	@JsonView(Views.Public.class)
	public ResponseEntity<Product> getProduct(@PathVariable("productCode") String productCode) {
		return ResponseEntity.ok(productService.findByProductCode(productCode).get());
	}

	@GetMapping("searchByQuery")
	public ResponseEntity<?> search(@RequestParam("query") String query, @RequestParam("page") Optional<Integer> page,
			@RequestParam("sort") int sort) {

		int curentPage = page.orElse(1);

		Sort sortObj = null;
		if (sort == 1) {
			sortObj = Sort.by("price").ascending();
		} else if (sort == 2) {
			sortObj = Sort.by("price").descending();

		} else {
			sortObj = Sort.by("price").ascending();
		}
		
		Pageable pageable = PageRequest.of(0, curentPage * 10, sortObj);
		
		Page<Product> list = query.equals("")?productService.findAll(pageable):productService.findByKeyword(query, pageable);
		
		return ResponseEntity.ok(list);
	}

}
