package easyappointmentsystemserviceproviderclient;

import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.CategoryEntitySessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.AppointmentEntity;
import entity.CategoryEntity;
import entity.RatingEntity;
import entity.ServiceProviderEntity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import util.enumeration.ServiceProviderStatusEnum;
import util.exception.AppointmentCancellationException;
import util.exception.AppointmentNotFoundException;
import util.exception.EntityAttributeNullException;
import util.exception.InvalidLoginException;
import util.exception.ServiceProviderNotFoundException;

public class ServiceProviderModule {
    
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    private CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote;
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private ServiceProviderEntity currentServiceProvider;
    
    public ServiceProviderModule(){
    }
    
    public ServiceProviderModule(ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote, CategoryEntitySessionBeanRemote categoryEntitySessionBeanRemote, AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote){
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
        this.categoryEntitySessionBeanRemote = categoryEntitySessionBeanRemote;
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
    }
    
    public void menuServiceProviderOperation() {
        
        int response;
        Scanner scanner = new Scanner(System.in);
        try {
            while(true) {
                System.out.println("*** Welcome to Service provider terminal ***\n");
                System.out.println("1: Registration");
                System.out.println("2: Login");
                System.out.println("3: Exit\n");
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
                        System.out.println("Error: Invalid input value! Please enter a value between 1 and 3.");
                    }
                }
                if(response == 3) {
                    break;
                }
            }
        } catch (InputMismatchException ex) {
            System.out.println("Error: Invalid input type entered! Please enter the correct input.");
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
            
            //need to check that the number exists
            System.out.print("Enter Business Category> ");
            newServiceProviderEntity.setBizCategory(scanner.nextInt()); 
            scanner.nextLine();
            
            System.out.print("Enter Business Registration Number> ");
            newServiceProviderEntity.setBizRegNum(scanner.nextLine().trim());
            
            System.out.print("Enter City> ");
            newServiceProviderEntity.setCity(scanner.nextLine().trim());
            
            while (true) {
                System.out.print("Enter Phone> ");
                String phoneNum = scanner.nextLine().trim();
                if (checkNoLetters(phoneNum) && phoneNum.length() == 8) {
                    newServiceProviderEntity.setPhoneNum(phoneNum);
                    break;
                } else {
                    System.out.println("Error: Invalid phone number! PLease enter a valid phone number with 8 digits.");
                }
            }
            
            System.out.print("Enter Business Address> ");
            newServiceProviderEntity.setBizAddress(scanner.nextLine().trim());
            
            while (true) {
                System.out.print("Enter Email> ");
                String input = scanner.nextLine().trim();
                if (checkEmailIsValid(input)) {
                    newServiceProviderEntity.setEmail(input);
                    break;
                } else {
                    System.out.println("Error: Invalid email! Please enter a valid email.");
                }
            }
            
            while (true) {
                System.out.print("Enter Password> ");
                String password = scanner.nextLine().trim();
                if (checkNoLetters(password) && password.length() == 6) {
                    newServiceProviderEntity.setPassword(password);
                    break;
                } else {
                    System.out.println("Error: Invalid password! Please enter 6-digit numeric password.");
                }
            }
            
            newServiceProviderEntity.setRatings(new ArrayList<RatingEntity>());
            newServiceProviderEntity.setAppointments(new ArrayList<AppointmentEntity>());
            newServiceProviderEntity.setStatus(ServiceProviderStatusEnum.PENDING);
            serviceProviderEntitySessionBeanRemote.addNewServiceProvider(newServiceProviderEntity);
            System.out.println("You have successfully registered as service provider! Please proceed to login.");
            
        } catch (InputMismatchException ex) {
            System.out.println("Input is invalid! Please try again.");
        } catch (EntityAttributeNullException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public boolean checkNoLetters(String input) {
        return (input.matches("[0-9]+")); 
    }
    
    public boolean checkEmailIsValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
        if (email.isEmpty()) {
            return false;
        } else {
            Pattern pattern = Pattern.compile(emailRegex);
            return pattern.matches(emailRegex, email);
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
        
        try {
            if(emailAdd.length() > 0 && password.length() > 0) {
                currentServiceProvider = serviceProviderEntitySessionBeanRemote.login(emailAdd, password);      
            } else {
                throw new InvalidLoginException("Missing login credential!");
            } 
        } catch (InvalidLoginException ex) {
            System.out.println(ex.getMessage());
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
                System.out.println("5: Logout\n");
                response = 0;
                
                while(response < 1 || response > 5) {
                    System.out.print("> ");

                    response = scanner.nextInt();
                    System.out.println();
                    if(response == 1) {
                        viewProfile();
                        System.out.println("Press any key to go back to the main menu");
                        scanner.nextLine();
                    } else if (response == 2) {
                        editProfile();
                    } else if (response == 3) {
                        viewAppointments();
                        System.out.println();
                        System.out.println("Press any key to go back to the main menu");
                        scanner.nextLine();
                        System.out.println();
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
    
    public void viewProfile() {
        System.out.println("*** Service Provider Terminal :: Your Profile ***\n");
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("%22s%40s\n", "Name: ", currentServiceProvider.getName());
        System.out.printf("%22s%40s\n", "Average Rating: ", serviceProviderEntitySessionBeanRemote.getAverageRating(currentServiceProvider));
        System.out.printf("%22s%40s\n", "Registration Number: ", currentServiceProvider.getBizRegNum());
        System.out.printf("%22s%40s\n", "Business Address: ", currentServiceProvider.getBizAddress());
        System.out.printf("%22s%40s\n", "City: ", currentServiceProvider.getCity());
        System.out.printf("%22s%40s\n", "Email: ", currentServiceProvider.getEmail());
        System.out.printf("%22s%40s\n", "Contact Number: ", currentServiceProvider.getPhoneNum());
        System.out.printf("%22s%40s\n", "Status: ", currentServiceProvider.getStatus());
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println();
    }
    
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
            try {
                serviceProviderEntitySessionBeanRemote.updateServiceProviderEntity(currentServiceProvider);
            } catch (EntityAttributeNullException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println("Profile has been updated successfully!\n");
        }
    }
    
    public void viewAppointments() {
        System.out.println("*** Service Provider Terminal :: View Appointments ***");
        System.out.println();
        System.out.println("Appointments\n");
        List<AppointmentEntity> appointments = new ArrayList<AppointmentEntity>();
        System.out.printf("%-20s%-14s%-10s%-15s\n", "Name", "Date", "Time", "Appoint No.");
        try {
            appointments = serviceProviderEntitySessionBeanRemote.retrieveAppointmentsByServiceProviderId(currentServiceProvider.getServiceProviderId());
        } catch (ServiceProviderNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
        if (appointments.isEmpty()) {
            System.out.println("You do not have any appointments at the moment.");
        } else {
            String datePattern = "yyyy-MM-dd";
            String timePattern = "HH:mm";
            DateFormat dateFormat = new SimpleDateFormat(datePattern);
            DateFormat timeFormat = new SimpleDateFormat(timePattern);
            
            for (AppointmentEntity a : appointments) {
                System.out.printf("%-20s%-14s%-10s%-15s\n", 
                        a.getCustomerEntity().getFirstName() + " " + a.getCustomerEntity().getLastName(),
                        dateFormat.format(a.getStartTimestamp()),
                        timeFormat.format(a.getStartTimestamp()),
                        a.getAppointmentNum());
            }
        }
    }
    
    
    public void cancelAppointments() {
        System.out.println("*** Service Provider Terminal :: Cancel Appointment ***\n");
        System.out.println();
        System.out.println("Appointments\n");
        List<AppointmentEntity> appointments = new ArrayList<AppointmentEntity>();
        System.out.printf("%-20s%-14s%-10s%-15s\n", "Name", "Date", "Time", "Appoint No.");
        try {
            appointments = serviceProviderEntitySessionBeanRemote.retrieveAppointmentsByServiceProviderId(currentServiceProvider.getServiceProviderId());
        } catch (ServiceProviderNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
        if (appointments.isEmpty()) {
            System.out.println("You do not have any appointments at the moment.");
        } else {
            String datePattern = "yyyy-MM-dd";
            String timePattern = "HH:mm";
            DateFormat dateFormat = new SimpleDateFormat(datePattern);
            DateFormat timeFormat = new SimpleDateFormat(timePattern);
            
            for (AppointmentEntity a : appointments) {
                System.out.printf("%-20s%-14s%-10s%-15s\n", 
                        a.getCustomerEntity().getFirstName() + " " + a.getCustomerEntity().getLastName(),
                        dateFormat.format(a.getStartTimestamp()),
                        timeFormat.format(a.getStartTimestamp()),
                        a.getAppointmentNum());
            }
            
            System.out.print("Enter appointment ID> ");
            Scanner scanner = new Scanner(System.in);
            String appointmentNum = scanner.nextLine().trim();
            try {
                AppointmentEntity appointment = appointmentEntitySessionBeanRemote.retrieveAppointmentEntityByAppointmentNumber(appointmentNum);
                appointmentEntitySessionBeanRemote.cancelAppointment(appointment.getAppointmentId());
                System.out.println("Appointment " + appointmentNum + " has been cancelled successfully.");
                System.out.println("Press any key to go back to the main menu.");
                scanner.nextLine();
            } catch (AppointmentNotFoundException ex) {
                System.out.println(ex.getMessage());
            } catch (AppointmentCancellationException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println();
        }
    }
}
