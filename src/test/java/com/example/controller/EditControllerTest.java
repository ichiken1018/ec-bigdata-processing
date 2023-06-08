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
import com.example.service.EditService;
import com.example.service.UserDetailsServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
class EditControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private UserDetailsServiceImpl userImpl;
	@Mock
	private EditService service;
	@InjectMocks
	private EditController controller;
	@Mock
	private Model model;
	@Mock
	private ShowDetailController detailController;


	@Test
	@DisplayName("編集ページ遷移を確認するテスト")
	void testShowList() throws Exception {
		LoginUser loginuser = (LoginUser) userImpl.loadUserByUsername("junit5@sample.com");
		mockMvc.perform(get("/edit").param("itemId", "1").with(user(loginuser))).andExpect(status().isOk())
				.andExpect(view().name("edit"));
		System.out.println("編集画面に遷移しました");
	}
	
	@Test
	@DisplayName("編集機能を確認するテスト")
	void testEdit() {
		Integer itemId = 1;
		ItemForm form = new ItemForm();
		form.setInputName("editName");
		form.setPrice("100");
		form.setParentId("2");
		form.setChildId("3");
		form.setGrandChildId("4");
		form.setBrand("editBrand");
		form.setCondition(1);
		form.setDescription("editDescription");
		BindingResult br = new BeanPropertyBindingResult(form, "form");
		
		String result = controller.insert(model, form, br, itemId);
		
		assertEquals(detailController.showItemDetail(model, itemId),result);
		verify(service,times(1)).updateItem(form, itemId);
		assertTrue(br.getAllErrors().isEmpty());
	}
	
	@Test
	@DisplayName("カテゴリエラーを確認するテスト")
	void testInvalidCategory() {
		Integer itemId = 1;
		ItemForm form = new ItemForm();
		form.setInputName("editName");
		form.setPrice("10");
		form.setParentId("-1");
		form.setChildId("-1");
		form.setGrandChildId("-1");
		form.setBrand("editBrand");
		form.setCondition(1);
		form.setDescription("editDescription");
		BindingResult br = new BeanPropertyBindingResult(form, "form");
		
		String result = controller.insert(model, form, br, itemId);
		assertEquals("edit",result);
		verify(service,never()).updateItem(form, itemId);
		assertEquals(1,br.getErrorCount());
		assertTrue(br.hasFieldErrors("parentId"));
		
	}

	@Test
	@DisplayName("値段エラーを確認するテスト")
	void testInvalidPrice() {
		Integer itemId = 1;
		ItemForm form = new ItemForm();
		form.setInputName("editName");
		form.setPrice("aaa");
		form.setParentId("2");
		form.setChildId("3");
		form.setGrandChildId("4");
		form.setBrand("editBrand");
		form.setCondition(1);
		form.setDescription("editDescription");
		BindingResult br = new BeanPropertyBindingResult(form, "form");
		
		String result = controller.insert(model, form, br, itemId);
		assertEquals("edit",result);
		verify(service,never()).updateItem(form, itemId);
		assertEquals(1,br.getErrorCount());
		assertTrue(br.hasFieldErrors("price"));
		
	}
	
	
	

}
