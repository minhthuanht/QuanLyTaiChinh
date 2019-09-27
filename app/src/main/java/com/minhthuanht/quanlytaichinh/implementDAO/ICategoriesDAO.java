package com.minhthuanht.quanlytaichinh.implementDAO;

import com.minhthuanht.quanlytaichinh.model.Category;

import java.util.List;

public interface ICategoriesDAO {
    boolean insertCategory(Category category);
    boolean updateCategory(Category category);
    boolean deleteCategory(Category category);
    List<Category> getAllCategory();
    List<Category> getCategoriesByType(int type);
    Category getCategoryById(int typeId);
    Category getCategoryByName(String name);
}
