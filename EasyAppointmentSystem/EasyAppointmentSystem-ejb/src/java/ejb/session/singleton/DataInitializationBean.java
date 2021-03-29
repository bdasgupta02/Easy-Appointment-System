/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AdminEntitySessionBeanLocal;
import ejb.session.stateless.AppointmentEntitySessionBeanLocal;
import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.ServiceProviderEntitySessionBeanLocal;
import entity.AdminEntity;
import entity.AppointmentEntity;
import entity.CategoryEntity;
import entity.CustomerEntity;
import entity.ServiceProviderEntity;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.exception.CustomerNotFoundException;
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
    @EJB
    private AdminEntitySessionBeanLocal adminEntitySessionBeanLocal;
    @EJB 
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;
    @EJB 
    private  AppointmentEntitySessionBeanLocal apptEtySessionBeanLocal;


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
            adminEntitySessionBeanLocal.createNewAdminEntity(new AdminEntity("admin01@easyappointment.com", "001001", "Admin01",""));
            customerEntitySessionBeanLocal.createCustomerEntity(new CustomerEntity("id", "Liza", "Mozart", "address",new Character('F'), 30, "Singapore", "liza@gmail.com", new Long(345240), "password"));
            AppointmentEntity aEty = new AppointmentEntity(customerEntitySessionBeanLocal.retrieveCustomerEntityById(new Long(1)), serviceProviderEntitySessionBeanLocal.retrieveServiceProviderByServiceProviderId(new Long(1)), new Date(), new Date(), new Date(), "2");
            apptEtySessionBeanLocal.createAppointmentEntity(aEty);
        } catch (EntityAttributeNullException | CustomerNotFoundException | ServiceProviderNotFoundException ex) {
        }
    }
    
}
