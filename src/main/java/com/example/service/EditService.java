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

/**
 * 商品編集を操作するサービス.
 * 
 * @author kenta_ichiyoshi
 *
 */
@Service
@Transactional
public class EditService {
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	/**
	 * 主キー検索する
	 * 
	 * @param itemId 商品id
	 * @return
	 */
	public Item load(Integer itemId) {
		Item item = itemRepository.load(itemId);
		List<Category> categoryList = categoryRepository.findByChildId(item.getCategoryId());
		item.setCategoryList(categoryList);

		return item;
	}

	/**
	 * 階層に紐付くカテゴリ情報を取得する.
	 * 
	 * @param depth 階層
	 * @return 検索されたカテゴリ情報
	 */
	public List<Category> pickUpCategoryListByDepth(Integer depth) {
		List<Category> categoryList = categoryRepository.findByDepth(depth);
		return categoryList;
	}

	/**
	 * 親idと階層に紐付くカテゴリ情報を取得する.
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
	 * 商品情報を更新する.
	 * 
	 * @param form   フォーム
	 * @param itemId 商品id
	 */
	public synchronized void updateItem(ItemForm form, Integer itemId) {
		Item item = createItem(form, itemId);
		itemRepository.deleteIndexForItemId();
		itemRepository.updateItem(item);
		itemRepository.createIndexForItemId();
	}

	/**
	 * 商品更新情報を作成する.
	 * 
	 * @param form   フォーム
	 * @param itemId 商品id
	 * @return 更新商品情報
	 */
	public Item createItem(ItemForm form, Integer itemId) {
		Item item = new Item();
		item.setItemId(itemId);
		item.setName(form.getInputName());
		item.setCondition(form.getCondition());
		item.setBrand(form.getBrand());
		item.setPrice(Double.parseDouble(form.getPrice()));
		item.setShipping(0); // shippingを一旦0で設定.
		item.setDescription(form.getDescription());
		item.setCategoryId(Integer.parseInt(form.getGrandChildId()));
		return item;
	}
}
