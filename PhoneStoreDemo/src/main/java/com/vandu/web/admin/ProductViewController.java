package com.vandu.web.admin;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vandu.dto.BrandDto;
import com.vandu.dto.CategoryDto;
import com.vandu.dto.DiscountDto;
import com.vandu.dto.ProductDto;
import com.vandu.model.Brand;
import com.vandu.model.Category;
import com.vandu.model.Product;
import com.vandu.model.ProductSeries;
import com.vandu.service.BrandService;
import com.vandu.service.CategoryService;
import com.vandu.service.DiscountService;
import com.vandu.service.ProductSeriesService;
import com.vandu.service.ProductService;

@Controller
@RequestMapping("admin/product")
public class ProductViewController {

	@Autowired
	private BrandService brandService;

	@Autowired
	private ProductSeriesService productSeriesService;

	@Autowired
	private DiscountService discountService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;

	@ModelAttribute("listBrand")
	public List<BrandDto> getAllBrand() {

		return brandService.findAll().stream().map(item -> {
			BrandDto brandDto = new BrandDto();

			BeanUtils.copyProperties(item, brandDto);
			brandDto.setBrandName(item.getBrandName());

			return brandDto;
		}).toList();
	}

	@ModelAttribute("listCategory")
	public List<CategoryDto> getAllCategory() {
		return categoryService.findAll().stream().map(item ->{
			CategoryDto categoryDto = new CategoryDto();
			
			BeanUtils.copyProperties(item, categoryDto);
			
			return categoryDto;
		}).toList();
	}

	@GetMapping("add")
	public String add(ModelMap modelMap) {
		ProductDto productDto = (ProductDto) modelMap.get("productDto");

		if (productDto != null) {
			productDto.setEdit(false);
			System.err.println(productDto.getProductId()+"là product ì");
			modelMap.addAttribute("productDto", productDto);
		} else {
			ProductDto product = new ProductDto();
			product.setEdit(true);
			modelMap.addAttribute("productDto", product);
		}
		return "admin/product";
	}

	@GetMapping("list")
	public String list() {
		return "admin/listProduct";

	}

	@GetMapping("edit/{productId}")
	public String edit(@PathVariable("productId") Long id, RedirectAttributes redirectAttributes) {
		Product product = productService.findById(id).orElse(null);
		ProductDto productDto = new ProductDto();

	
		BeanUtils.copyProperties(product, productDto);
		productDto.setBrandId(product.getProductSeries().getBrand().getBrandId());
		productDto.setProductSeriesId(product.getProductSeries().getProductSeriesId());
	
		
//		System.err.println(productDto.getProductSeriesId()+"shgdsgfsdhf"+productDto.getProductId());;

		redirectAttributes.addFlashAttribute("productDto", productDto);

		return "redirect:/admin/product/add";

	}

}
