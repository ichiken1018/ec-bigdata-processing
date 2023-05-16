package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Category;
import com.example.repository.CategoryRepository;

@Service
@Transactional
public class PickUpCategoryService {
	@Autowired
	CategoryRepository categoryRepository;
	public List<Category> pickUpCategoryListByParentIdAndDepth(Integer parentId, Integer depth) {

		return categoryRepository.findByParentIdAndDepth(parentId, depth);
	}

	
}
