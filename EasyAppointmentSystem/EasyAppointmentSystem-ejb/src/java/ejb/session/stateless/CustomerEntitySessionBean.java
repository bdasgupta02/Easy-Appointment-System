package ejb.session.stateless;

import entity.CustomerEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CustomerNotFoundException;
import util.exception.EntityAttributeNullException;

@Stateless
@Local(CustomerEntitySessionBeanLocal.class)
@Remote(CustomerEntitySessionBeanRemote.class)

public class CustomerEntitySessionBean implements CustomerEntitySessionBeanLocal, CustomerEntitySessionBeanRemote {

    @PersistenceContext(unitName = "EasyAppointmentSystem-ejbPU")
    private EntityManager em;

    // CRUD
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

    public CustomerEntity retrieveCustomerEntityById(Long customerId) throws CustomerNotFoundException {
        CustomerEntity customerEntity = em.find(CustomerEntity.class, customerId);

        if (customerEntity != null) {
            return customerEntity;
        } else {
            throw new CustomerNotFoundException("Customer ID: " + customerId + " does not exist!");
        }
    }

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
    
    public void deleteCustomerEntity(Long customerId) {
        try {
            CustomerEntity customerEntity = retrieveCustomerEntityById(customerId);
            em.remove(customerEntity);
        } catch (CustomerNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
