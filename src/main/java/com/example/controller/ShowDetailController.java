package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Item;
import com.example.service.ShowDetailService;

/**
 * 商品詳細機能を操作するコントローラ.
 * 
 * @author kenta_ichiyoshi
 *
 */
@Controller
@RequestMapping("/detail")
public class ShowDetailController {

	@Autowired
	private ShowDetailService showDetailService;

	/**
	 * 商品詳細画面に遷移する.
	 * 
	 * @param model  モデル
	 * @param itemId 商品id
	 * @return 商品詳細画面
	 */
	@GetMapping("")
	public String showItemDetail(Model model, Integer itemId) {
		Item item = showDetailService.showItemDetail(itemId);
		model.addAttribute("item", item);
		return "detail";
	}
}
