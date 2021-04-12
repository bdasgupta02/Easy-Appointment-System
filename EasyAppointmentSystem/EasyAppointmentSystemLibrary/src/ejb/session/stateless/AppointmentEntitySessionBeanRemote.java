/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AppointmentEntity;
import java.util.List;
import util.exception.AppointmentAlreadyExistsException;
import util.exception.AppointmentCancellationException;
import util.exception.AppointmentNotFoundException;
import util.exception.EntityAttributeNullException;

public interface AppointmentEntitySessionBeanRemote {
    
    public Long createAppointmentEntity(AppointmentEntity newAppointmentEntity) throws EntityAttributeNullException, AppointmentAlreadyExistsException;

    public AppointmentEntity retrieveAppointmentEntityById(Long appointmentId) throws AppointmentNotFoundException;
    
    public void updateAppointmentEntity(AppointmentEntity appointmentEntity) throws EntityAttributeNullException;

    public void deleteAppointmentEntity(Long appointmentId) throws AppointmentNotFoundException;
    
    public void cancelAppointment(Long appointmentId) throws AppointmentCancellationException;
    
    public List<AppointmentEntity>  retrieveAppointmentEntityByCustomerId(Long customerId) throws AppointmentNotFoundException;
    
    public AppointmentEntity  retrieveAppointmentEntityByAppointmentNumber(String appointmentNum) throws AppointmentNotFoundException;
    
}
