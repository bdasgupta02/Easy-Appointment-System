package ejb.session.stateless;

import entity.CategoryEntity;
import java.util.List;
import util.exception.CategoryAlreadyExistsException;
import util.exception.CategoryInUseException;
import util.exception.CategoryNotFoundException;
import util.exception.EntityAttributeNullException;


public interface CategoryEntitySessionBeanRemote {
    
    public Long addNewCategory(CategoryEntity newCategoryEntity) throws EntityAttributeNullException, CategoryAlreadyExistsException;
    public CategoryEntity retrieveCategoryByCategoryId(Long categoryId) throws CategoryNotFoundException;
    public CategoryEntity retrieveCategoryByCategoryName(String categoeryName) throws CategoryNotFoundException;
    public List<CategoryEntity> retrieveAllCategories();
    public void updateCategory(CategoryEntity updatedCategoryEntity) throws CategoryNotFoundException;
    public void deleteCategory(String categoryName) throws CategoryNotFoundException, CategoryInUseException;
    
}
