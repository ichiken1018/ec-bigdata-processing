package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.domain.Item;
@SpringBootTest
class ShowDetailServiceTest {

	@Autowired
	private ShowDetailService showDetailService;
	
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
	@DisplayName("主キー検索を確認するテスト")
	void testLoad() {
		Item item = showDetailService.showItemDetail(0);
		assertEquals("MLB Cincinnati Reds T Shirt Size XL",item.getName(),"検索結果が異なります");
		assertEquals(909,item.getCategoryId(),"検索結果が異なります");
		assertEquals("No description yet",item.getDescription(),"検索結果が異なります");
	}

}
