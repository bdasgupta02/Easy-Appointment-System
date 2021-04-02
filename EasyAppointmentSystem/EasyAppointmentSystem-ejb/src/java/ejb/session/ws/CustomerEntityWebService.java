package ejb.session.ws;

import ejb.session.stateless.AppointmentEntitySessionBeanLocal;
import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.RatingEntitySessionBeanLocal;
import ejb.session.stateless.ServiceProviderEntitySessionBeanLocal;
import entity.AppointmentEntity;
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
import util.exception.AppointmentCancellationException;
import util.exception.CustomerNotFoundException;
import util.exception.DateProcessingException;
import util.exception.EntityAttributeNullException;
import util.exception.RatingWithoutAppointmentException;
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
    
    
    @WebMethod
    public List<ServiceProviderEntity> searchApprovedServiceProviders() {
        return serviceProviderEntitySessionBeanLocal.retrieveAllApprovedServiceProviders();
    }
    
    @WebMethod
    public Long createAppointmentEntity(@WebParam Long customerId, @WebParam Long serviceProviderId, @WebParam Date startTimestamp, @WebParam Date endTimestamp, @WebParam String appointmentNum) throws CustomerNotFoundException, ServiceProviderNotFoundException {
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.retrieveCustomerEntityById(customerId);
        ServiceProviderEntity serviceProviderEntity = serviceProviderEntitySessionBeanLocal.retrieveServiceProviderEntityById(serviceProviderId);
        try {
            AppointmentEntity appointmentEntity = new AppointmentEntity();
            appointmentEntity.setStartTimestamp(startTimestamp);
            appointmentEntity.setEndTimestamp(endTimestamp);
            appointmentEntity.setCustomerEntity(customerEntity);
            appointmentEntity.setServiceProviderEntity(serviceProviderEntity);
            appointmentEntity.setAppointmentNum(appointmentNum);
            
            return appointmentEntitySessionBeanLocal.createAppointmentEntity(appointmentEntity);
        } catch (EntityAttributeNullException ex) {
            return new Long(-1);
        }
    }
    
    @WebMethod
    public List<ServiceProviderEntity> searchServiceProvidersByCategoryCityDate(@WebParam Long categoryId, @WebParam String city, @WebParam Date date) {
        return serviceProviderEntitySessionBeanLocal.retrieveSearchResult(categoryId, city, date);
    }
    
    @WebMethod
    public void rateServiceProvider(@WebParam Long serviceProviderId, @WebParam Long customerId, @WebParam Integer rating) throws RatingWithoutAppointmentException, ServiceProviderNotFoundException, CustomerNotFoundException, EntityAttributeNullException {
        ServiceProviderEntity serviceProviderEntity = serviceProviderEntitySessionBeanLocal.retrieveServiceProviderEntityById(serviceProviderId);
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.retrieveCustomerEntityById(customerId);
        
        // check if there is at least 1 appointment for a customer with a service provider
        if (!customerEntitySessionBeanLocal.checkForAppointmentWithServiceProvider(serviceProviderId, customerId)) {
            throw new RatingWithoutAppointmentException("Error: Service provider could not be rated because you haven't had an appointment with them!\n");
        }
        
        // make a new rating entity
        RatingEntity ratingEntity = new RatingEntity();
        ratingEntity.setCustomerEntity(customerEntity);
        ratingEntity.setServiceProviderEntity(serviceProviderEntity);
        ratingEntity.setRating(rating);
        ratingEntitySessionBeanLocal.addRating(ratingEntity);
    }
    
    @WebMethod
    public CustomerEntity login(@WebParam String email, @WebParam String password) throws CustomerNotFoundException {
        return customerEntitySessionBeanLocal.retrieveCustomerEntityByEmail(email);
    }
    
    @WebMethod
    public List<Date> freeSlotsPerServiceProviderAndDate(@WebParam Long serviceProviderId, @WebParam String date) throws DateProcessingException, ServiceProviderNotFoundException {
        return serviceProviderEntitySessionBeanLocal.nextSlotFreePerDate(serviceProviderId, date);
    }
    
    // need to add check for unique attribute
    @WebMethod
    public Long createCustomerEntity(@WebParam String identityNo, @WebParam String firstName, @WebParam String lastName, 
            @WebParam String address, @WebParam Character gender, @WebParam Integer age, @WebParam String city, @WebParam String email, 
            @WebParam Long phone, @WebParam String password) throws EntityAttributeNullException {
            
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setAddress(address);
        customerEntity.setAge(age);
        customerEntity.setCity(city);
        customerEntity.setEmail(email);
        customerEntity.setFirstName(firstName);
        customerEntity.setGender(gender);
        customerEntity.setIdentityNo(identityNo);
        customerEntity.setLastName(lastName);
        customerEntity.setPassword(password);
        customerEntity.setPhone(phone);
        
        return customerEntitySessionBeanLocal.createCustomerEntity(customerEntity);
    }
    
    @WebMethod
    public void cancelAppointment(@WebParam Long appointmentId) throws AppointmentCancellationException {
        customerEntitySessionBeanLocal.cancelAppointment(appointmentId);
    }
    
    @WebMethod
    public List<AppointmentEntity> retrieveAppointmentsByCustomerId(@WebParam Long customerId) throws CustomerNotFoundException {
        return customerEntitySessionBeanLocal.retrieveAppointmentsByCustomerId(customerId);
    }
}
