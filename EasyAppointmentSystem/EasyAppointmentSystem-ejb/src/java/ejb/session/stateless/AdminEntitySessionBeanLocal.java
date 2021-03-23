/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AdminEntity;
import util.exception.AdminNotFoundException;
import util.exception.InvalidLoginException;

/**
 *
 * @author vanshiqa
 */

public interface AdminEntitySessionBeanLocal {
    Long createNewAdminEntity(AdminEntity adminEty);
    AdminEntity retrieveAdminByAdminID(Long adminId) throws AdminNotFoundException;
    AdminEntity retrieveAdminByEmail(String email) throws AdminNotFoundException;
    AdminEntity adminLogin(String email, String password) throws InvalidLoginException;
    void updateAdmin(AdminEntity adminEty) throws AdminNotFoundException;
    void deleteAdmin(Long adminId) throws AdminNotFoundException;
}
