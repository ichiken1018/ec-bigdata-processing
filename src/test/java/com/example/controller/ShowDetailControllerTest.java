package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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

import com.example.domain.Item;
import com.example.service.ShowDetailService;

@SpringBootTest
@AutoConfigureMockMvc
class ShowDetailControllerTest {	
	@Mock
	private ShowDetailService showDetailService;
	@InjectMocks
	private ShowDetailController showDetailController;
	
	@Mock
	private Model model;
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	@DisplayName("詳細遷移を確認するテスト")
	void testShowList() throws Exception {
			mockMvc.perform(get("/detail").param("itemId", "1"))
			.andExpect(status().isOk())
			.andExpect(view().name("detail"));
			System.out.println("詳細画面に遷移しました");
	}
	
	@Test
	@DisplayName("詳細表示機能を確認するテスト")
	void testDetail() throws Exception {
		Integer itemId = 1;
		Item item = new Item();
		item.setItemId(itemId);
		when(showDetailService.showItemDetail(itemId)).thenReturn(item);
		//テスト対象のメソッド呼び出し
		String result = showDetailController.showItemDetail(model, itemId);
		//結果検証
		assertEquals("detail",result);
		verify(showDetailService,times(1)).showItemDetail(itemId);
		verify(model,times(1)).addAttribute("item",item);
		
	}

}
