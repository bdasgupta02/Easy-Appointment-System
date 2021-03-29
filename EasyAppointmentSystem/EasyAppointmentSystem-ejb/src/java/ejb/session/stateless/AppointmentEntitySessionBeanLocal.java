/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AppointmentEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.AppointmentNotFoundException;
import util.exception.EntityAttributeNullException;

public interface AppointmentEntitySessionBeanLocal {

    public Long createAppointmentEntity(AppointmentEntity newAppointmentEntity) throws EntityAttributeNullException;

    public AppointmentEntity retrieveAppointmentEntityById(Long appointmentId) throws AppointmentNotFoundException;
    
    public List<AppointmentEntity> retrieveAppointmentEntityByCustomerId(Long customerId) throws AppointmentNotFoundException;

    public void updateAppointmentEntity(AppointmentEntity appointmentEntity) throws EntityAttributeNullException;

    public void deleteAppointmentEntity(Long appointmentId) throws AppointmentNotFoundException;
    
}
