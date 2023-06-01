package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.domain.Item;
import com.example.form.ItemForm;
@SpringBootTest
class EditServiceTest {
	@Autowired
	private EditService service;
		
	@BeforeEach
	void setUp() throws Exception {
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("主キー検索を確認するテスト")
	void testLoad() {
		Integer itemId = 1482537;
		Item item = service.load(itemId);
		assertEquals("test",item.getName(),"検索結果が異なります");
	}

	@Test
	@DisplayName("商品更新を確認するテスト")
	void testUpdate() {
		Integer itemId = 1482537;
		ItemForm form = new ItemForm();
		form.setInputName("test");
		form.setPrice("100");
		form.setBrand("updateBrand");
		form.setCondition(1);
		form.setDescription("description");
		form.setParentId("2");
		form.setChildId("3");
		form.setGrandChildId("4");
		service.updateItem(form, itemId);
		
		Item item = new Item();
		item.setItemId(itemId);
		item.setName(form.getInputName());
		item.setBrand(form.getBrand());
		item.setCategoryId(Integer.parseInt(form.getGrandChildId()));
		item.setCondition(form.getCondition());
		item.setDescription(form.getDescription());
		item.setPrice(Double.parseDouble(form.getPrice()));
		item.setShipping(0);
		
		assertEquals("test",item.getName(),"名前が更新されていません");
		assertEquals(100,item.getPrice(),"値段が更新されていません");
		assertEquals(4,item.getCategoryId(),"カテゴリが更新されていません");
		
		
		
		
	}
	
	

}
