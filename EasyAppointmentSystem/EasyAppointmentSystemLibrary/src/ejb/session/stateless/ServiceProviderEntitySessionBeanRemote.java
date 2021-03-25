/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ServiceProviderEntity;
import util.exception.EntityAttributeNullException;
import util.exception.InvalidLoginException;
import util.exception.ServiceProviderNotFoundException;

public interface ServiceProviderEntitySessionBeanRemote {
    
    public Long addNewServiceProvider(ServiceProviderEntity newServiceProviderEntity) throws EntityAttributeNullException;
    public ServiceProviderEntity retrieveServiceProviderByServiceProviderId(Long serviceProviderId) throws ServiceProviderNotFoundException;
    public void updateServiceProviderEntity(ServiceProviderEntity updatedServiceProviderEntity);
    public void deleteServiceProviderEntity(Long serviceProviderId) throws ServiceProviderNotFoundException;
    public ServiceProviderEntity retrieveServiceProviderByEmail(String emailAdd) throws ServiceProviderNotFoundException;
    public ServiceProviderEntity login(String emailAdd, String password) throws InvalidLoginException;
    
}
