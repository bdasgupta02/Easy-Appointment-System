package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.CustomerEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;
import util.exception.EntityAttributeNullException;

@Stateless
@Local(CustomerEntitySessionBeanLocal.class)
@Remote(CustomerEntitySessionBeanRemote.class)

public class CustomerEntitySessionBean implements CustomerEntitySessionBeanLocal, CustomerEntitySessionBeanRemote {

    @PersistenceContext(unitName = "EasyAppointmentSystem-ejbPU")
    private EntityManager em;

    // CRUD
    @Override
    public Long createCustomerEntity(CustomerEntity newCustomerEntity) throws EntityAttributeNullException {

        if (newCustomerEntity.getLastName() != null && newCustomerEntity.getLastName() != null
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
        if (customerEntity.getLastName() != null && customerEntity.getLastName() != null
                && customerEntity.getIdentityNo() != null && customerEntity.getAddress() != null
                && customerEntity.getGender() != null && customerEntity.getAge() != null
                && customerEntity.getEmail() != null && customerEntity.getCity() != null
                && customerEntity.getPassword() != null && customerEntity.getPhone() != null) {
            em.merge(customerEntity);
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
    public List<AppointmentEntity> findAppointmentsByCustomerId(Long customerId) throws CustomerNotFoundException {
        
        // try if namedqueries work for all session bean queries
        Query query = em.createQuery("SELECT c.appointments FROM CustomerEntity c WHERE c.id=:customerId");
        query.setParameter("customerId", customerId);
        
        try {
            return (List<AppointmentEntity>) query.getSingleResult();
        } catch(NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException();
        }
    }
    
    @Override
    public void cancelAppointment(Long customerId, Long appointmentId) {
        
        // check if we delete appointments asap or should we add a cancelled indicator
        // to prevent orphan issues, delete from appointments first then customer/service provider
        
    }
}
