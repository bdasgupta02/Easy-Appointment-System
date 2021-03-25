/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;
import util.exception.EntityAttributeNullException;

/**
 *
 * @author Sheethal14
 */
@Local
public interface CategoryEntitySessionBeanLocal {
    
    public Long addNewCategory(CategoryEntity newCategoryEntity) throws EntityAttributeNullException;
    public CategoryEntity retrieveCategoryByCategoryId(Long categoryId) throws CategoryNotFoundException;
    public CategoryEntity retrieveCategoryByCategoryName(String categoeryName) throws CategoryNotFoundException;
    public List<CategoryEntity> retrieveAllCategories();
    public void updateCategory(CategoryEntity updatedCategoryEntity);
    public void deleteCategory(String categoryName) throws CategoryNotFoundException;
}
