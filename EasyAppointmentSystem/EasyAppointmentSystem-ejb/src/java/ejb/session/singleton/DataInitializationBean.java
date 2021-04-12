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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.enumeration.ServiceProviderStatusEnum;
import util.exception.AppointmentAlreadyExistsException;
import util.exception.CategoryAlreadyExistsException;
import util.exception.CategoryNotFoundException;
import util.exception.CustomerAlreadyExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.EntityAttributeNullException;
import util.exception.ServiceProviderAlreadyExistsException;
import util.exception.ServiceProviderNotFoundException;

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
            serviceProviderEntitySessionBeanLocal.retrieveServiceProviderByEmail("test@revolution.com");
        } catch (ServiceProviderNotFoundException ex) {
            initializeData();
        }

    }

    private void initializeData() {
        try {
            adminEntitySessionBeanLocal.createNewAdminEntity(new AdminEntity("admin01@easyappointment.com", "001001", "Admin01", "Staff"));
            customerEntitySessionBeanLocal.createCustomerEntity(new CustomerEntity("S9054321P", "Liza", "Mozart", "address", new Character('F'), 30, "Singapore", "liza@gmail.com", new Long(87654321), "123456"));
            customerEntitySessionBeanLocal.createCustomerEntity(new CustomerEntity("S7898470J", "Robert", "Bach", "address", new Character('M'), 40, "Singapore", "robert@gmail.com", new Long(24681012), "123456"));
            try {
                categoryEntitySessionBeanLocal.addNewCategory(new CategoryEntity("Health"));
                categoryEntitySessionBeanLocal.addNewCategory(new CategoryEntity("Fashion"));
                categoryEntitySessionBeanLocal.addNewCategory(new CategoryEntity("Education"));
                
                serviceProviderEntitySessionBeanLocal.addNewServiceProvider(
                    new ServiceProviderEntity("0012345678", categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(1L), "Revolution", "51 Bras Basah Road", "Bras Basah", "test@revolution.com", "123456", "65551234", ServiceProviderStatusEnum.APPROVED,
                            new ArrayList<RatingEntity>(), new ArrayList<AppointmentEntity>()));
                serviceProviderEntitySessionBeanLocal.addNewServiceProvider(
                    new ServiceProviderEntity("0000012345", categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(3L), "NUS", "7 Kent Ridge Road", "Kent Ridge", "test@nus.com", "123456", "65550000", ServiceProviderStatusEnum.APPROVED,
                            new ArrayList<RatingEntity>(), new ArrayList<AppointmentEntity>()));
            
            } catch (CategoryNotFoundException ex) {
                System.out.println("Category not found while initializing a service provider!");
            } catch (ServiceProviderAlreadyExistsException | CategoryAlreadyExistsException ex) {
                ex.getMessage();
            }
            
            
            try {
                int year = 2021 - 1900;
                AppointmentEntity a1Ety = new AppointmentEntity(customerEntitySessionBeanLocal.retrieveCustomerEntityById(1L), serviceProviderEntitySessionBeanLocal.retrieveServiceProviderEntityById(new Long(1)), new Date(year,03,11,9,30), new Date(year,03,11,10,30));
                apptEtySessionBeanLocal.createAppointmentEntity(a1Ety);
                AppointmentEntity a2Ety = new AppointmentEntity(customerEntitySessionBeanLocal.retrieveCustomerEntityById(1L), serviceProviderEntitySessionBeanLocal.retrieveServiceProviderEntityById(new Long(2)), new Date(year,03,24,11,30), new Date(year,03,24,12,30));
                apptEtySessionBeanLocal.createAppointmentEntity(a2Ety);
                AppointmentEntity a3Ety = new AppointmentEntity(customerEntitySessionBeanLocal.retrieveCustomerEntityById(2L), serviceProviderEntitySessionBeanLocal.retrieveServiceProviderEntityById(new Long(1)), new Date(year,03,10,15,30), new Date(year,03,10,16,30));
                apptEtySessionBeanLocal.createAppointmentEntity(a3Ety);
                AppointmentEntity a4Ety = new AppointmentEntity(customerEntitySessionBeanLocal.retrieveCustomerEntityById(2L), serviceProviderEntitySessionBeanLocal.retrieveServiceProviderEntityById(new Long(2)), new Date(year,03,30,13,30), new Date(year,03,30,14,30));
                apptEtySessionBeanLocal.createAppointmentEntity(a4Ety);
            } catch (CustomerNotFoundException ex) {
                System.out.println("Tried initialising appointment with Customer. Customer not found!");
            } catch (ServiceProviderNotFoundException ex) {
                System.out.println("Tried initialising appointment with Service provider. Service provider not found!");
            } catch (AppointmentAlreadyExistsException ex) {
                System.out.println("Tried initialising appointment. Appointment already exists!");
            }
        } catch (EntityAttributeNullException ex) {
            System.out.println("Some values are null. Data initialization has not been completed!");
        } catch (CustomerAlreadyExistsException ex) {
            System.out.println("Tried initialising customer. Customer already exists!");
        }
    }

}
