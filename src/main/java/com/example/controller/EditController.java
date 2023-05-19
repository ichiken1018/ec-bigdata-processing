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
import com.example.domain.Item;
import com.example.form.ItemForm;
import com.example.service.EditService;

/**
 * 商品編集機能のコントローラ.
 * 
 * @author kenta_ichiyoshi
 *
 */
@Controller
@RequestMapping("/edit")
public class EditController {

	@Autowired
	private EditService service;

	/**
	 * 編集画面を表示させる.
	 * 
	 * @param model  モデル
	 * @param form   フォーム
	 * @param itemId 商品id
	 * @param result バリデーション結果
	 * @return 編集画面
	 */
	@GetMapping("")
	public String showEditPage(Model model, ItemForm form, Integer itemId, BindingResult result ) {
		
		model.addAttribute("itemId", itemId);

		Item item = service.load(itemId);
		if (!result.hasErrors()) {
			form.setInputName(item.getName());
			form.setPrice(Double.toString(item.getPrice()));
			form.setParentId(item.getCategoryList().get(0).getId().toString());
			form.setChildId(item.getCategoryList().get(1).getId().toString());
			form.setGrandChildId(item.getCategoryList().get(2).getId().toString());
			form.setBrand(item.getBrand());
			form.setCondition(item.getCondition());
			form.setDescription(item.getDescription());
		}

		// 親カテゴリーの処理
		List<Category> parentCategoryList = service.pickUpCategoryListByDepth(0);
		model.addAttribute("parentCategoryList", parentCategoryList);
		// 子、孫カテゴリーの処理
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

		return "edit";
	}

	/**
	 * 商品情報を更新する.
	 * 
	 * @param model  モデル
	 * @param form   フォーム
	 * @param result バリデーション結果
	 * @param itemId 商品id
	 * @return 商品一覧画面にリダイレクト
	 */
	@PostMapping("/update")
	public String insert(Model model, @Validated ItemForm form, BindingResult result, Integer itemId) {

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
			return showEditPage(model, form, itemId, result);
		}

		service.updateItem(form, itemId);

		return "redirect:/list";
	}

}
