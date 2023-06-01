package com.example.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import com.example.domain.LoginUser;
import com.example.form.ItemForm;
import com.example.service.AddItemService;
import com.example.service.UserDetailsServiceImpl;
@SpringBootTest
@AutoConfigureMockMvc
class AddItemControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private UserDetailsServiceImpl userImpl;
	@Mock
	private AddItemService service;
	@InjectMocks
	private AddItemController controller;
	@Mock
	private Model model;
	
	
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
	@DisplayName("追加画面に遷移を確認するテスト")
	void testShowAdd() throws Exception{
		LoginUser loginuser = (LoginUser) userImpl.loadUserByUsername("junit5@sample.com");
		mockMvc.perform(get("/add").with(user(loginuser))).andExpect(status().isOk())
				.andExpect(view().name("add"));
		System.out.println("編集画面に遷移しました");
	}
	
	@Test
	@DisplayName("追加機能を確認するテスト")
	void testAdd() {
		ItemForm form = new ItemForm();
		form.setInputName("addName");
		form.setPrice("10");
		form.setParentId("1");
		form.setChildId("2");
		form.setGrandChildId("3");
		form.setBrand("addBrand");
		form.setCondition(1);
		form.setDescription("addDescription");
		BindingResult br = new BeanPropertyBindingResult(form, "form");
		
		String result = controller.insert(model, form, br);
		assertEquals("redirect:/list",result);
		verify(service,times(1)).insertItem(form);
		assertTrue(br.getAllErrors().isEmpty());
		
	}

	@Test
	@DisplayName("追加機能のカテゴリエラーを確認するテスト")
	void testAddInvalidCategory() {
		ItemForm form = new ItemForm();
		form.setInputName("addName");
		form.setPrice("10");
		form.setParentId("-1");
		form.setChildId("-1");
		form.setGrandChildId("-1");
		form.setBrand("addBrand");
		form.setCondition(1);
		form.setDescription("addDescription");
		BindingResult br = new BeanPropertyBindingResult(form, "form");
		
		String result = controller.insert(model, form, br);
		assertEquals("add",result);
		verify(service,never()).insertItem(form);
		assertEquals(1,br.getErrorCount());
		assertTrue(br.hasFieldErrors("parentId"));
		
	}

	@Test
	@DisplayName("追加機能の値段エラーを確認するテスト")
	void testAddInvalidPrice() {
		ItemForm form = new ItemForm();
		form.setInputName("addName");
		form.setPrice("aaa");
		form.setParentId("1");
		form.setChildId("2");
		form.setGrandChildId("3");
		form.setBrand("addBrand");
		form.setCondition(1);
		form.setDescription("addDescription");
		BindingResult br = new BeanPropertyBindingResult(form, "form");
		
		String result = controller.insert(model, form, br);
		assertEquals("add",result);
		verify(service,never()).insertItem(form);
		assertEquals(1,br.getErrorCount());
		assertTrue(br.hasFieldErrors("price"));
		
	}
	
	

}
