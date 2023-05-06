package com.example.demo.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.JWT.JwtFilter;
import com.example.demo.POJO.Category;
import com.example.demo.constants.CafeConstants;
import com.example.demo.dao.CategoryDao;
import com.example.demo.service.CategoryService;
import com.example.demo.utils.CafeUtils;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryDao categoryDao;

	@Autowired
	JwtFilter jwtFilter;

	@Override
	public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
		try {

			if (jwtFilter.isAdmin()) {
				if (validateCategoryMap(requestMap, false)) {
					categoryDao.save(getCategoryFromMap(requestMap, false));
					return CafeUtils.getResponseEntity("Category Added Successfully.", HttpStatus.OK);
				}
			}

			return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
		if (requestMap.containsKey("name")) {
			if (requestMap.containsKey("id") && validateId)
				return true;
			else if (!validateId)
				return true;
		}
		return false;
	}

	private Category getCategoryFromMap(Map<String, String> requestMap, boolean isAdd) {
		Category category = new Category();

		if (isAdd) {
			category.setId(Integer.parseInt(requestMap.get("id")));
		}
		category.setName(requestMap.get("name"));
		return category;
	}

	@Override
	public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
		try {
			if (filterValue != null && filterValue.equalsIgnoreCase("true")) {
				return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(), HttpStatus.OK);
			}
			return new ResponseEntity<List<Category>>(categoryDao.findAll(), HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isAdmin()) {
				if (validateCategoryMap(requestMap, true)) {
					Optional<Category> optional = categoryDao.findById(Integer.parseInt(requestMap.get("id")));
					if (!optional.isEmpty()) {
						categoryDao.save(getCategoryFromMap(requestMap, true));
						return CafeUtils.getResponseEntity("Category Updated Successfully", HttpStatus.OK);
					}

					return CafeUtils.getResponseEntity("Category id does not exists.", HttpStatus.OK);
				}
				return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
			return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
