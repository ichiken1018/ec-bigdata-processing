package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.domain.Category;
import com.example.domain.Item;
import com.example.form.SearchItemForm;
import com.example.repository.ItemRepository;
@SpringBootTest
class ShowListServiceTest {
	
	@Mock
	private ItemRepository itemRepository;
	
	@InjectMocks
	private ShowListService service;
	
	@Autowired
	private ShowListService showListService ;
	
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
	@DisplayName("商品一覧全件表示を確認するテスト")
	void testFindAll() {
		//30件ずつ表示させる.1ページ目0~29,2ページ目30~59,,,
		Integer page = 1; 
		List<Item>itemList = showListService.showItemList(page);
		assertEquals(30,itemList.size(),"検索結果が異なります");
		assertEquals("MLB Cincinnati Reds T Shirt Size XL",itemList.get(0).getName(),"検索結果が異なります");
		assertEquals("Kendra bundle",itemList.get(29).getName(),"検索結果が異なります");
	}
	
	@Test
	@DisplayName("全商品数計算を確認するテスト")
	void testCountItems() {
		Integer count = showListService.countItems();
		assertEquals(1482540,count,"総商品数が異なります");
		
	}

	@Test
	@DisplayName("階層でカテゴリ検索を確認するテスト")
	void testPickUpCategoryByDepth() {
		Integer depth = 0;
		List<Category>categoryList = showListService.pickUpCategoryListByDepth(depth);
		assertEquals(11,categoryList.size(),"検索結果が異なります");
		assertEquals("",categoryList.get(0).getName(),"検索結果が異なります");
		assertEquals("Women",categoryList.get(10).getName(),"検索結果が異なります");		
	}

	@Test
	@DisplayName("子idでカテゴリ検索を確認するテスト")
	void testPickUpCategoryByChildId() {
		Integer childId = 4;
		List<Category>categoryList = showListService.pickUpCategoryListByChildId(childId);
		assertEquals(3, categoryList.size(), "検索結果が異なります");
		assertEquals("Beauty", categoryList.get(0).getName(), "検索結果が異なります");
		assertEquals("Bath & Body", categoryList.get(1).getName(), "検索結果が異なります");
		assertEquals("Bath", categoryList.get(2).getName(), "検索結果が異なります");	
	}

	@Test
	@DisplayName("親idと階層でカテゴリ検索を確認するテスト(親)")
	void testPickUpCategoryByParentIdAndDepth1() {
		Integer parentId = 2;
		Integer depth = 0;
		List<Category>categoryList = showListService.pickUpCategoryListByParentIdAndDepth(parentId, depth);
		assertEquals(1, categoryList.size(), "検索結果が異なります");
		assertEquals("Beauty", categoryList.get(0).getName(), "検索結果が異なります");
	}
	@Test
	@DisplayName("親idと階層でカテゴリ検索を確認するテスト(子)")
	void testPickUpCategoryByParentIdAndDepth2() {
		Integer parentId = 2;
		Integer depth = 1;
		List<Category>categoryList = showListService.pickUpCategoryListByParentIdAndDepth(parentId, depth);
		assertEquals(7, categoryList.size(), "検索結果が異なります");
		assertEquals("Bath & Body", categoryList.get(0).getName(), "検索結果が異なります");
		assertEquals("Tools & Accessories", categoryList.get(6).getName(), "検索結果が異なります");
	}
	@Test
	@DisplayName("親idと階層でカテゴリ検索を確認するテスト(孫)")
	void testPickUpCategoryByParentIdAndDepth3() {
		Integer parentId = 2;
		Integer depth = 2;
		List<Category>categoryList = showListService.pickUpCategoryListByParentIdAndDepth(parentId, depth);
		assertEquals(58, categoryList.size(), "検索結果が異なります");
		assertEquals("Bags & Cases", categoryList.get(0).getName(), "検索結果が異なります");
		assertEquals("Women", categoryList.get(57).getName(), "検索結果が異なります");
	}
	
	@Test
	@DisplayName("検索機能を確認するテスト(名前検索)")
	void testFindByName() {
		SearchItemForm form = new SearchItemForm();
		form.setName("mlb");
		//null,-1だとエラーが発生するためcategory_id("t-shirts")でテスト
		form.setGrandChildId("909");
		form.setBrand("");
		Integer offset = 1;
		List<Item>itemList = showListService.showListByForm(form, offset);		
		assertEquals(10,itemList.size(),"検索結果が異なります");
		assertEquals("MLB Cincinnati Reds T Shirt Size XL",itemList.get(0).getName(),"検索結果が異なります");
		assertEquals("Chicago Cubs Neon MLB Shirt NWT",itemList.get(9).getName(),"検索結果が異なります");	
	}

	@Test
	@DisplayName("検索機能を確認するテスト(カテゴリ検索)")
	void testFindByCategory() {
		SearchItemForm form = new SearchItemForm();
		form.setName("");
		//t-shirtsでテスト
		form.setGrandChildId("909");
		form.setBrand("");
		Integer offset = 1;
		List<Item>itemList = showListService.showListByForm(form, offset);		
		assertEquals(30,itemList.size(),"検索結果が異なります");
		assertEquals("MLB Cincinnati Reds T Shirt Size XL",itemList.get(0).getName(),"検索結果が異なります");
		assertEquals("Trump pence 2016 small and large",itemList.get(29).getName(),"検索結果が異なります");	
	}

	@Test
	@DisplayName("検索機能を確認するテスト(ブランド検索)")
	void testFindByBrand() {
		SearchItemForm form = new SearchItemForm();
		form.setName("");
		//t-shirtsでテスト
		form.setGrandChildId("909");
		form.setBrand("nike");
		Integer offset = 1;
		List<Item>itemList = showListService.showListByForm(form, offset);		
		assertEquals(30,itemList.size(),"検索結果が異なります");
		assertEquals("Nike men's dri-fit sleeveless shirt tee",itemList.get(0).getName(),"検索結果が異なります");
		assertEquals("Nike DriFit What The Kobe 7 Matching Tee",itemList.get(29).getName(),"検索結果が異なります");	
	}
	
	@Test
	@DisplayName("検索結果数を確認するテスト(名前検索)")
	void testCountByForm1() {
		SearchItemForm form = new SearchItemForm();
		form.setName("mlb");
		form.setGrandChildId("909");
		form.setBrand("");
		Integer count = showListService.countByForm(form);
		assertEquals(10,count,"検索結果数が異なります");
	}

	@Test
	@DisplayName("検索結果数を確認するテスト(カテゴリ検索)")
	void testCountByForm2() {
		SearchItemForm form = new SearchItemForm();
		form.setName("");
		form.setGrandChildId("909");
		form.setBrand("");
		Integer count = showListService.countByForm(form);
		assertEquals(15108,count,"検索結果数が異なります");
	}

	@Test
	@DisplayName("検索結果数を確認するテスト(ブランド検索)")
	void testCountByForm3() {
		SearchItemForm form = new SearchItemForm();
		form.setName("");
		form.setGrandChildId("909");
		form.setBrand("nike");
		Integer count = showListService.countByForm(form);
		assertEquals(914,count,"検索結果数が異なります");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
