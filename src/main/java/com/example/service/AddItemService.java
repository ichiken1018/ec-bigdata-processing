package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Category;
import com.example.domain.Item;
import com.example.form.ItemForm;
import com.example.repository.CategoryRepository;
import com.example.repository.ItemRepository;

@Service
public class AddItemService {

	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	
	public List<Category> pickUpCategoryListByDepth(Integer depth) {
		List<Category> categoryList = categoryRepository.findByDepth(depth);
		
		// category nullを含むリストが取得
		System.out.println("for前addService:" + categoryList);
		//Categoryがnullのものがないように
		for (int i = 0; i < categoryList.size(); i++) {
			if ("".equals(categoryList.get(i).getName())) {
				categoryList.remove(i);
			}
		}
		// category nullを含まないリストが取得
		System.out.println("for後addService:" + categoryList);
		
		return categoryList;
	}

	public List<Category> pickUpCategoryListByParentIdAndDepth(Integer parentId, Integer depth) {
		List<Category> categoryList = categoryRepository.findByParentIdAndDepth(parentId, depth);
		System.out.println("parent&depth : " + categoryList);
		for (int i = 0; i < categoryList.size(); i++) {
			if ("".equals(categoryList.get(i).getName())) {
				categoryList.remove(i);
			}
		}
		System.out.println("for後parent&depth : " + categoryList);
		
		return categoryList;
	}

	public List<Category> pickUpCategoryListByParentIdAndDepth1(Integer parentId, Integer depth) {
		List<Category> categoryList = categoryRepository.pickUpParentIdAndDepth(parentId, depth);
		System.out.println("parent&depth : " + categoryList);
		for (int i = 0; i < categoryList.size(); i++) {
			if ("".equals(categoryList.get(i).getName())) {
				categoryList.remove(i);
			}
		}
		System.out.println("for後parent&depth : " + categoryList);
		
		return categoryList;
	}

	public void insertItem(ItemForm form) {
		Item item = createItem(form);
		itemRepository.deleteIndexForItemId();
		itemRepository.insertItem(item);
		itemRepository.createIndexForItemId();
	}

	public Item createItem(ItemForm form) {
		Item item = new Item();
		item.setName(form.getInputName());
		item.setCondition(form.getCondition());
		item.setBrand(form.getBrand());
		item.setPrice(Double.parseDouble(form.getPrice()));
		item.setShipping(9999);  //shippingを一旦9999にしてしているが、データベースをnot nullにするほうが良いのか？
		item.setDescription(form.getDescription());
		item.setCategoryId(Integer.parseInt(form.getGrandChildId()));
		Integer itemId = itemRepository.checkItemId();
		itemId++;
		item.setItemId(itemId);
		return item;
	}
}
