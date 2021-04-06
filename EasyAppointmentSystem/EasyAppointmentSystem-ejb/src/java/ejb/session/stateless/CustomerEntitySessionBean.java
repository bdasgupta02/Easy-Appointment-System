package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.CustomerEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AppointmentCancellationException;
import util.exception.CustomerNotFoundException;
import util.exception.EntityAttributeNullException;

@Stateless
@Local(CustomerEntitySessionBeanLocal.class)
@Remote(CustomerEntitySessionBeanRemote.class)

public class CustomerEntitySessionBean implements CustomerEntitySessionBeanLocal, CustomerEntitySessionBeanRemote {

    @PersistenceContext(unitName = "EasyAppointmentSystem-ejbPU")
    private EntityManager em;
    @EJB
    private AppointmentEntitySessionBeanLocal appointmentEntitySessionBeanLocal;

    // CRUD
    @Override
    public Long createCustomerEntity(CustomerEntity newCustomerEntity) throws EntityAttributeNullException {

        if (newCustomerEntity.getFirstName() != null && newCustomerEntity.getLastName() != null
                && newCustomerEntity.getIdentityNo() != null && newCustomerEntity.getAddress() != null
                && newCustomerEntity.getGender() != null && newCustomerEntity.getAge() != null
                && newCustomerEntity.getEmail() != null && newCustomerEntity.getCity() != null
                && newCustomerEntity.getPassword() != null && newCustomerEntity.getPhone() != null) {
            em.persist(newCustomerEntity);
            em.flush();
            return newCustomerEntity.getCustomerId();
        } else {
            throw new EntityAttributeNullException("Some values are null! Creation of Customer aborted.");
        }
    }

    @Override
    public CustomerEntity retrieveCustomerEntityById(Long customerId) throws CustomerNotFoundException {
        CustomerEntity customerEntity = em.find(CustomerEntity.class, customerId);

        if (customerEntity != null) {
            return customerEntity;
        } else {
            throw new CustomerNotFoundException("Customer ID: " + customerId + " does not exist!");
        }
    }

    @Override
    public void updateCustomerEntity(CustomerEntity customerEntity) throws EntityAttributeNullException {
        if (customerEntity.getFirstName() != null && customerEntity.getLastName() != null
                && customerEntity.getIdentityNo() != null && customerEntity.getAddress() != null
                && customerEntity.getGender() != null && customerEntity.getAge() != null
                && customerEntity.getEmail() != null && customerEntity.getCity() != null
                && customerEntity.getPassword() != null && customerEntity.getPhone() != null) {
            em.merge(customerEntity);
            em.flush();
        } else {
            throw new EntityAttributeNullException("Some values are null! Update aborted.");
        }
    }
    
    @Override
    public void deleteCustomerEntity(Long customerId) throws CustomerNotFoundException {
        
        // code chunk automatically throws exception if entity not found
        CustomerEntity customerEntity = retrieveCustomerEntityById(customerId);
        em.remove(customerEntity);
    }
    
    @Override
    public List<AppointmentEntity> retrieveAppointmentsByCustomerId(Long customerId) throws CustomerNotFoundException {
        CustomerEntity customerEntity = retrieveCustomerEntityById(customerId);
        customerEntity.getAppointments().size();
        return customerEntity.getAppointments();
    }
    
    @Override
    public void cancelAppointment(Long appointmentId) throws AppointmentCancellationException {
        
        // added cancelled indicator in appointment entity for this
        // but check if you change cancelled to true in list<> attribute of customer entity,
        // does it change it automatically in apt entity?
        // this method should only be called after displaying a list by the prev method        
        appointmentEntitySessionBeanLocal.cancelAppointment(appointmentId);
        
    }
    
    @Override
    public CustomerEntity retrieveCustomerEntityByEmail(String email) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM CustomerEntity c WHERE c.email = :inEmail");
        query.setParameter("inEmail", email);
        
        try {
            return (CustomerEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Error: Customer with email: " + email + " does not exist!");
        }
    }
    
    @Override
    public Boolean checkForAppointmentWithServiceProvider(Long serviceProviderId, Long customerId) throws CustomerNotFoundException {
        List<AppointmentEntity> appointments = retrieveAppointmentsByCustomerId(customerId);
        appointments.size();
        for (AppointmentEntity a:appointments) {
            
            // checking if the appointment with service provider exists and has already happened
            if (a.getServiceProviderEntity().getServiceProviderId() == serviceProviderId && a.getEndTimestamp().before(new Date(System.currentTimeMillis()))) {
                return true;
            }
        }
        return false;
    }
}
