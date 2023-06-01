package com.example.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import com.example.domain.Category;
import com.example.domain.Item;
import com.example.form.SearchItemForm;
import com.example.service.ShowListService;



@SpringBootTest
@AutoConfigureMockMvc
class ShowListControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Mock
	private ShowListService service;
	@InjectMocks
	private ShowListController controller;
	@Mock
	private Model model;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	@DisplayName("商品一覧遷移を確認するテスト")
	void testShowList() throws Exception {
			mockMvc.perform(get("/list"))
			.andExpect(status().isOk())
			.andExpect(view().name("list"));
			System.out.println("商品一覧に遷移しました");
	}
	
	@Test
	@DisplayName("検索機能を確認するテスト")
	void testSearch()throws Exception{
		Integer page = 1;
		SearchItemForm form = new SearchItemForm();
		form.setName("mlb");
		form.setParentId("786");
		form.setChildId("902");
		form.setGrandChildId("909");
		form.setBrand("");
		List<Item>itemList=new ArrayList<>();
		List<Category>childCategoryList= new ArrayList<>();
		List<Category>parentCategoryList = new ArrayList<>();
		
		when(service.countByForm(form)).thenReturn(30);
		when(service.showListByForm(form, page)).thenReturn(itemList);
		when(service.pickUpCategoryListByParentIdAndDepth(Integer.parseInt(form.getParentId()), 1)).thenReturn(childCategoryList);
		when(service.pickUpCategoryListByDepth(0)).thenReturn(parentCategoryList);
		
		//テスト対象のメソッドの呼び出し
		String result = controller.showList(model, page, form);
		
		//結果検証
		assertEquals("list",result);
		verify(service,times(1)).countByForm(form);
		verify(service,times(1)).showListByForm(form, page);
		verify(service,times(1)).pickUpCategoryListByParentIdAndDepth(Integer.parseInt(form.getParentId()), 1);
		verify(service,times(1)).pickUpCategoryListByDepth(0);
		
	}
	
	
}
