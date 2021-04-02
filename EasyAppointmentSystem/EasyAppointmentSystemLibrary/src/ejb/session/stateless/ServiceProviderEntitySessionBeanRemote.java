/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.ServiceProviderEntity;
import java.util.Date;
import java.util.List;
import util.exception.DateProcessingException;
import util.exception.EntityAttributeNullException;
import util.exception.InvalidLoginException;
import util.exception.ServiceProviderAlreadyBlockedException;
import util.exception.ServiceProviderNotFoundException;

public interface ServiceProviderEntitySessionBeanRemote {
    
    public Long addNewServiceProvider(ServiceProviderEntity newServiceProviderEntity) throws EntityAttributeNullException;
    
    public ServiceProviderEntity retrieveServiceProviderEntityById(Long serviceProviderId) throws ServiceProviderNotFoundException;
    
    public void updateServiceProviderEntity(ServiceProviderEntity serviceProviderEntity) throws EntityAttributeNullException;
    
    public void deleteServiceProviderEntity(Long serviceProviderId) throws ServiceProviderNotFoundException;
    
    public ServiceProviderEntity retrieveServiceProviderByEmail(String emailAdd) throws ServiceProviderNotFoundException;
    
    public ServiceProviderEntity login(String emailAdd, String password) throws InvalidLoginException;
    
    public List<AppointmentEntity> retrieveAppointmentsByCustomerId(Long serviceProviderId) throws ServiceProviderNotFoundException;
    
    public List<ServiceProviderEntity> retrieveAllServiceProviders();
    
    public List<ServiceProviderEntity> retrieveAllPendingServiceProviders();
    
    public void approveServiceProviderStatus(ServiceProviderEntity serviceProviderEntity) throws EntityAttributeNullException;

    public void blockServiceProviderStatus(ServiceProviderEntity serviceProviderEntity) throws ServiceProviderAlreadyBlockedException, EntityAttributeNullException;
    
    public List<ServiceProviderEntity> retrieveAllApprovedServiceProviders();
    
    public List<ServiceProviderEntity> retrieveSearchResult(Long categoryId, String city, Date date);
    
    public List<Date> nextSlotFreePerDate(Long serviceProviderId, String date) throws DateProcessingException, ServiceProviderNotFoundException;
}
