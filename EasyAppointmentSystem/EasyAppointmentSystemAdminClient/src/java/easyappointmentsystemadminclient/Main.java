package easyappointmentsystemadminclient;

import ejb.session.stateless.AdminEntitySessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import javax.ejb.EJB;


public class Main {
    
    @EJB
    private static AdminEntitySessionBeanRemote adminEntitySessionBeanRemote;
    @EJB
    private static ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;

    public static void main(String[] args) {
        AdminModule adminModule = new AdminModule(adminEntitySessionBeanRemote, serviceProviderEntitySessionBeanRemote);
        adminModule.adminStartMenu();
    }   
}
