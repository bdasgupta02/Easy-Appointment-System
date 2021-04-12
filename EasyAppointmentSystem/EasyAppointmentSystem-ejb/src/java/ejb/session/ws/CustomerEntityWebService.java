package ejb.session.ws;

import ejb.session.stateless.AppointmentEntitySessionBeanLocal;
import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.RatingEntitySessionBeanLocal;
import ejb.session.stateless.ServiceProviderEntitySessionBeanLocal;
import entity.AppointmentEntity;
import entity.CategoryEntity;
import entity.CustomerEntity;
import entity.RatingEntity;
import entity.ServiceProviderEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.AppointmentAlreadyExistsException;
import util.exception.AppointmentCancellationException;
import util.exception.AppointmentNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.DateProcessingException;
import util.exception.EntityAttributeNullException;
import util.exception.InvalidLoginException;
import util.exception.RatingWithoutAppointmentException;
import util.exception.ServiceProviderAlreadyRatedException;
import util.exception.ServiceProviderNotFoundException;

@WebService(serviceName = "CustomerEntityWebService")
@Stateless()
public class CustomerEntityWebService {

    @EJB
    private ServiceProviderEntitySessionBeanLocal serviceProviderEntitySessionBeanLocal;
    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;
    @EJB
    private AppointmentEntitySessionBeanLocal appointmentEntitySessionBeanLocal;
    @EJB
    private RatingEntitySessionBeanLocal ratingEntitySessionBeanLocal;
    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;
    
    
    @WebMethod
    public List<ServiceProviderEntity> searchApprovedServiceProviders() {
        return serviceProviderEntitySessionBeanLocal.retrieveAllApprovedServiceProviders();
    }
    
    @WebMethod
    public Long createAppointmentEntity(@WebParam Long serviceProviderId, @WebParam Date startTimestamp, @WebParam Date endTimestamp, @WebParam String email, @WebParam String password) throws InvalidLoginException, ServiceProviderNotFoundException, EntityAttributeNullException, AppointmentAlreadyExistsException {
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(email, password);
        System.out.println("**** EasyAppointmentSystem*** Customer has logged in remotely: " + customerEntity.getFirstName());
        ServiceProviderEntity serviceProviderEntity = serviceProviderEntitySessionBeanLocal.retrieveServiceProviderEntityById(serviceProviderId);
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setStartTimestamp(startTimestamp);
        appointmentEntity.setEndTimestamp(endTimestamp);
        appointmentEntity.setCustomerEntity(customerEntity);
        appointmentEntity.setServiceProviderEntity(serviceProviderEntity);
        appointmentEntity.setCancelled(Boolean.FALSE);
            
        return appointmentEntitySessionBeanLocal.createAppointmentEntity(appointmentEntity);
    }
    
    @WebMethod
    public List<ServiceProviderEntity> searchServiceProvidersByCategoryCityDate(@WebParam Long categoryId, @WebParam String city, @WebParam Date date,@WebParam String email, @WebParam String password ) throws InvalidLoginException {
       CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(email, password);
        System.out.println("**** EasyAppointmentSystem*** Customer has logged in remotely: " + customerEntity.getFirstName());
        return serviceProviderEntitySessionBeanLocal.retrieveSearchResult(categoryId, city, date);
    }
    
