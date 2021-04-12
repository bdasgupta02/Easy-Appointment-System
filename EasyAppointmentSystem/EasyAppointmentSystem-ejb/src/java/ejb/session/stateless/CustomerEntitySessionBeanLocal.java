/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.CustomerEntity;
import java.util.List;
import util.exception.AppointmentCancellationException;
import util.exception.CustomerNotFoundException;
import util.exception.EntityAttributeNullException;
import util.exception.InvalidLoginException;

public interface CustomerEntitySessionBeanLocal {

    public Long createCustomerEntity(CustomerEntity newCustomerEntity) throws EntityAttributeNullException;

    public CustomerEntity retrieveCustomerEntityById(Long customerId) throws CustomerNotFoundException;

    public void updateCustomerEntity(CustomerEntity customerEntity) throws EntityAttributeNullException;

    public void deleteCustomerEntity(Long customerId) throws CustomerNotFoundException;

    public List<AppointmentEntity> retrieveAppointmentsByCustomerId(Long customerId) throws CustomerNotFoundException;

    public void cancelAppointment(Long appointmentId) throws AppointmentCancellationException;

    public CustomerEntity retrieveCustomerEntityByEmail(String email) throws CustomerNotFoundException;

    public Boolean checkForAppointmentWithServiceProvider(Long serviceProviderId, Long customerId) throws CustomerNotFoundException;
    
    public CustomerEntity customerLogin(String email, String password) throws InvalidLoginException;
}
