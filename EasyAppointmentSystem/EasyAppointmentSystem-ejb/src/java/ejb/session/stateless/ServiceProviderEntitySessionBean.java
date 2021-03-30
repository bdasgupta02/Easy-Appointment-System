package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.CustomerEntity;
import entity.ServiceProviderEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.Remote;
import javax.ejb.Local;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.ServiceProviderStatusEnum;
import util.exception.EntityAttributeNullException;
import util.exception.InvalidLoginException;
import util.exception.ServiceProviderAlreadyBlockedException;
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
            throw new EntityAttributeNullException("Error: Some values are null! Creation of Service Provider aborted.");
        } else {
            em.persist(newServiceProviderEntity);
            em.flush();
            return(newServiceProviderEntity.getServiceProviderId());
        }
    }
    
    @Override
    public ServiceProviderEntity retrieveServiceProviderEntityById(Long serviceProviderId) throws ServiceProviderNotFoundException {
       
        ServiceProviderEntity serviceProvider = em.find(ServiceProviderEntity.class, serviceProviderId);
        if (serviceProvider != null) {
            return serviceProvider;
        } else {
            throw new ServiceProviderNotFoundException("Service provider with id " + serviceProviderId + " does not exist!\n");
        } 
    }
    
    // Is this the correct way to update? Shouldn't we abort even if one value is null?
    @Override
    public void updateServiceProviderEntity(ServiceProviderEntity updatedServiceProviderEntity) {
        try {
            ServiceProviderEntity currentServiceProviderEntity = retrieveServiceProviderEntityById(updatedServiceProviderEntity.getServiceProviderId());
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
            // don't make error messages have "\n" because we're gonna println anyway
            throw new ServiceProviderNotFoundException("Error: Service provider with id " + serviceProviderId + " does not exist!");
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
                throw new InvalidLoginException("Error:Invalid password!");
            }
        } catch(ServiceProviderNotFoundException ex) {
            throw new InvalidLoginException("Error: Username does not exist!");
        }
    }
    
    @Override
    public List<AppointmentEntity> retrieveAppointmentsByCustomerId(Long serviceProviderId) throws ServiceProviderNotFoundException {
        ServiceProviderEntity serviceProviderEntity = retrieveServiceProviderEntityById(serviceProviderId);
        return serviceProviderEntity.getAppointments();
    }
    
    @Override
    public List<ServiceProviderEntity> retrieveAllServiceProviders() {
        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s ORDER BY s.serviceProviderId ASC");
        return query.getResultList();
    }
    
    @Override
    public List<ServiceProviderEntity> retrieveAllPendingServiceProviders() {
        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s WHERE s.status = :inPending ORDER BY s.serviceProviderId ASC");
        query.setParameter("inPending", ServiceProviderStatusEnum.PENDING);
        return query.getResultList();
    }
    
    @Override
    public void approveServiceProviderStatus(ServiceProviderEntity serviceProviderEntity) {
        serviceProviderEntity.setStatus(ServiceProviderStatusEnum.APPROVED);
        updateServiceProviderEntity(serviceProviderEntity);
    }
    
    @Override
    public void blockServiceProviderStatus(ServiceProviderEntity serviceProviderEntity) throws ServiceProviderAlreadyBlockedException {
        
        if (serviceProviderEntity.getStatus() == ServiceProviderStatusEnum.BLOCKED) {
            throw new ServiceProviderAlreadyBlockedException("Error: Service provider already blocked!");
        }
        
        serviceProviderEntity.setStatus(ServiceProviderStatusEnum.BLOCKED);
        updateServiceProviderEntity(serviceProviderEntity);
    }
}