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

/**
 * 商品一覧機能を操作するコントローラ.
 * 
 * @author kenta_ichiyoshi
 *
 */
@Controller
@RequestMapping("/list")
public class ShowListController {

	@Autowired
	private ShowListService showListService;
	@Autowired
	private HttpSession session;

	/**
	 * 商品一覧画面に遷移する.
	 * 
	 * @param model モデル
	 * @param page  ページ
	 * @param form  フォーム
	 * @return ページングされた商品一覧画面
	 */
	@GetMapping("")
	public String showList(Model model, Integer page, SearchItemForm form) {
		session.setAttribute("form", form);

		// 検索機能で分岐
		Integer totalItems = null;
		if (form.getName() == null && form.getBrand() == null && form.getParentId() == null) {
			// フォームなし
			totalItems = showListService.countItems();
		} else {
			// フォームあり
			model.addAttribute("searchItemForm", form);
			totalItems = showListService.countByForm(form);
		}

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

			// 検索結果0件時、全件表示
			if (itemList.size() == 0) {
				model.addAttribute("errorMessage", "No matching items found");
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

	// ページ数を確認する.
	public Integer checkPage(Integer page, Integer totalPage) {
		if (page == null || page < 1 || page > totalPage) {
			page = 1;
		}
		return page;
	}

	/**
	 * 次のページへ遷移する.
	 * 
	 * @param model モデル
	 * @param form  フォーム
	 * @param page  ページ
	 * @return 次のページ画面
	 */
	@GetMapping("/prev-next")
	public String nextPage(Model model, SearchItemForm form, Integer page) {
		form = (SearchItemForm) session.getAttribute("form");
		return showList(model, page, form);
	}

	/**
	 * 前のページへ遷移する.
	 * 
	 * @param model モデル
	 * @param form  フォーム
	 * @param page  ページ
	 * @return 前のページ画面
	 */
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

	/**
	 * 指定したページへ遷移する.
	 * 
	 * @param model モデル
	 * @param form  フォーム
	 * @param page  ページ
	 * @return 指定されたページ画面
	 */
	@GetMapping("/jump")
	public String jumpPage(Model model, SearchItemForm form, String page) {
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
