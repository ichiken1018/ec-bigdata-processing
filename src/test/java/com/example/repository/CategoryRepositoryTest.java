package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.domain.Category;

@SpringBootTest
class CategoryRepositoryTest {
	@Autowired
	CategoryRepository categoryRepository;

	@Test
	@DisplayName("階層でカテゴリ情報を検索を確認するテスト")
	void testFindByDepth() {
		Integer depth = 0;
		List<Category> categoryList = categoryRepository.findByDepth(depth);
		assertEquals(11, categoryList.size(), "検索結果が異なります");
		assertEquals("", categoryList.get(0).getName(), "検索結果が異なります");
		assertEquals("Women", categoryList.get(10).getName(), "検索結果が異なります");
	}

	@Test
	@DisplayName("親に紐付く子孫のカテゴリ名検索を確認するテスト")
	void testFindByParentIdAndDepth() {
		// 親カテゴリ取得
		Integer parentId1 = 2;
		Integer depth1 = 0;
		List<Category> bigCategoryList = categoryRepository.findByParentIdAndDepth(parentId1, depth1);
		assertEquals("Beauty", bigCategoryList.get(0).getName(), "検索結果が異なります");
		// 子カテゴリ取得
		Integer parentId2 = 2;
		Integer depth2 = 1;
		List<Category> childCategoryList = categoryRepository.findByParentIdAndDepth(parentId2, depth2);
		assertEquals("Bath & Body", childCategoryList.get(0).getName(), "検索結果が異なります");
		assertEquals("Tools & Accessories", childCategoryList.get(6).getName(), "検索結果が異なります");
		// 孫カテゴリ取得
		Integer parentId3 = 2;
		Integer depth3 = 2;
		List<Category> grandChildCategoryList = categoryRepository.findByParentIdAndDepth(parentId3, depth3);
		assertEquals("Bags & Cases", grandChildCategoryList.get(0).getName(), "検索結果が異なります");
		assertEquals("Women", grandChildCategoryList.get(57).getName(), "検索結果が異なります");
		for(Category category :grandChildCategoryList) {
			System.out.println("grandList:" + category.getName());			
		}
		System.out.println("size:" + grandChildCategoryList.size());
	}

	@Test
	@DisplayName("子idから親,子,孫カテゴリの検索を確認するテスト")
	void testFindByChildId() {
		Integer childId = 4;
		List<Category> categoryList = categoryRepository.findByChildId(childId);
		// カテゴリ情報を取得する. 〇/〇/〇
		assertEquals(3, categoryList.size(), "検索結果が異なります");
		assertEquals("Beauty", categoryList.get(0).getName(), "検索結果が異なります");
		assertEquals("Bath & Body", categoryList.get(1).getName(), "検索結果が異なります");
		assertEquals("Bath", categoryList.get(2).getName(), "検索結果が異なります");
	}

}
