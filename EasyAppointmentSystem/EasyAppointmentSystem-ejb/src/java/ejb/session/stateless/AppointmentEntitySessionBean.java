/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AppointmentEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AppointmentNotFoundException;
import util.exception.EntityAttributeNullException;

/**
 *
 * @author Bikram
 */
@Stateless
@Local(AppointmentEntitySessionBeanLocal.class)
@Remote(AppointmentEntitySessionBeanRemote.class)
public class AppointmentEntitySessionBean implements AppointmentEntitySessionBeanRemote, AppointmentEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EasyAppointmentSystem-ejbPU")
    private EntityManager em;

    // CRUD
    @Override
    public Long createAppointmentEntity(AppointmentEntity newAppointmentEntity) throws EntityAttributeNullException {
        
        // doesn't check end time
        if (newAppointmentEntity.getAppointmentNum() != null && newAppointmentEntity.getCustomerEntity() != null &&
                newAppointmentEntity.getDate() != null && newAppointmentEntity.getStartTime() != null &&
                newAppointmentEntity.getServiceProviderEntity() != null) {
            em.persist(newAppointmentEntity);
            em.flush();
            return newAppointmentEntity.getAppointmentId();
        } else {
            throw new EntityAttributeNullException("Some values are null! Creation of Appointment aborted.");
        }
    }
    
    @Override
    public AppointmentEntity retrieveAppointmentEntityById(Long appointmentId) throws AppointmentNotFoundException {
        AppointmentEntity appointmentEntity = em.find(AppointmentEntity.class, appointmentId);
        
        if (appointmentEntity != null) {
            return appointmentEntity;
        } else {
            throw new AppointmentNotFoundException("Appointment ID: " + appointmentId + " does not exist!");
        }
    }
    
    @Override
    public void updateAppointmentEntity(AppointmentEntity appointmentEntity) throws EntityAttributeNullException {
        // doesn't check end time
        if (appointmentEntity.getAppointmentNum() != null && appointmentEntity.getCustomerEntity() != null &&
                appointmentEntity.getDate() != null && appointmentEntity.getStartTime() != null &&
                appointmentEntity.getServiceProviderEntity() != null) {
            em.merge(appointmentEntity);
        } else {
            throw new EntityAttributeNullException("Some values are null! Creation of Appointment aborted.");
        }
    }
    
    @Override
    public void deleteAppointmentEntity(Long appointmentId) throws AppointmentNotFoundException {
        
        // code chunk automatically throws exception if entity not found
        AppointmentEntity appointmentEntity = retrieveAppointmentEntityById(appointmentId);
        em.remove(appointmentEntity);
    }

    @Override
    public List<AppointmentEntity>  retrieveAppointmentEntityByCustomerId(Long customerId) throws AppointmentNotFoundException {
        Query q = em.createQuery("SELECT a FROM AppointmentEntity a WHERE a.customerEntity.customerId = :inCustomerId");
        q.setParameter("inCustomerId", customerId);
        try{
            return (List<AppointmentEntity>)q.getResultList();
        }catch( NoResultException ex){
            throw new AppointmentNotFoundException();
        }
    }
}
