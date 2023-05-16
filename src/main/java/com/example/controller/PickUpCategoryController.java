package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Category;
import com.example.service.PickUpCategoryService;

/**
 * 子、孫カテゴリー情報取得するコントローラ.
 * 
 * @author kenta_ichiyoshi
 *
 */
//JSON形式で送るためRestController利用
@RestController
@RequestMapping("/pick-up-category-list")
public class PickUpCategoryController {
	@Autowired
	private PickUpCategoryService categoryService;

	/**
	 * 子カテゴリーを取得する.
	 * 
	 * @param parentId 親id
	 * @return 子カテゴリーをつめたmap
	 */
	@GetMapping("/child-category")
	public Map<String, List<Category>> pickUpChildCategory(Integer parentId) {
		Map<String, List<Category>> map = new HashMap<>();
		List<Category> categoryList = categoryService.pickUpCategoryListByParentIdAndDepth(parentId, 1);
		map.put("childCategoryList", categoryList);
		return map;
	}

	/**
	 * 孫カテゴリーを取得する.
	 * 
	 * @param childId 子id
	 * @return 孫カテゴリーをつめたmap
	 */
	@GetMapping("/grand-child-category")
	public Map<String, List<Category>> pickUpGrandChildCategory(Integer childId) {
		Map<String, List<Category>> map = new HashMap<>();
		List<Category> categoryList = categoryService.pickUpCategoryListByParentIdAndDepth(childId, 2);
		map.put("grandChildCategoryList", categoryList);
		return map;
	}
}
