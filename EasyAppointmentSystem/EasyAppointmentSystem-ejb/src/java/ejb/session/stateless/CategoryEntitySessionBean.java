package ejb.session.stateless;

import entity.CategoryEntity;
import entity.ServiceProviderEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CategoryInUseException;
import util.exception.CategoryNotFoundException;
import util.exception.EntityAttributeNullException;
import util.exception.ServiceProviderNotFoundException;

@Stateless
@Remote(CategoryEntitySessionBeanRemote.class)
@Local(CategoryEntitySessionBeanLocal.class)

public class CategoryEntitySessionBean implements CategoryEntitySessionBeanRemote, CategoryEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EasyAppointmentSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long addNewCategory(CategoryEntity newCategoryEntity) throws EntityAttributeNullException {
        if (newCategoryEntity.getCategory().isEmpty()) {
            throw new EntityAttributeNullException("Some values are null! Creation of Category aborted.\n");
        } else {
            em.persist(newCategoryEntity);
            em.flush();
            return newCategoryEntity.getCategoryId();
        }
    }
    
     @Override
    public CategoryEntity retrieveCategoryByCategoryId(Long categoryId) throws CategoryNotFoundException{
        CategoryEntity categoryEntity = em.find(CategoryEntity.class, categoryId);
        if (categoryEntity != null) {
            return categoryEntity;
        } else {
            throw new CategoryNotFoundException("Category with id " + categoryId + " does not exist!\n");
        }
    }
    
    @Override
    public CategoryEntity retrieveCategoryByCategoryName(String categoryName) throws CategoryNotFoundException {
        Query query = em.createQuery("SELECT c FROM CategoryEntity c WHERE c.category = :inCategory");
        query.setParameter("inCategory", categoryName);
        
        try {
            return (CategoryEntity) query.getSingleResult();
        } catch(NoResultException | NonUniqueResultException ex) {
            throw new CategoryNotFoundException();
        }
    }
    
    @Override
    public List<CategoryEntity> retrieveAllCategories() {
        Query query = em.createQuery("SELECT c FROM CategoryEntity c ORDER BY c.categoryId ASC");
        return query.getResultList();
    }    
    
    @Override
    public void updateCategory(CategoryEntity updatedCategoryEntity) throws CategoryNotFoundException {
            CategoryEntity categoryEntity = retrieveCategoryByCategoryId(updatedCategoryEntity.getCategoryId());
            if (updatedCategoryEntity.getCategory() != null) {
                categoryEntity.setCategory(updatedCategoryEntity.getCategory());
            }
    }
    
    @Override
    public void deleteCategory(String categoryName) throws CategoryNotFoundException, CategoryInUseException {
        CategoryEntity categoryEntity = retrieveCategoryByCategoryName(categoryName);
        //CHECK IF A SERVICE PROVIDER HAS THIS CATEGORY
        if (categoryEntity != null) {
            Long categoryId = categoryEntity.getCategoryId();
            Query query = em.createQuery("SELECT sp FROM ServiceProviderEntity sp WHERE sp.bizCategory = :inCategoryId");
            query.setParameter("inCategoryId", categoryId);
            List<ServiceProviderEntity> result = query.getResultList();
            
            if(result != null){
                throw new CategoryInUseException("Category with name " + categoryName + "used by service providers!");
            } else {
                em.remove(categoryEntity);
            }
        } else {
            throw new CategoryNotFoundException("Category with name " + categoryName + " does not exist!\n");
        }
    }   
}
