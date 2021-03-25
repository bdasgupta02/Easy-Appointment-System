package ejb.session.stateless;

import entity.ServiceProviderEntity;
import javax.ejb.Stateless;
import javax.ejb.Remote;
import javax.ejb.Local;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    public Long addNewServiceProvider(ServiceProviderEntity newServiceProviderEntity) throws EntityAttributeNullException {
        if (newServiceProviderEntity.getName().isEmpty() || newServiceProviderEntity.getBizCategory() == null ||
                newServiceProviderEntity.getBizRegNum().isEmpty() || newServiceProviderEntity.getCity().isEmpty() ||
                newServiceProviderEntity.getBizAddress().isEmpty() || newServiceProviderEntity.getEmail().isEmpty() ||
                newServiceProviderEntity.getPhoneNum().isEmpty() || newServiceProviderEntity.getPassword().isEmpty()) {
            throw new EntityAttributeNullException("Some values are null! Creation of Service Provider aborted.\n");
        } else {
            em.persist(newServiceProviderEntity);
            em.flush();
            return(newServiceProviderEntity.getServiceProviderId());
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
            ServiceProviderEntity currentServiceProviderEntity = retrieveServiceProviderByServiceProviderId(updatedServiceProviderEntity.getServiceProviderId());
            if (updatedServiceProviderEntity.getCity() != null) {
                currentServiceProviderEntity.setCity(updatedServiceProviderEntity.getCity());
            }

            if (updatedServiceProviderEntity.getBizAddress() != null) {
                currentServiceProviderEntity.setBizAddress(updatedServiceProviderEntity.getBizAddress());
            } 

            if (updatedServiceProviderEntity.getEmail() != null) {
                currentServiceProviderEntity.setEmail(updatedServiceProviderEntity.getEmail());
            }

            if (updatedServiceProviderEntity.getPhoneNum() != null) {
                currentServiceProviderEntity.setPhoneNum(updatedServiceProviderEntity.getPhoneNum());
            }

            if (updatedServiceProviderEntity.getPassword() != null) {
                currentServiceProviderEntity.setPassword(updatedServiceProviderEntity.getPassword());
            }

            if (updatedServiceProviderEntity.getStatus() != null) {
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
    
    @Override
    public ServiceProviderEntity retrieveServiceProviderByEmail(String email) throws ServiceProviderNotFoundException {
        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s WHERE s.email = :inEmail");
        query.setParameter("inEmail", email);
        
        try {
            return (ServiceProviderEntity) query.getSingleResult();
        } catch(NoResultException | NonUniqueResultException ex) {
            throw new ServiceProviderNotFoundException();
        }
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