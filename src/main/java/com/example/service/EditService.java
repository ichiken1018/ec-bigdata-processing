package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Category;
import com.example.domain.Item;
import com.example.form.ItemForm;
import com.example.repository.CategoryRepository;
import com.example.repository.ItemRepository;

@Service
@Transactional
public class EditService {
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	public Item load(Integer itemId) {
		Item item =  itemRepository.load(itemId);
		List<Category> categoryList = categoryRepository.findByChildId(item.getCategoryId());
		item.setCategoryList(categoryList);
		
		return item;
	}

	public List<Category> pickUpCategoryListByDepth(Integer depth) {
		List<Category> categoryList = categoryRepository.findByDepth(depth);
		for (int i = 0; i < categoryList.size(); i++) {
			if ("".equals(categoryList.get(i).getName())) {
				categoryList.remove(i);
			}
		}
		return categoryList;
	}

	public List<Category> pickUpCategoryListByParentIdAndDepth(Integer parentId, Integer depth) {
		List<Category> categoryList = categoryRepository.findByParentIdAndDepth(parentId, depth);
		return categoryList;
	}

	public synchronized void updateItem(ItemForm form,Integer itemId) {
		Item item = createItem(form,itemId);
		itemRepository.deleteIndexForItemId();
		itemRepository.updateItem(item);
		itemRepository.createIndexForItemId();
	}

	public Item createItem(ItemForm form,Integer itemId) {
		Item item = new Item();
		item.setItemId(itemId);
		item.setName(form.getInputName());
		item.setCondition(form.getCondition());
		item.setBrand(form.getBrand());
		item.setPrice(Double.parseDouble(form.getPrice()));
		item.setShipping(9999);  //shippingを一旦9999にしてしているが、データベースをnot nullにするほうが良いのか？
		item.setDescription(form.getDescription());
		item.setCategoryId(Integer.parseInt(form.getGrandChildId()));
		return item;
	}
}
