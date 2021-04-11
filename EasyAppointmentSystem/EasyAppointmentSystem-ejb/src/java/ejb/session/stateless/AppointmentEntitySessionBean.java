/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AppointmentEntity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AppointmentCancellationException;
import util.exception.AppointmentNotFoundException;
import util.exception.EntityAttributeNullException;


@Stateless
@Local(AppointmentEntitySessionBeanLocal.class)
@Remote(AppointmentEntitySessionBeanRemote.class)
public class AppointmentEntitySessionBean implements AppointmentEntitySessionBeanRemote, AppointmentEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EasyAppointmentSystem-ejbPU")
    private EntityManager em;

    // CRUD
    @Override
    public Long createAppointmentEntity(AppointmentEntity newAppointmentEntity) throws EntityAttributeNullException {
        
        if (newAppointmentEntity.getCustomerEntity() != null &&
                newAppointmentEntity.getStartTimestamp() != null &&
                newAppointmentEntity.getServiceProviderEntity() != null &&
                newAppointmentEntity.getEndTimestamp() != null) {
            
            Long serviceProviderId = newAppointmentEntity.getServiceProviderEntity().getServiceProviderId();
            String date = (new SimpleDateFormat("MMdd")).format(newAppointmentEntity.getStartTimestamp());
            String time = (new SimpleDateFormat("HHmm")).format(newAppointmentEntity.getStartTimestamp());
            String appointmentNum = serviceProviderId + date + time;
            newAppointmentEntity.setAppointmentNum(appointmentNum);
            
            em.persist(newAppointmentEntity);
            em.flush();
            return newAppointmentEntity.getAppointmentId();
        } else {
            throw new EntityAttributeNullException("Error: Some values are null, creation of Appointment aborted.");
        }
    }
    
    @Override
    public AppointmentEntity retrieveAppointmentEntityById(Long appointmentId) throws AppointmentNotFoundException {
        AppointmentEntity appointmentEntity = em.find(AppointmentEntity.class, appointmentId);
        
        if (appointmentEntity != null) {
            em.refresh(appointmentEntity);
            return appointmentEntity;
        } else {
            throw new AppointmentNotFoundException("Error: Appointment ID: " + appointmentId + " does not exist!");
        }
    }
    
    @Override
    public void updateAppointmentEntity(AppointmentEntity appointmentEntity) throws EntityAttributeNullException {
        if (appointmentEntity.getAppointmentNum() != null && appointmentEntity.getCustomerEntity() != null &&
                appointmentEntity.getStartTimestamp() != null &&
                appointmentEntity.getServiceProviderEntity() != null &&
                appointmentEntity.getEndTimestamp() != null) {
            em.merge(appointmentEntity);
        } else {
            throw new EntityAttributeNullException("Error: Some values are null, update of Appointment aborted.");
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
    
    @Override
    public AppointmentEntity retrieveAppointmentEntityByAppointmentNumber(String appointmentNum) throws AppointmentNotFoundException {
        Query q = em.createQuery("SELECT a FROM AppointmentEntity a WHERE a.appointmentNum = :inAppointNum");
        q.setParameter("inAppointNum", appointmentNum);
        try{
            return (AppointmentEntity) q.getSingleResult();
        }catch(NoResultException ex){
            throw new AppointmentNotFoundException("Error: Appointment Number: " + appointmentNum + " does not exist!");
        }
    }
    
    @Override
    public void cancelAppointment(Long appointmentId) throws AppointmentCancellationException {
        AppointmentEntity appointmentEntity;
        try {
            appointmentEntity = retrieveAppointmentEntityById(appointmentId);
        } catch (AppointmentNotFoundException ex) {
            throw new AppointmentCancellationException(ex.getMessage());
        }
        if (appointmentEntity.getCancelled()) {
            throw new AppointmentCancellationException("Error: Appointment ID: " + appointmentId + " is already cancelled!");
        }
        
        
        Date today = new Date();
        Date appointmentDate = appointmentEntity.getStartTimestamp();
        double hours = (appointmentDate.getTime() - today.getTime()) / (1000 * 3600);
        if (hours < 24) {
            throw new AppointmentCancellationException("Error: Less than 24H to appointment. Appointment cannot be cancelled.");
        }
        
        appointmentEntity.setCancelled(true);
        try {
            updateAppointmentEntity(appointmentEntity);
        } catch (EntityAttributeNullException ex) {
            throw new AppointmentCancellationException(ex.getMessage());
        }
    }
}
