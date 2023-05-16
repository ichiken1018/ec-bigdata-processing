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

@Controller
@RequestMapping("/add")
public class AddItemController {
	@Autowired
	private AddItemService service;

	@GetMapping("")
	public String showAddNewItemPage(Model model, ItemForm form) {
		// 親カテゴリの処理
		List<Category> parentCategoryList = service.pickUpCategoryListByDepth(0);
		model.addAttribute("parentCategoryList", parentCategoryList);

		if (form.getParentId() != null) {
			List<Category> childCategoryList = service
					.pickUpCategoryListByParentIdAndDepth(Integer.parseInt(form.getParentId()), 1);
			model.addAttribute("childCategoryList", childCategoryList);
			System.out.println("childList" + childCategoryList);
		}
		if (form.getChildId() != null) {
			List<Category> grandChildCategoryList = service
					.pickUpCategoryListByParentIdAndDepth(Integer.parseInt(form.getChildId()), 2);
			model.addAttribute("grandChildCategoryList", grandChildCategoryList);
			System.out.println("grandchildList" + grandChildCategoryList);
		}

		return "add";
	}
	
	@PostMapping("/insert")
	public String insert(Model model, @Validated ItemForm form, BindingResult br) {

		// カテゴリの入力値チェック
		if (Integer.parseInt(form.getParentId()) == -1) {
			br.rejectValue("parentId", null, "選択必須項目です");
		} else if (Integer.parseInt(form.getChildId()) == -1) {
			br.rejectValue("parentId", null, "選択必須項目です(子カテゴリ、孫カテゴリも選択必須)");
		} else if (Integer.parseInt(form.getGrandChildId()) == -1) {
			br.rejectValue("parentId", null, "選択必須項目です(孫カテゴリも選択必須)");
		}

		// 金額のチェック
		if (!("".equals(form.getPrice()))) {
			try {
				Double.parseDouble(form.getPrice());
			} catch (Exception e) {
				br.rejectValue("price", null, "価格を数値で入力してください(単位は$です)");
			}
		}

		// エラーがあれば入力画面に遷移
		if (br.hasErrors()) {
			return showAddNewItemPage(model, form);
		}

		service.insertItem(form);

		return "redirect:/";
	}

}
