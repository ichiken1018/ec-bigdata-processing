package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Category;
import com.example.domain.Item;
import com.example.repository.CategoryRepository;
import com.example.repository.ItemRepository;

@Service
@Transactional
public class ShowDetailService {

	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	public Item showItemDetail(Integer itemId) {
		Item item = itemRepository.load(itemId);
		List<Category> categoryList = categoryRepository.findByChildId(item.getCategoryId());
		item.setCategoryList(categoryList);
		String nameAll = null;
		for (int i = 0; i < categoryList.size(); i++) {
			if (i == 0) {
				nameAll = categoryList.get(i).getName();
			} else {
				nameAll += "/" + categoryList.get(i).getName();
			}
		}
		item.setCategoryNameAll(nameAll);

		return item;
	}
}
