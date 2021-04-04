package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.CustomerEntity;
import entity.ServiceProviderEntity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import util.exception.DateProcessingException;
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
            throw new EntityAttributeNullException("Error: Some values are null! Creation of Service Provider aborted.\n");
        } else {
            em.persist(newServiceProviderEntity);
            em.flush();
            return newServiceProviderEntity.getServiceProviderId();
        }
    }
    
    @Override
    public ServiceProviderEntity retrieveServiceProviderEntityById(Long serviceProviderId) throws ServiceProviderNotFoundException {
       
        ServiceProviderEntity serviceProvider = em.find(ServiceProviderEntity.class, serviceProviderId);
        if (serviceProvider != null) {
            return serviceProvider;
        } else {
            throw new ServiceProviderNotFoundException("Error: Service provider with id " + serviceProviderId + " does not exist!\n");
        } 
    }
    
    @Override
    public void updateServiceProviderEntity(ServiceProviderEntity serviceProviderEntity) throws EntityAttributeNullException {
        if (serviceProviderEntity.getName().isEmpty() || serviceProviderEntity.getBizCategory() == null ||
                serviceProviderEntity.getBizRegNum().isEmpty() || serviceProviderEntity.getCity().isEmpty() ||
                serviceProviderEntity.getBizAddress().isEmpty() || serviceProviderEntity.getEmail().isEmpty() ||
                serviceProviderEntity.getPhoneNum().isEmpty() || serviceProviderEntity.getPassword().isEmpty()) {
            throw new EntityAttributeNullException("Error: Some values are null! Update of Service Provider aborted.\n");
        } else {
            em.merge(serviceProviderEntity);
            em.flush();
        }
    }
    
    @Override
    public void deleteServiceProviderEntity(Long serviceProviderId) throws ServiceProviderNotFoundException{
        ServiceProviderEntity serviceProvider = em.find(ServiceProviderEntity.class, serviceProviderId);
        if (serviceProvider != null) {
            em.remove(serviceProvider);
        } else {
            throw new ServiceProviderNotFoundException("Error: Service provider with id " + serviceProviderId + " does not exist!\n");
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
    public List<AppointmentEntity> retrieveAppointmentsByServiceProviderId(Long serviceProviderId) throws ServiceProviderNotFoundException {
        ServiceProviderEntity serviceProviderEntity = retrieveServiceProviderEntityById(serviceProviderId);
        serviceProviderEntity.getAppointments().size();
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
    public void approveServiceProviderStatus(ServiceProviderEntity serviceProviderEntity) throws EntityAttributeNullException {
        serviceProviderEntity.setStatus(ServiceProviderStatusEnum.APPROVED);
        updateServiceProviderEntity(serviceProviderEntity);
    }
    
    @Override
    public void blockServiceProviderStatus(ServiceProviderEntity serviceProviderEntity) throws ServiceProviderAlreadyBlockedException, EntityAttributeNullException {
        
        if (serviceProviderEntity.getStatus() == ServiceProviderStatusEnum.BLOCKED) {
            throw new ServiceProviderAlreadyBlockedException("Error: Service provider already blocked!");
        }
        
        serviceProviderEntity.setStatus(ServiceProviderStatusEnum.BLOCKED);
        updateServiceProviderEntity(serviceProviderEntity);
    }
    
    @Override
    public List<ServiceProviderEntity> retrieveAllApprovedServiceProviders() {
        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s WHERE s.status = :inApproved ORDER BY s.serviceProviderId ASC");
        query.setParameter("inApproved", ServiceProviderStatusEnum.APPROVED);
        return query.getResultList();
    }
    
    @Override
    public List<ServiceProviderEntity> retrieveSearchResult(Long categoryId, String city, Date date) {
        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s WHERE s.bizCategory = :inCategory AND s.city = :inCity AND s. ORDER BY s.serviceProviderId ASC");
        query.setParameter("inCategory", categoryId);
        query.setParameter("inCity", city);
        query.setParameter("inDate", date);
        return query.getResultList();
    }
    
    // revised slot going to be added if chosen by customer
    @Override
    public List<Date> nextSlotFreePerDate(Long serviceProviderId, String date) throws DateProcessingException, ServiceProviderNotFoundException {
        ServiceProviderEntity serviceProviderEntity = retrieveServiceProviderEntityById(serviceProviderId);
        serviceProviderEntity.getAppointments().size();
        List<AppointmentEntity> appointments = serviceProviderEntity.getAppointments();
        List<Date> emptySlots = initEmptySlots(date);
        List<Date> revisedSlots = new ArrayList<>();
        
        for (int i = 0; i < emptySlots.size(); i++) {
            boolean isEmpty = false;
            for (AppointmentEntity a : appointments) {
                
                // if existing appointments end and if the slot time is more than 2 hours away from now
                if (!emptySlots.get(i).equals(a.getStartTimestamp()) && emptySlots.get(i).after(new Date(System.currentTimeMillis() + (2 * 3600 * 1000)))) {
                    isEmpty = true;
                }
            }
            if (isEmpty) revisedSlots.add(emptySlots.get(i));
        }
        return revisedSlots;
    }
    
    // duration is set to 1 hour
    private List<Date> initEmptySlots(String date) throws DateProcessingException {
        List<Date> emptySlots = new ArrayList<>();
        for (int i = 8; i < 19; i++) {
            String newHour = i + "";
            String newMinute = "30";
            try {
                emptySlots.add(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date + " " + newHour + ":" + newMinute));
            } catch (ParseException ex) {
                throw new DateProcessingException("Error processing date: parse issues while creating new slots!");
            }
        }
        return emptySlots;
    }
}