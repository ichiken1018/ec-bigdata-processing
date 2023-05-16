package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Item;
import com.example.service.ShowDetailService;

@Controller
@RequestMapping("/detail")
public class ShowDetailController {

	@Autowired
	private ShowDetailService showDetailService;
	
	@GetMapping("")
	public String showItemDetail(Model model,Integer itemId) {
		Item item = showDetailService.showItemDetail(itemId);
		model.addAttribute("item",item);
		return"detail";
	}
}
