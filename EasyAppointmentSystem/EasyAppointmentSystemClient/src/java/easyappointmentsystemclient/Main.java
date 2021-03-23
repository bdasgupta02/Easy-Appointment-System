/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easyappointmentsystemclient;

import ejb.session.stateless.AdminEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import entity.AdminEntity;
import javax.ejb.EJB;



public class Main {
    
    @EJB
    private static AdminEntitySessionBeanRemote admEtyBnRmt;
    @EJB
    private static CustomerEntitySessionBeanRemote custEtyRmt;
 
    /**
     * @param args the command line arguments
     */
   
    public static void main(String[] args) {
    
       
            MainApp mainApp = new MainApp(admEtyBnRmt, custEtyRmt);
            mainApp.runApp();
        
    }
    
}