    @WebMethod
    public void rateServiceProvider(@WebParam Long serviceProviderId, @WebParam Integer rating, @WebParam String email, @WebParam String password) throws RatingWithoutAppointmentException, ServiceProviderNotFoundException, CustomerNotFoundException, EntityAttributeNullException, ServiceProviderAlreadyRatedException, InvalidLoginException {
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(email, password);
        System.out.println("**** EasyAppointmentSystem*** Customer has logged in remotely: " + customerEntity.getFirstName());
        ServiceProviderEntity serviceProviderEntity = serviceProviderEntitySessionBeanLocal.retrieveServiceProviderEntityById(serviceProviderId); 
        // check if there is at least 1 appointment for a customer with a service provider
        if (!customerEntitySessionBeanLocal.checkForAppointmentWithServiceProvider(serviceProviderId, customerEntity.getCustomerId())) {
            throw new RatingWithoutAppointmentException("Error: Service provider could not be rated because you haven't had an appointment with them!\n");
        } else if (ratingEntitySessionBeanLocal.isAlreadyRated(serviceProviderEntity, customerEntity)) {
            throw new ServiceProviderAlreadyRatedException("Error: You have already rated this service provider before!\n");
        }
        
        // make a new rating entity
        RatingEntity ratingEntity = new RatingEntity();
        ratingEntity.setCustomerEntity(customerEntity);
        ratingEntity.setServiceProviderEntity(serviceProviderEntity);
        ratingEntity.setRating(rating);
        ratingEntitySessionBeanLocal.addRating(ratingEntity);
    }
    
    @WebMethod
    public CustomerEntity login(@WebParam String email, @WebParam String password) throws InvalidLoginException {
        return customerEntitySessionBeanLocal.customerLogin(email, password);
    }
    
    @WebMethod
    public List<Date> freeSlotsPerServiceProviderAndDate(@WebParam Long serviceProviderId, @WebParam String date, @WebParam String email, @WebParam String password) throws DateProcessingException, ServiceProviderNotFoundException, InvalidLoginException {
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(email, password);
        System.out.println("**** EasyAppointmentSystem*** Customer has logged in remotely: " + customerEntity.getFirstName());
        return serviceProviderEntitySessionBeanLocal.nextSlotFreePerDate(serviceProviderId, date);
    }
    
    @WebMethod
    public Long createCustomerEntity(@WebParam String identityNo, @WebParam String firstName, @WebParam String lastName, 
            @WebParam String address, @WebParam String gender, @WebParam Integer age, @WebParam String city, @WebParam String email, 
            @WebParam Long phone, @WebParam String password) throws EntityAttributeNullException {
            
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setAddress(address);
        customerEntity.setAge(age);
        customerEntity.setCity(city);
        customerEntity.setEmail(email);
        customerEntity.setFirstName(firstName);
        customerEntity.setGender(gender.charAt(0));
        customerEntity.setIdentityNo(identityNo);
        customerEntity.setLastName(lastName);
        customerEntity.setPassword(password);
        customerEntity.setPhone(phone);
        
        return customerEntitySessionBeanLocal.createCustomerEntity(customerEntity);
    }
    
    @WebMethod
    public void cancelAppointment(@WebParam Long appointmentId, @WebParam String email, @WebParam String password) throws AppointmentCancellationException, InvalidLoginException {
       CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(email, password);
        System.out.println("**** EasyAppointmentSystem*** Customer has logged in remotely: " + customerEntity.getFirstName());
        customerEntitySessionBeanLocal.cancelAppointment(appointmentId);
    }
    
    @WebMethod
    public List<AppointmentEntity> retrieveAppointmentsByCustomerId(@WebParam Long customerId, @WebParam String email, @WebParam String password) throws CustomerNotFoundException, InvalidLoginException {
       CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(email, password);
        System.out.println("**** EasyAppointmentSystem*** Customer has logged in remotely: " + customerEntity.getFirstName());
        return customerEntitySessionBeanLocal.retrieveAppointmentsByCustomerId(customerId);
    }
    
    @WebMethod
    public List<CategoryEntity> getAllCategories() {
        return categoryEntitySessionBeanLocal.retrieveAllCategories();
    }
    
    @WebMethod
    public Double getRatingForService(@WebParam Long serviceProviderId) throws ServiceProviderNotFoundException {
        return serviceProviderEntitySessionBeanLocal.getAverageRating(serviceProviderEntitySessionBeanLocal.retrieveServiceProviderEntityById(serviceProviderId));
    }
    
    @WebMethod
    public ServiceProviderEntity getServiceProviderFromAppointmentId(@WebParam Long appointmentId) throws AppointmentNotFoundException {
        return appointmentEntitySessionBeanLocal.retrieveAppointmentEntityById(appointmentId).getServiceProviderEntity();
    }
}
