package ejb.session.stateless;

import entity.ServiceProviderEntity;
import javax.ejb.Stateless;
import javax.ejb.Remote;
import javax.ejb.Local;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.EntityAttributeNullException;
import util.exception.InvalidLoginException;
import util.exception.ServiceProviderNotFoundException;

@Stateless
@Local(ServiceProviderEntitySessionBeanLocal.class)
@Remote(ServiceProviderEntitySessionBeanRemote.class)

public class ServiceProviderEntitySessionBean implements ServiceProviderEntitySessionBeanLocal, ServiceProviderEntitySessionBeanRemote {
    
    @PersistenceContext(unitName = "EasyAppointmentSystem-ejbPU")
    private EntityManager em;

    public ServiceProviderEntitySessionBean() {
    }
    
    @Override
    public Long createNewServiceProvider(ServiceProviderEntity newServiceProviderEntity) throws EntityAttributeNullException {
        if (newServiceProviderEntity.getName() != null && newServiceProviderEntity.getBizCategory() != null &&
                newServiceProviderEntity.getBizRegNum() != null && newServiceProviderEntity.getCity() != null &&
                newServiceProviderEntity.getBizAddress() != null && newServiceProviderEntity.getEmailAddress() != null &&
                newServiceProviderEntity.getPhoneNum() != null && newServiceProviderEntity.getPassword() != null) {
            em.persist(newServiceProviderEntity);
            em.flush();
            return(newServiceProviderEntity.getId());
        } else {
            throw new EntityAttributeNullException("Some values are null! Creation of Service Provider aborted.\n");
        }
    }
    
    @Override
    public ServiceProviderEntity retrieveServiceProviderByServiceProviderId(Long serviceProviderId) throws ServiceProviderNotFoundException {
       
        ServiceProviderEntity serviceProvider = em.find(ServiceProviderEntity.class, serviceProviderId);
        if (serviceProvider != null) {
            return serviceProvider;
        } else {
            throw new ServiceProviderNotFoundException("Service provider with id " + serviceProviderId + " does not exist!\n");
        } 
    }
    
    @Override
    public void updateServiceProviderEntity(ServiceProviderEntity updatedServiceProviderEntity) {
        try {
            ServiceProviderEntity currentServiceProviderEntity = retrieveServiceProviderByServiceProviderId(updatedServiceProviderEntity.getId());
            if (updatedServiceProviderEntity.getCity() != null) {
                currentServiceProviderEntity.setCity(updatedServiceProviderEntity.getCity());
            }

            if (updatedServiceProviderEntity.getBizAddress() != null) {
                currentServiceProviderEntity.setBizAddress(updatedServiceProviderEntity.getBizAddress());
            } 

            if (updatedServiceProviderEntity.getEmailAddress() != null) {
                currentServiceProviderEntity.setEmailAddress(updatedServiceProviderEntity.getEmailAddress());
            }

            if (updatedServiceProviderEntity.getPhoneNum() != null) {
                currentServiceProviderEntity.setPhoneNum(updatedServiceProviderEntity.getPhoneNum());
            }

            if (updatedServiceProviderEntity.getPassword() != null) {
                currentServiceProviderEntity.setPassword(updatedServiceProviderEntity.getPassword());
            }

            //to allow admin to update status of service provider
            // check is different as status is assigned to PENDING upon instantiation of object
            if (updatedServiceProviderEntity.getStatus() != "PENDING") {
                currentServiceProviderEntity.setStatus(updatedServiceProviderEntity.getStatus());
            }
        } catch (ServiceProviderNotFoundException ex) {
        }
    }
    
    @Override
    public void deleteServiceProviderEntity(Long serviceProviderId) throws ServiceProviderNotFoundException{
        ServiceProviderEntity serviceProvider = em.find(ServiceProviderEntity.class, serviceProviderId);
        if (serviceProvider != null) {
            em.remove(serviceProvider);
        } else {
            throw new ServiceProviderNotFoundException("Service provider with id " + serviceProviderId + " does not exist!\n");
        }
    }
    
    // to edit later
    @Override
    public ServiceProviderEntity retrieveServiceProviderByEmail(String emailAdd) throws ServiceProviderNotFoundException {
        return new ServiceProviderEntity();
    }
    
    @Override
    public ServiceProviderEntity login(String emailAdd, String password) throws InvalidLoginException {
        try {
            ServiceProviderEntity serviceProviderEntity = retrieveServiceProviderByEmail(emailAdd);
            
            if(serviceProviderEntity.getPassword().equals(password)) {                
                return serviceProviderEntity;
            } else {
                throw new InvalidLoginException("Username does not exist or invalid password! \n");
            }
        } catch(ServiceProviderNotFoundException ex) {
            throw new InvalidLoginException("Username does not exist or invalid password! \n");
        }
    }
}