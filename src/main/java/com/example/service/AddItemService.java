package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Category;
import com.example.domain.Item;
import com.example.form.ItemForm;
import com.example.repository.CategoryRepository;
import com.example.repository.ItemRepository;

/**
 * 商品追加を操作するサービス.
 * 
 * @author kenta_ichiyoshi
 *
 */
@Service
public class AddItemService {

	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	/**
	 * 階層に紐付くカテゴリを取得する.
	 * 
	 * @param depth 階層
	 * @return 検索されたカテゴリ情報
	 */
	public List<Category> pickUpCategoryListByDepth(Integer depth) {
		List<Category> categoryList = categoryRepository.findByDepth(depth);
		return categoryList;

	}

	/**
	 * 親id,階層に紐付くカテゴリ情報を取得する.
	 * 
	 * @param parentId 親id
	 * @param depth    階層
	 * @return 検索されたカテゴリ情報
	 */
	public List<Category> pickUpCategoryListByParentIdAndDepth(Integer parentId, Integer depth) {
		List<Category> categoryList = categoryRepository.findByParentIdAndDepth(parentId, depth);
		return categoryList;
	}

	/**
	 * 新商品を追加する.
	 * 
	 * @param form フォーム
	 */
	public void insertItem(ItemForm form) {
		// 作成されたitemオブジェクトをつめる.
		Item item = createItem(form);
		itemRepository.deleteIndexForItemId();
		itemRepository.insertItem(item);
		itemRepository.createIndexForItemId();
	}

	/**
	 * 追加商品情報を作成する.
	 * 
	 * @param form フォーム
	 * @return 追加商品情報
	 */
	public Item createItem(ItemForm form) {
		Item item = new Item();
		item.setName(form.getInputName());
		item.setCondition(form.getCondition());
		item.setBrand(form.getBrand());
		item.setPrice(Double.parseDouble(form.getPrice()));
		item.setShipping(0); // shippingを一旦0で設定.
		item.setDescription(form.getDescription());
		item.setCategoryId(Integer.parseInt(form.getGrandChildId()));
		Integer itemId = itemRepository.checkItemId();
		itemId++;
		item.setItemId(itemId);
		return item;
	}
}
