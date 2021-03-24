package easyappointmentsystemclient;

import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.ServiceProviderEntity;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.EntityAttributeNullException;
import util.exception.InvalidLoginException;

public class ServiceProviderModule {
    
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    private ServiceProviderEntity currentServiceProvider;
    
    public ServiceProviderModule(){
    }
    
    //to add overloaded constructor later
    
    public void menuServiceProviderOperation() {
        
        int response;
        Scanner scanner = new Scanner(System.in);
        try {
            while(true) {
                System.out.println("*** Welcome to Service provider terminal ***\n");
                System.out.println("1: Registration");
                System.out.println("2: Login");
                System.out.println("3: Exit");
                response = 0;

                while(response < 1 || response > 3) {

                    System.out.print("> ");

                    response = scanner.nextInt();

                    if(response == 1) {
                        registerServiceProvider();
                    } else if(response == 2) {
                        try {
                            doLogin();
                        } catch(InvalidLoginException ex) {
                           System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                        }
                    } else if(response == 3) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }
                if(response == 3) {
                    break;
                }
            }
        } catch (InputMismatchException ex) {
            System.out.println("Invalid option, please try again!\n");
            menuServiceProviderOperation();
        }
    }
    
    public void registerServiceProvider() {
        Scanner scanner = new Scanner(System.in);
        ServiceProviderEntity newServiceProviderEntity = new ServiceProviderEntity();
        
        try{
            System.out.println("*** Service Provider Terminal :: Registration Operation ***\n");
           
            System.out.print("Enter Name> ");
            newServiceProviderEntity.setName(scanner.nextLine().trim());
            
            //need to figure out the biz category part
            //Customer searches by int
            //Admin needs to adds/removes by String
            //System.out.println("")
            
            System.out.print("Enter Business Category> ");
            newServiceProviderEntity.setBizCategory(scanner.nextInt()); 
            scanner.nextLine();
            
            System.out.print("Enter Business Registration Number> ");
            newServiceProviderEntity.setBizRegNum(scanner.nextLine().trim());
            
            System.out.print("Enter City> ");
            newServiceProviderEntity.setCity(scanner.nextLine().trim());
            
            System.out.print("Enter Phone> ");
            newServiceProviderEntity.setPhoneNum(scanner.nextLine().trim());
            
            System.out.print("Enter Business Address> ");
            newServiceProviderEntity.setBizAddress(scanner.nextLine().trim());
            
            System.out.print("Enter Email> ");
            newServiceProviderEntity.setEmail(scanner.nextLine().trim());
            
            System.out.print("Enter Password> ");
            newServiceProviderEntity.setPassword(scanner.nextLine().trim());
            
            serviceProviderEntitySessionBeanRemote.createNewServiceProvider(newServiceProviderEntity);
            
        } catch (InputMismatchException ex) {
            System.out.println("Input is invalid! Please try again.\n");
        } catch (EntityAttributeNullException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void doLogin() throws InvalidLoginException {
        Scanner scanner = new Scanner(System.in);
        String emailAdd = "";
        String password = "";
        
        System.out.println("*** Service Provider Terminal :: Login ***\n");
        System.out.print("Enter Email Address> ");
        emailAdd = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(emailAdd.length() > 0 && password.length() > 0) {
            currentServiceProvider = serviceProviderEntitySessionBeanRemote.login(emailAdd, password);      
        } else {
            throw new InvalidLoginException("Missing login credential!");
        } 
    }
}
