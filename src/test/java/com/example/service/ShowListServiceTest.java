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
		assertEquals(1482542,count,"総商品数が異なります");
		
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
	@DisplayName("検索機能を確認するテスト(フォーム全入力1ページ目表示)")
	void testFindByForm1() {
		SearchItemForm form = new SearchItemForm();
		form.setName("bath");
		form.setParentId("2");
		form.setChildId("3");
		form.setGrandChildId("4");
		form.setBrand("bath");
		Integer page = 1;
		List<Item>itemList=showListService.showListByForm(form, page);
		assertEquals(30,itemList.size(),"検索結果数が異なります");
		assertEquals("New BBW Body Wash And Bath Foam",itemList.get(0).getName(),"検索結果数が異なります");
		assertEquals("Bath and Body Works",itemList.get(29).getName(),"検索結果数が異なります");		
	}
	@Test
	@DisplayName("検索機能を確認するテスト(フォーム全入力最終ページ表示)")
	void testFindByForm2() {
		SearchItemForm form = new SearchItemForm();
		form.setName("bath");
		form.setParentId("2");
		form.setChildId("3");
		form.setGrandChildId("4");
		form.setBrand("bath");
		Integer page = 17;
		List<Item>itemList=showListService.showListByForm(form, page);
		assertEquals(9,itemList.size(),"検索結果数が異なります");
		assertEquals("Bath & Body Works lot",itemList.get(0).getName(),"検索結果数が異なります");
		assertEquals("Bath and body works lotions",itemList.get(8).getName(),"検索結果数が異なります");		
	}
	@Test
	@DisplayName("名前検索機能を確認するテスト")
	void testFindByName() {
		SearchItemForm form = new SearchItemForm();
		form.setName("mlb");
		form.setParentId("-1");
		form.setChildId("-1");
		form.setGrandChildId("-1");
		form.setBrand("");
		Integer page = 1;
		List<Item>itemList = showListService.showListByForm(form, page);		
		assertEquals(30,itemList.size(),"検索結果が異なります");
		assertEquals("MLB Cincinnati Reds T Shirt Size XL",itemList.get(0).getName(),"検索結果が異なります");
		assertEquals("Los Angeles Dodgers jersey MLB Majestic",itemList.get(29).getName(),"検索結果が異なります");	
	}

	@Test
	@DisplayName("カテゴリ検索機能を確認するテスト(全選択)")
	void testFindByCategory1() {
		SearchItemForm form = new SearchItemForm();
		form.setName("");
		form.setParentId("2");
		form.setChildId("3");
		form.setGrandChildId("4");
		form.setBrand("");
		Integer page = 1;
		List<Item>itemList = showListService.showListByForm(form, page);		
		assertEquals(30,itemList.size(),"検索結果が異なります");
		assertEquals("Dove body wash",itemList.get(0).getName(),"検索結果が異なります");
		assertEquals("Lush Bar",itemList.get(29).getName(),"検索結果が異なります");	
	}
	@Test
	@DisplayName("カテゴリ検索機能を確認するテスト(親のみ)")
	void testFindByCategory2() {
		SearchItemForm form = new SearchItemForm();
		form.setName("");
		form.setParentId("2");
		form.setChildId("-1");
		form.setGrandChildId("-1");
		form.setBrand("");
		Integer page = 1;
		List<Item>itemList = showListService.showListByForm(form, page);		
		assertEquals(30,itemList.size(),"検索結果が異なります");
		assertEquals("Smashbox primer",itemList.get(0).getName(),"検索結果が異なります");
		assertEquals("3D moodstruck fiber lashes mascara",itemList.get(29).getName(),"検索結果が異なります");	
	}
	@Test
	@DisplayName("カテゴリ検索機能を確認するテスト(親と子のみ)")
	void testFindByCategory3() {
		SearchItemForm form = new SearchItemForm();
		form.setName("");
		form.setParentId("2");
		form.setChildId("3");
		form.setGrandChildId("-1");
		form.setBrand("");
		Integer page = 1;
		List<Item>itemList = showListService.showListByForm(form, page);		
		assertEquals(30,itemList.size(),"検索結果が異なります");
		assertEquals("Dove body wash",itemList.get(0).getName(),"検索結果が異なります");
		assertEquals("Baby Shampoo Shower Bath Protection cap",itemList.get(29).getName(),"検索結果が異なります");	
	}

	@Test
	@DisplayName("ブランド検索機能を確認するテスト")
	void testFindByBrand() {
		SearchItemForm form = new SearchItemForm();
		form.setName("");
		form.setParentId("-1");
		form.setChildId("-1");
		form.setGrandChildId("-1");
		form.setBrand("nike");
		Integer offset = 1;
		List<Item>itemList = showListService.showListByForm(form, offset);		
		assertEquals(30,itemList.size(),"検索結果が異なります");
		assertEquals("Girls Nike Pro shorts",itemList.get(0).getName(),"検索結果が異なります");
		assertEquals("Nike Hurachi Men Size 8.5",itemList.get(29).getName(),"検索結果が異なります");	
	}
	
	@Test
	@DisplayName("検索結果数を確認するテスト(フォーム全入力)")
	void testCountByForm1() {
		SearchItemForm form = new SearchItemForm();
		form.setName("bath");
		form.setParentId("2");
		form.setChildId("3");
		form.setGrandChildId("4");
		form.setBrand("bath");
		Integer count = showListService.countByForm(form);
		assertEquals(489,count,"検索結果数が異なります");
	}
	
	@Test
	@DisplayName("名前検索結果数を確認するテスト")
	void testCountByForm2() {
		SearchItemForm form = new SearchItemForm();
		form.setName("mlb");
		form.setParentId("-1");
		form.setChildId("-1");
		form.setGrandChildId("-1");
		form.setBrand("");
		Integer count = showListService.countByForm(form);
		assertEquals(285,count,"検索結果数が異なります");
	}

	@Test
	@DisplayName("カテゴリ検索結果数を確認するテスト(全選択)")
	void testCountByForm3() {
		SearchItemForm form = new SearchItemForm();
		form.setName("");
		form.setParentId("2");
		form.setChildId("3");
		form.setGrandChildId("4");
		form.setBrand("");
		Integer count = showListService.countByForm(form);
		assertEquals(5051,count,"検索結果数が異なります");
	}
	@Test
	@DisplayName("カテゴリ検索結果数を確認するテスト(親のみ)")
	void testCountByForm4() {
		SearchItemForm form = new SearchItemForm();
		form.setName("");
		form.setParentId("2");
		form.setChildId("-1");
		form.setGrandChildId("-1");
		form.setBrand("");
		Integer count = showListService.countByForm(form);
		assertEquals(207833,count,"検索結果数が異なります");
	}
	@Test
	@DisplayName("カテゴリ検索結果数を確認するテスト(親と子のみ)")
	void testCountByForm5() {
		SearchItemForm form = new SearchItemForm();
		form.setName("");
		form.setParentId("2");
		form.setChildId("3");
		form.setGrandChildId("-1");
		form.setBrand("");
		Integer count = showListService.countByForm(form);
		assertEquals(7760,count,"検索結果数が異なります");
	}

	@Test
	@DisplayName("ブランド検索結果数を確認するテスト")
	void testCountByForm6() {
		SearchItemForm form = new SearchItemForm();
		form.setName("");
		form.setParentId("-1");
		form.setChildId("-1");
		form.setGrandChildId("-1");
		form.setBrand("nike");
		Integer count = showListService.countByForm(form);
		assertEquals(54148,count,"検索結果数が異なります");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
