package com.vandu.controller.admin;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vandu.dto.CategoryDto;
import com.vandu.exception.DuplicateDataException;
import com.vandu.model.Brand;
import com.vandu.model.Category;
import com.vandu.service.CategoryService;
import com.vandu.service.FileSystemStorageService;
import com.vandu.service.ProductService;

import jakarta.servlet.annotation.MultipartConfig;

@RestController
@RequestMapping("admin/category")
@MultipartConfig
public class CategoryApiController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private FileSystemStorageService fileSystemStorageService;

	@Autowired
	private ProductService productService;

	@PostMapping("saveOrUpdate")
	public ResponseEntity<?> saveOrUpdate(@ModelAttribute CategoryDto categoryDto) {
		try {

			Boolean check = true;

			if (categoryDto.getId() != null && !categoryDto.getId().equals("")) {
				System.err.println("đã vào");
				Category currentCategory = categoryService.findById(categoryDto.getId()).orElse(null);

				check = false;

				if (!currentCategory.getCategoryCode().equalsIgnoreCase(categoryDto.getCategoryCode())) {
					check = true;
				}

			}

			if (check) {
				if (productService.checkDuplicateCode(categoryDto.getCategoryCode())) {
					throw new DuplicateDataException("Mã định danh thương hiệu đã tồn tại");
				}

			}
			
			Category category = new Category();

			BeanUtils.copyProperties(categoryDto, category);

			UUID uuid = UUID.randomUUID();

			if (categoryDto.getLogo() != null) {
				String name = fileSystemStorageService.getStorageFileName(categoryDto.getLogo(), uuid.toString());

				fileSystemStorageService.saveFile(categoryDto.getLogo(), name);
				System.err.println(name + "nemae");
				category.setLogo(name);
			} else {
				if (categoryDto.getId() != null) {
					Category currentCategory = categoryService.findById(categoryDto.getId()).orElse(null);
					if (currentCategory != null) {
						category.setLogo(currentCategory.getLogo());
					}
				}
			}

			Category entity = categoryService.save(category);

			BeanUtils.copyProperties(entity, categoryDto);

			return new ResponseEntity<>(categoryDto, HttpStatus.OK);
		} catch (DuplicateDataException e) {
			throw e;
		}

	}

	@GetMapping("getAll")
	public ResponseEntity<?> getAll() {
		return ResponseEntity.ok(categoryService.findAll().stream().map(item -> {
			CategoryDto categoryDto = new CategoryDto();

			BeanUtils.copyProperties(item, categoryDto);

			return categoryDto;
		}).toList());
	}

	@GetMapping("pagination")
	public ResponseEntity<Page<Category>> pagination(@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size, @RequestParam("keyword") String keyword) {
		System.err.println("đã vào phân trang");

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(5);

		Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

		Page<Category> resultPage = keyword.equals("") ? categoryService.findAll(pageable)
				: categoryService.findByNameContaining(keyword, pageable);

		return new ResponseEntity<>(resultPage, HttpStatus.OK);
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<String> update(@PathVariable("id") Long categoryId) {
		categoryService.deleteById(categoryId);
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}

	@GetMapping("edit/{id}")
	public ResponseEntity<CategoryDto> findById(@PathVariable("id") Long categoryId) {

		CategoryDto categoryDto = new CategoryDto();

		Category category = categoryService.findById(categoryId).orElse(null);

		if (category != null) {
			BeanUtils.copyProperties(category, categoryDto);
		}

		return ResponseEntity.ok(categoryDto);
	}

}
