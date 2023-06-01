package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.domain.Category;
@SpringBootTest
class PickUpCategoryServiceTest {

	@Autowired
	private PickUpCategoryService pickCategoryService;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("カテゴリ取得を確認するテスト")
	void test() {
		Integer parentId =2;
		Integer depth = 2;
		List<Category>categoryList = pickCategoryService.pickUpCategoryListByParentIdAndDepth(parentId, depth);
		assertEquals("Bags & Cases", categoryList.get(0).getName(), "検索結果が異なります");
		assertEquals("Women", categoryList.get(57).getName(), "検索結果が異なります");
				
	}

}
