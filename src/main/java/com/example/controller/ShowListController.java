package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Category;
import com.example.domain.Item;
import com.example.form.SearchItemForm;
import com.example.service.ShowListService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class ShowListController {

	@Autowired
	private ShowListService showListService;
	@Autowired
	private HttpSession session;

	@GetMapping("")
	public String showList(Model model, Integer page, SearchItemForm form) {
		// sessionにform追加
		session.setAttribute("form", form);

		// 検索機能も追加実装
		// form条件で商品数を算出
		Integer totalItems = null;
		if (form.getName() == null && form.getBrand() == null && form.getParentId() == null) {
			// フォームなし
			totalItems = showListService.countItems();
		} else {
			// フォームあり
			model.addAttribute("searchItemForm", form);
			totalItems = showListService.countByForm(form);
		}
		// System.out.println("count:" + totalItems);

		// ページ数の遷移の処理
		Integer totalPage = totalItems / 30 + 1;
		model.addAttribute("totalPage", totalPage);
		page = checkPage(page, totalPage);
		session.setAttribute("page", page);

		// 商品表示
		List<Item> itemList = null;
		if (form.getName() == null && form.getBrand() == null && form.getParentId() == null) {
			// フォームなし
			itemList = showListService.showItemList(page);
		} else {
			// フォームあり
			itemList = showListService.showListByForm(form, page);
			
			//検索結果0件時、全件表示
			if(itemList.size() == 0) {
				model.addAttribute("errorMessage","No matching items found");
				itemList = showListService.showItemList(page);
			}

			// 子カテゴリの処理
			if (Integer.parseInt(form.getParentId()) > 0) {
				List<Category> childCategoryList = showListService
						.pickUpCategoryListByParentIdAndDepth(Integer.parseInt(form.getParentId()), 1);
				model.addAttribute("childCategoryList", childCategoryList);
			}
			// 孫カテゴリの処理
			if (Integer.parseInt(form.getChildId()) > 0) {
				List<Category> grandChildCategoryList = showListService
						.pickUpCategoryListByParentIdAndDepth(Integer.parseInt(form.getChildId()), 2);
				model.addAttribute("grandChildCategoryList", grandChildCategoryList);
			}
		}
		model.addAttribute("itemList", itemList);
		List<Category> parentCategoryList = showListService.pickUpCategoryListByDepth(0);
		model.addAttribute("parentCategoryList", parentCategoryList);

		return "list";
	}

	public Integer checkPage(Integer page, Integer totalPage) {
		if (page == null || page < 1 || page > totalPage) {
			page = 1;
		}
		return page;
	}

	@GetMapping("/prev-next")
	public String nextPage(Model model, SearchItemForm form, Integer page,Integer categoryId,String selectBrand) {
		form = (SearchItemForm) session.getAttribute("form");
		return showList(model, page, form);
	}

	@GetMapping("/back")
	public String backPage(Model model, SearchItemForm form, Integer page) {
		form = (SearchItemForm) session.getAttribute("form");
		if ((Integer) session.getAttribute("page") != null) {
			page = (Integer) session.getAttribute("page");
		} else {
			page = 1;
		}
		return showList(model, page, form);
	}

	@GetMapping("/jump")
	public String jumpPage(Model model, SearchItemForm form,String page,Integer categoryId,String selectBrand) {
	    form = (SearchItemForm) session.getAttribute("form");
		Integer integerPage = null;
		try {
			integerPage = Integer.parseInt(page);
		} catch (Exception e) {
			integerPage = 1;
		}
		return showList(model, integerPage, form);
	}

}
