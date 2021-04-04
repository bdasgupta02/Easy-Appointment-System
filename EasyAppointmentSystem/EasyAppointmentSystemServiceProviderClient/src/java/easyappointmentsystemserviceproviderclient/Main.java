package easyappointmentsystemserviceproviderclient;

import ejb.session.stateless.CategoryEntitySessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import javax.ejb.EJB;


public class Main {
    
    @EJB
    private static ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote; 
    @EJB
    private static CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote;

    public static void main(String[] args) {
        ServiceProviderModule serviceProviderModule = new ServiceProviderModule(serviceProviderEntitySessionBeanRemote, categoryEntitySessionBeanRemote);
        serviceProviderModule.menuServiceProviderOperation();
    }
}
