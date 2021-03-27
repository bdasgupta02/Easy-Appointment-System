/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easyappointmentsystemclient;

import ejb.session.stateless.AdminEntitySessionBeanRemote;
import ejb.session.stateless.CategoryEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import java.util.Scanner;

/**
 *
 * @author vanshiqa
 */
public class MainApp {
    private AdminEntitySessionBeanRemote adminEntitySessionBeanRemote;
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    private CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote;
    
   
    public MainApp(){}

    public MainApp(AdminEntitySessionBeanRemote adminEntitySessionBeanRemote, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote,
            ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote, CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote) {
        this.adminEntitySessionBeanRemote = adminEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
        this.categoryEntitySessionBeanRemote = categoryEntitySessionBeanRemote;
    }
    
    public void runApp(){
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        while (true){
            System.out.println("*** Welcome to Easy Appointment System ***");
            System.out.println("1: Login as Administrator");
        
            System.out.println("2: Login as Service Provider");
            System.out.println("3: Login as Customer");
            System.out.println("4: Exit");
            response = 0;
            System.out.print("> ");
            
            response = scanner.nextInt();
            if(response == 1){
                AdminModule adminModule = new AdminModule(adminEntitySessionBeanRemote, customerEntitySessionBeanRemote);
                adminModule.adminStartMenu();
            }
            if(response == 2){
                ServiceProviderModule serviceProviderModule = new ServiceProviderModule(serviceProviderEntitySessionBeanRemote, categoryEntitySessionBeanRemote);
                serviceProviderModule.menuServiceProviderOperation();
            }
            if (response == 4) {
                break;
            }
        }
               
      
    
    }
}
