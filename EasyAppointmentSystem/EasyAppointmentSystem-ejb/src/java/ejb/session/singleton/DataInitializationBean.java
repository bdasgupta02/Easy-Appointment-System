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
import entity.RatingEntity;
import entity.ServiceProviderEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.enumeration.ServiceProviderStatusEnum;
import util.exception.CategoryAlreadyExistsException;
import util.exception.CategoryNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.EntityAttributeNullException;
import util.exception.ServiceProviderAlreadyExistsException;
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
    private AppointmentEntitySessionBeanLocal apptEtySessionBeanLocal;

    public DataInitializationBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            serviceProviderEntitySessionBeanLocal.retrieveServiceProviderByEmail("test@zalora.com");
        } catch (ServiceProviderNotFoundException ex) {
            initializeData();
        }

    }

    private void initializeData() {
        try {
            adminEntitySessionBeanLocal.createNewAdminEntity(new AdminEntity("admin01@easyappointment.com", "001001", "Admin01", "001001"));
            customerEntitySessionBeanLocal.createCustomerEntity(new CustomerEntity("id", "Liza", "Mozart", "address", new Character('F'), 30, "Singapore", "liza@gmail.com", new Long(345240), "password"));
            try {
                categoryEntitySessionBeanLocal.addNewCategory(new CategoryEntity("Health"));
                categoryEntitySessionBeanLocal.addNewCategory(new CategoryEntity("Fashion"));
                categoryEntitySessionBeanLocal.addNewCategory(new CategoryEntity("Education"));
                serviceProviderEntitySessionBeanLocal.addNewServiceProvider(
                    new ServiceProviderEntity("0012345678", categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(1L), "Zalora", "51 Bras Basah Road #07-01/04", "Bras Basah", "test@Zalora.com", "123456", "65551234", ServiceProviderStatusEnum.APPROVED,
                            new ArrayList<RatingEntity>(), new ArrayList<AppointmentEntity>()));
            } catch (CategoryNotFoundException ex) {
                System.out.println("Category not found while initializing a service provider!");
            } catch (ServiceProviderAlreadyExistsException | CategoryAlreadyExistsException ex) {
                ex.getMessage();
            }
            
            
            AppointmentEntity aEty = null;
            try {
                aEty = new AppointmentEntity(customerEntitySessionBeanLocal.retrieveCustomerEntityById(1L), serviceProviderEntitySessionBeanLocal.retrieveServiceProviderEntityById(new Long(1)), new Date(), new Date());
            } catch (CustomerNotFoundException ex) {
                System.out.println("Tried initialising appointment with Customer id: 1. Customer not found!");
            } catch (ServiceProviderNotFoundException ex) {
                System.out.println("Tried initialising appointment with Service providder id: 1. Service provider not found!");
            }
            apptEtySessionBeanLocal.createAppointmentEntity(aEty);
        } catch (EntityAttributeNullException ex) {
            System.out.println("Some values are null. Data initialization has not been completed!");
        }
    }

}
