package easyappointmentsystemclient;

import ejb.session.stateless.CategoryEntitySessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.CategoryEntity;
import entity.ServiceProviderEntity;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.EntityAttributeNullException;
import util.exception.InvalidLoginException;

public class ServiceProviderModule {
    
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    private CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote;
    private ServiceProviderEntity currentServiceProvider;
    
    public ServiceProviderModule(){
    }
    
    public ServiceProviderModule(ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote, CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote){
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
        this.categoryEntitySessionBeanRemote = categoryEntitySessionBeanRemote;
    }
    
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
                            System.out.println("Login successful!\n");
                            loginMenuServiceProviderOperation();
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
            
            List<CategoryEntity> allCategories = categoryEntitySessionBeanRemote.retrieveAllCategories();
            for (CategoryEntity category : allCategories) {
                System.out.print(category.getCategoryId() + "  " + category.getCategory() + " ");
                if (allCategories.indexOf(category) != allCategories.size() - 1) {
                    System.out.print("| ");
                }
            }
            
            System.out.println();
            
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
            
            newServiceProviderEntity.setAvgRating(0);
            newServiceProviderEntity.setStatus("PENDING");
            serviceProviderEntitySessionBeanRemote.addNewServiceProvider(newServiceProviderEntity);
            
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
    
    public void loginMenuServiceProviderOperation() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        try {
            while(true) {
                System.out.println("*** Service Provider Terminal :: Main ***\n");
                System.out.println("You are login as " + currentServiceProvider.getName());
                System.out.println("1: View Profile");
                System.out.println("2: Edit Profile");
                System.out.println("3: View Appointments");
                System.out.println("4: Cancel Appointments");
                System.out.println("5: Logout");
                response = 0;
                
                while(response < 1 || response > 5) {
                    System.out.print("> ");

                    response = scanner.nextInt();
                    System.out.println();
                    if(response == 1) {
                        viewProfile();
                    } else if (response == 2) {
                        editProfile();
                    } else if (response == 3) {
                        viewAppointments();
                    } else if (response == 4) {
                        cancelAppointments();
                    } else if (response == 5) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");                
                    }
                }
                if(response == 5) {
                    break;
                }
            }
        } catch (InputMismatchException ex) {
            System.out.println("Input is invalid! Please input a number from 1 to 5.\n");
            loginMenuServiceProviderOperation();
        }
    }
    
    // to edit
    public void viewProfile() {
        System.out.println("*** Service Provider Terminal :: Your Profile ***\n");
        System.out.print("-----------------------------------------------------------------------------");
        System.out.printf("%22s%40s\n", "Name: ", currentServiceProvider.getName());
        System.out.printf("%22s%40s\n", "Average Rating: ", currentServiceProvider.getAvgRating());
        System.out.printf("%22s%40s\n", "Registration Number: ", currentServiceProvider.getBizRegNum());
        System.out.printf("%22s%40s\n", "Business Address: ", currentServiceProvider.getBizAddress());
        System.out.printf("%22s%40s\n", "City: ", currentServiceProvider.getCity());
        System.out.printf("%22s%40s\n", "Email: ", currentServiceProvider.getEmail());
        System.out.printf("%22s%40s\n", "Contact Number: ", currentServiceProvider.getPhoneNum());
        System.out.printf("%22s%40s\n", "Status: ", currentServiceProvider.getStatus());
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Press any key to go back to the main menu");
        scanner.nextLine();
    }
    
    //to edit
    public void editProfile() {
        System.out.println("*** Service Provider Terminal :: Edit Profile ***\n");
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter city (blank if no change)> ");
        String city = scanner.nextLine().trim();
        if (!city.isEmpty()) {
            currentServiceProvider.setCity(city);
        }
        
        System.out.print("Enter business address (blank if no change)> ");
        String businessAdd = scanner.nextLine().trim();
        if (!businessAdd.isEmpty()) {
            currentServiceProvider.setBizAddress(businessAdd);
        }
        
        System.out.print("Enter emaill address (blank if no change> ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) {
            currentServiceProvider.setEmail(email);
        }
        
        System.out.print("Enter phone number (blank if no change> ");
        String phoneNum = scanner.nextLine().trim();
        if (!phoneNum.isEmpty()) {
            currentServiceProvider.setPhoneNum(phoneNum);
        }
        
        System.out.print("Enter password (blank if no change> ");
        String password = scanner.nextLine().trim();
        if (!password.isEmpty()) {
            currentServiceProvider.setPassword(password);
        }
        
        if (city.isEmpty() && businessAdd.isEmpty() && email.isEmpty() && phoneNum.isEmpty() && password.isEmpty()) {
            System.out.println("No changes detected. Profile has not been updated!\n");
        } else {
            serviceProviderEntitySessionBeanRemote.updateServiceProviderEntity(currentServiceProvider);
            System.out.println("Profile has been updated successfully!\n");
        }
    }
    
    //to edit
    public void viewAppointments() {
        System.out.println("Not supported yet.");
    }
    
    //to edit
    public void cancelAppointments() {
        System.out.println("Not supported yet.");
    }
}
