/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AdminEntity;
import entity.AppointmentEntity;
import java.util.List;
import util.exception.AdminNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.EntityAttributeNullException;
import util.exception.InvalidLoginException;
import util.exception.ServiceProviderNotFoundException;

/**
 *
 * @author vanshiqa
 */

public interface AdminEntitySessionBeanLocal {
    
    Long createNewAdminEntity(AdminEntity adminEty) throws EntityAttributeNullException ;
    
    AdminEntity retrieveAdminByAdminID(Long adminId) throws AdminNotFoundException;
    
    AdminEntity retrieveAdminByEmail(String email) throws AdminNotFoundException;
    
    AdminEntity adminLogin(String email, String password) throws InvalidLoginException;
    
    void updateAdmin(AdminEntity adminEty) throws AdminNotFoundException, EntityAttributeNullException ;
    
    void deleteAdmin(Long adminId) throws AdminNotFoundException;

    public List<AppointmentEntity> retrieveAppointmentEntityByCustomerId(Long customerId) throws CustomerNotFoundException;
    
    public List<AppointmentEntity> retrieveAppointmentEntityByServiceProviderId(Long serviceProviderId) throws ServiceProviderNotFoundException;
    
    
}
