/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AppointmentEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    
}
