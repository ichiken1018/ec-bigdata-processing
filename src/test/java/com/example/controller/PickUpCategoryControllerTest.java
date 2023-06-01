package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.domain.Category;
import com.example.service.PickUpCategoryService;

class PickUpCategoryControllerTest {
	
	@Mock
	private PickUpCategoryService service;
	@InjectMocks
	private PickUpCategoryController controller;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testPickUpChildCategory() {
		Integer parentId = 1;
		List<Category>categoryList=new ArrayList<>();
		when(service.pickUpCategoryListByParentIdAndDepth(parentId, 1)).thenReturn(categoryList);
		Map<String,List<Category>>result = controller.pickUpChildCategory(parentId);
		assertEquals(1,result.size());
		assertEquals(categoryList,result.get("childCategoryList"));
	}

	@Test
	void testPickUpGrandChildCategory() {
		Integer childId = 2;
		List<Category>categoryList=new ArrayList<>();
		when(service.pickUpCategoryListByParentIdAndDepth(childId, 2)).thenReturn(categoryList);
		Map<String,List<Category>>result = controller.pickUpGrandChildCategory(childId);
		assertEquals(1,result.size());
		assertEquals(categoryList,result.get("grandChildCategoryList"));
	}

}
