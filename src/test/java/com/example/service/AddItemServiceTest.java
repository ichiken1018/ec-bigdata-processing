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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.example.domain.Category;
import com.example.form.ItemForm;
@SpringBootTest
class AddItemServiceTest {
	@Autowired
	AddItemService addItemService;
	@Autowired
	private NamedParameterJdbcTemplate template;
	
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
	@DisplayName("階層でカテゴリ情報取得を確認するテスト")
	void testPickUpCategoryListByDepth() {
		Integer depth = 0;
		//カテゴリnullを除外してカテゴリ情報を取得.
		List<Category>categoryList = addItemService.pickUpCategoryListByDepth(depth);
		assertEquals(10, categoryList.size(), "検索結果が異なります");
		assertEquals("Beauty", categoryList.get(0).getName(), "検索結果が異なります");
		assertEquals("Women", categoryList.get(9).getName(), "検索結果が異なります");
	}

	@Test
	@DisplayName("親idと階層でカテゴリ情報取得を確認するテスト")
	void testPickUpCategoryListByParentIdAndDepth() {
		Integer parentId = 2;
		Integer depth = 0;
		//カテゴリnullを除外してカテゴリ情報を取得.
		List<Category>categoryList = addItemService.pickUpCategoryListByParentIdAndDepth(parentId, depth);
		assertEquals(1, categoryList.size(), "検索結果が異なります");
		assertEquals("Beauty", categoryList.get(0).getName(), "検索結果が異なります");
	}

	@Test
	@DisplayName("商品追加を確認するテスト")
	void testInsertItem() {
		delete("testInsert");
		//テストデータ作成
		ItemForm form = new ItemForm();
		form.setInputName("testInsert");
		form.setCondition(1);
		form.setGrandChildId("909");
		form.setBrand("brand");
		form.setPrice("100");
		form.setDescription("testDescription");
		//insert
		addItemService.insertItem(form);
		System.out.println("テストデータinsert");
		assertEquals("testInsert",form.getInputName(),"商品名が登録されていません");
		assertEquals("brand",form.getBrand(),"商品名が登録されていません");
		assertEquals("100",form.getPrice(),"商品名が登録されていません");
		
		delete("testInsert");
		System.out.println("テストデータdelete");
		
	}
	
	//テスト用で追加したitemを削除する.
	public void delete(String name) {
		String sql = "DELETE FROM items WHERE name = :name";
		MapSqlParameterSource param = new MapSqlParameterSource("name",name);
		template.update(sql, param);
	}
	
	
	

}
