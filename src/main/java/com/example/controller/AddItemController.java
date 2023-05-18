package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Category;
import com.example.form.ItemForm;
import com.example.service.AddItemService;

/**
 * 商品追加機能のコントローラ.
 * 
 * @author kenta_ichiyoshi
 *
 */
@Controller
@RequestMapping("/add")
public class AddItemController {
	@Autowired
	private AddItemService service;

	/**
	 * 商品追加画面を表示する.
	 * 
	 * @param model モデル
	 * @param form  フォーム
	 * @return 商品追加画面
	 */
	@GetMapping("")
	public String showAddNewItemPage(Model model, ItemForm form) {
		// 親カテゴリーの処理
		List<Category> parentCategoryList = service.pickUpCategoryListByDepth(0);
		model.addAttribute("parentCategoryList", parentCategoryList);
		// 子,孫カテゴリーの取得
		if (form.getParentId() != null) {
			List<Category> childCategoryList = service
					.pickUpCategoryListByParentIdAndDepth(Integer.parseInt(form.getParentId()), 1);
			model.addAttribute("childCategoryList", childCategoryList);
		}
		if (form.getChildId() != null) {
			List<Category> grandChildCategoryList = service
					.pickUpCategoryListByParentIdAndDepth(Integer.parseInt(form.getChildId()), 2);
			model.addAttribute("grandChildCategoryList", grandChildCategoryList);
		}

		return "add";
	}

	/**
	 * 商品を追加する.
	 * 
	 * @param model  モデル
	 * @param form   フォーム
	 * @param result バリデーション結果
	 * @return 商品一覧画面にリダイレクト
	 */
	@PostMapping("/insert")
	public String insert(Model model, @Validated ItemForm form, BindingResult result) {

		// カテゴリの入力値チェック
		if (Integer.parseInt(form.getParentId()) == -1) {
			result.rejectValue("parentId", null, "please select parentCategory");
		} else if (Integer.parseInt(form.getChildId()) == -1) {
			result.rejectValue("parentId", null, "please select childCategory");
		} else if (Integer.parseInt(form.getGrandChildId()) == -1) {
			result.rejectValue("parentId", null, "please select grandChildCategory");
		}

		// 金額のチェック
		if (!("".equals(form.getPrice()))) {
			try {
				Double.parseDouble(form.getPrice());
			} catch (Exception e) {
				result.rejectValue("price", null, "please enter price");
			}
		}

		// エラーがあれば入力画面に遷移
		if (result.hasErrors()) {
			return showAddNewItemPage(model, form);
		}

		service.insertItem(form);

		return "redirect:/list";
	}

}
