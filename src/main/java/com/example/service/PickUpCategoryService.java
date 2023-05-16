package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Category;
import com.example.repository.CategoryRepository;

/**
 * カテゴリのDOM操作用のサービス.
 * 
 * @author kenta_ichiyoshi
 *
 */
@Service
@Transactional
public class PickUpCategoryService {
	@Autowired
	CategoryRepository categoryRepository;

	/**
	 * 親idと階層に紐付くカテゴリ情報を取得する.
	 * 
	 * @param parentId 親id
	 * @param depth    階層
	 * @return 検索されたカテゴリ情報
	 */
	public List<Category> pickUpCategoryListByParentIdAndDepth(Integer parentId, Integer depth) {

		return categoryRepository.findByParentIdAndDepth(parentId, depth);
	}

}
