/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AdminEntity;
import entity.AppointmentEntity;
import entity.CustomerEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AdminNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.EntityAttributeNullException;
import util.exception.InvalidLoginException;
import util.exception.ServiceProviderNotFoundException;

/**
 *
 * @author vanshiqa
 */
@Stateless
@Local(AdminEntitySessionBeanLocal.class)
@Remote(AdminEntitySessionBeanRemote.class)

public class AdminEntitySessionBean implements AdminEntitySessionBeanRemote, AdminEntitySessionBeanLocal {
    
    @PersistenceContext(unitName = "EasyAppointmentSystem-ejbPU")
    private EntityManager em;
    
    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;
    @EJB
    private ServiceProviderEntitySessionBeanLocal serviceProviderEntitySessionBeanLocal;
    @EJB 
    private EmailControllerLocal emailControllerLocal;
    private Future<Boolean> asyncResult;
    
        
    @Override
    public Long createNewAdminEntity(AdminEntity adminEty) throws EntityAttributeNullException {
        
         if (adminEty.getEmail() != null 
                 && adminEty.getPassword() != null && adminEty.getFirstName() != null 
                 && adminEty.getLastName()  != null){
           em.persist(adminEty);
           em.flush();
           return adminEty.getId();
        } else {
            throw new EntityAttributeNullException("Some values are null! Update aborted.");
        }
      
    }

    @Override
    public AdminEntity retrieveAdminByAdminID(Long adminId) throws AdminNotFoundException {
        AdminEntity admin = em.find(AdminEntity.class, adminId);
        if(admin != null){
            return admin;
        } else{
            throw new AdminNotFoundException("Admin with id: " + adminId + " not found!");
        }
    }

    @Override
    public AdminEntity retrieveAdminByEmail(String email) throws AdminNotFoundException {
      Query q = em.createQuery(" SELECT a FROM AdminEntity a WHERE a.email = :inEmail");
      q.setParameter("inEmail", email);
      try{
          return (AdminEntity)q.getSingleResult();
      } catch(NoResultException | NonUniqueResultException ex){
          throw new AdminNotFoundException("Admin with email: " + email + " not found!");
      }
    }

    @Override
    public AdminEntity adminLogin(String email, String password) throws InvalidLoginException {
         
        try{
            AdminEntity admin = retrieveAdminByEmail(email);
            if(admin.getPassword().equals(password)){
                return admin;
            } else{
                throw new InvalidLoginException("Password is wrong.");
            }
        } catch (AdminNotFoundException ex){
            throw new InvalidLoginException("Email or password is wrong");
        }
    }

    @Override
    public void updateAdmin(AdminEntity adminEty) throws AdminNotFoundException, EntityAttributeNullException {
         if (adminEty.getEmail() != null 
                 && adminEty.getPassword() != null && adminEty.getFirstName() != null 
                 && adminEty.getLastName()  != null){
             em.merge(adminEty);
            em.flush();
        } else {
            throw new EntityAttributeNullException("Some values are null! Update aborted.");
        }
    }

    @Override
    public void deleteAdmin(Long adminId) throws AdminNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AppointmentEntity> retrieveAppointmentEntityByCustomerId(Long customerId) throws CustomerNotFoundException {
        return customerEntitySessionBeanLocal.retrieveAppointmentsByCustomerId(customerId);
    }
    
    @Override
    public List<AppointmentEntity> retrieveAppointmentEntityByServiceProviderId(Long serviceProviderId) throws ServiceProviderNotFoundException {
        return serviceProviderEntitySessionBeanLocal.retrieveAppointmentsByServiceProviderId(serviceProviderId);
    }
    
    @Override
    public List<String> sendEmail(Long customerId) throws CustomerNotFoundException, InterruptedException {
        List<AppointmentEntity> appointments;
        appointments = retrieveAppointmentEntityByCustomerId(customerId);
        if(appointments == null){
            return null;
        } else {
            CustomerEntity c  = customerEntitySessionBeanLocal.retrieveCustomerEntityById(customerId);
            String fullName = c.getFirstName() + "  " + c.getLastName();
            String toEmail = c.getEmail();
            appointments.sort((AppointmentEntity a, AppointmentEntity b) -> a.getStartTimestamp().compareTo(b.getStartTimestamp()));
            AppointmentEntity firstAppt = appointments.get(0);
            asyncResult = emailControllerLocal.emailCheckoutNotificationAsync(firstAppt, "is2103group10@gmail.com", toEmail );
            List resultList = new ArrayList();
            resultList.add(fullName);
            resultList.add(firstAppt.getAppointmentNum()); 
            return resultList;

        }
    }
}
