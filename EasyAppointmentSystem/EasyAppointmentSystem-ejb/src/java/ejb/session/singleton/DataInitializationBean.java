/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AdminEntitySessionBeanRemote;
import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.ServiceProviderEntitySessionBeanLocal;
import entity.AdminEntity;
import entity.CategoryEntity;
import entity.ServiceProviderEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.exception.EntityAttributeNullException;
import util.exception.ServiceProviderNotFoundException;

/**
 *
 * @author Bikram
 */
@Singleton
@LocalBean
@Startup

public class DataInitializationBean {
    
    @EJB
    private ServiceProviderEntitySessionBeanLocal serviceProviderEntitySessionBeanLocal;
    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;


    public DataInitializationBean(){}
    
    @PostConstruct
    public void postConstruct()
    {
        try {
            serviceProviderEntitySessionBeanLocal.retrieveServiceProviderByEmail("test@zalora.com");
        } catch(ServiceProviderNotFoundException ex) {
            initializeData();
        }
    }
    
    
    
    private void initializeData() {
        try {
            serviceProviderEntitySessionBeanLocal.addNewServiceProvider(
                    new ServiceProviderEntity("0012345678", 1, "Zalora", "51 Bras Basah Road #07-01/04", "Bras Basah", "test@Zalora.com", "123456", "65551234", "APPROVED", 3));
            categoryEntitySessionBeanLocal.addNewCategory(new CategoryEntity("Health"));
            categoryEntitySessionBeanLocal.addNewCategory(new CategoryEntity("Fashion"));
            categoryEntitySessionBeanLocal.addNewCategory(new CategoryEntity("Education"));
        } catch (EntityAttributeNullException ex) {
        }
    }
    
}
