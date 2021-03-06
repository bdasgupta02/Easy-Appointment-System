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
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AppointmentCancellationException;
import util.exception.CustomerAlreadyExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.EntityAttributeNullException;
import util.exception.InvalidLoginException;
import util.exception.ServiceProviderAlreadyExistsException;
import util.security.CryptographicHelper;

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
    public Long createCustomerEntity(CustomerEntity newCustomerEntity) throws EntityAttributeNullException, CustomerAlreadyExistsException {

        if (newCustomerEntity.getFirstName() != null && newCustomerEntity.getLastName() != null
                && newCustomerEntity.getIdentityNo() != null && newCustomerEntity.getAddress() != null
                && newCustomerEntity.getGender() != null && newCustomerEntity.getAge() != null
                && newCustomerEntity.getEmail() != null && newCustomerEntity.getCity() != null
                && newCustomerEntity.getPassword() != null && newCustomerEntity.getPhone() != null) {

            try {
                String hashedPassword = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(newCustomerEntity.getPassword()));
                newCustomerEntity.setPassword(hashedPassword);
                em.persist(newCustomerEntity);
                em.flush();
            } catch (PersistenceException ex) {
                if ((ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                        && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new CustomerAlreadyExistsException("Error: Customer already exists!");
                }
            }
            return newCustomerEntity.getCustomerId();
        } else {
            throw new EntityAttributeNullException("Some values are null! Creation of Customer aborted.");
        }
    }

    @Override
    public CustomerEntity retrieveCustomerEntityById(Long customerId) throws CustomerNotFoundException {
        CustomerEntity customerEntity = em.find(CustomerEntity.class, customerId);
        if (customerEntity != null) {
            em.refresh(customerEntity);
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
        CustomerEntity customerEntity = retrieveCustomerEntityById(customerId);
        //CHECK IF A SERVICE PROVIDER HAS THIS CATEGORY
        if (customerEntity != null) {
            em.remove(customerEntity);
        } else {
            throw new CustomerNotFoundException("Customer with id " + customerId + " does not exist!\n");
        }
    }

    @Override
    public List<AppointmentEntity> retrieveAppointmentsByCustomerId(Long customerId) throws CustomerNotFoundException {
        CustomerEntity customerEntity = retrieveCustomerEntityById(customerId);
        customerEntity.getAppointments().size();
        List<AppointmentEntity> appointments = customerEntity.getAppointments();
        return appointments;
    }

    @Override
    public void cancelAppointment(Long appointmentId) throws AppointmentCancellationException {
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
        for (AppointmentEntity a : appointments) {

            // checking if the appointment with service provider exists and has already happened
            if (a.getServiceProviderEntity().getServiceProviderId() == serviceProviderId && a.getEndTimestamp().before(new Date(System.currentTimeMillis()))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public CustomerEntity customerLogin(String email, String password) throws InvalidLoginException {
        CustomerEntity customerEntity;
        try {
            customerEntity = retrieveCustomerEntityByEmail(email);
        } catch (CustomerNotFoundException ex) {
            throw new InvalidLoginException("Customer email not found!");
        }
        String inputPassword = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password));
        if (!customerEntity.getPassword().equals(inputPassword)) {
            throw new InvalidLoginException("Customer login credentials are invalid!");
        }
        return customerEntity;
    }

}
