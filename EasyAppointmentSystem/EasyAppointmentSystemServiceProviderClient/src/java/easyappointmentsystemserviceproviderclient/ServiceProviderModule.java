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
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import util.enumeration.ServiceProviderStatusEnum;
import util.exception.AppointmentCancellationException;
import util.exception.AppointmentNotFoundException;
import util.exception.EntityAttributeNullException;
import util.exception.InvalidLoginException;
import util.exception.ServiceProviderAlreadyExistsException;
import util.exception.ServiceProviderNotFoundException;
import util.exception.UniqueFieldExistsException;

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
                            loginMenuServiceProviderOperation();
                        } catch (InvalidLoginException ex) {
                            System.out.println(ex.getMessage());
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
            for (int i = 1; i <= allCategories.size(); i++) {
                System.out.print(i + " " + allCategories.get(i-1).getCategory() + " ");
                if (i != allCategories.size()) {
                    System.out.print("| ");
                }
            }
            
            System.out.println();
            
            while (true) {
                System.out.print("Enter Business Category> ");
                try {
                    newServiceProviderEntity.setBizCategory(allCategories.get(scanner.nextInt() - 1)); 
                    scanner.nextLine();
                    break;
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.out.println("Error: No such category exists!");
                }
            }
            
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
            
            newServiceProviderEntity.setStatus(ServiceProviderStatusEnum.PENDING);
            serviceProviderEntitySessionBeanRemote.addNewServiceProvider(newServiceProviderEntity);
            System.out.println("You have successfully registered as service provider! Please proceed to login.");
            
        } catch (InputMismatchException ex) {
            System.out.println("Input is invalid! Please try again.");
        } catch (EntityAttributeNullException | ServiceProviderAlreadyExistsException ex) {
            System.out.println(ex.getMessage());
            System.out.println();
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
       
        if(emailAdd.length() > 0 && password.length() > 0) {
            currentServiceProvider = serviceProviderEntitySessionBeanRemote.login(emailAdd, password);
            System.out.println("Login successful!\n");
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
                System.out.println("5: Logout\n");
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
    
    public void viewProfile() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Service Provider Terminal :: Your Profile ***\n");
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("%22s%40s\n", "Name: ", currentServiceProvider.getName());
        double averageRating = serviceProviderEntitySessionBeanRemote.getAverageRating(currentServiceProvider);
        String doubleFormat = String.format("%.2f", averageRating);
        System.out.printf("%22s%40s\n", "Average Rating: ", averageRating == -1.0 ? "Not rated yet!": doubleFormat);
        System.out.printf("%22s%40s\n", "Registration Number: ", currentServiceProvider.getBizRegNum());
        System.out.printf("%22s%40s\n", "Business Address: ", currentServiceProvider.getBizAddress());
        System.out.printf("%22s%40s\n", "City: ", currentServiceProvider.getCity());
        System.out.printf("%22s%40s\n", "Email: ", currentServiceProvider.getEmail());
        System.out.printf("%22s%40s\n", "Contact Number: ", currentServiceProvider.getPhoneNum());
        System.out.printf("%22s%40s\n", "Status: ", currentServiceProvider.getStatus());
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println();
        System.out.println("Press any key to go back to the main menu");
        scanner.nextLine();
    }
    
    public void editProfile() {
        try {
            ServiceProviderEntity serviceProviderToUpdate = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityById(currentServiceProvider.getServiceProviderId());
            System.out.println("*** Service Provider Terminal :: Edit Profile ***\n");
            Scanner scanner = new Scanner(System.in);
            String email = "";
            String phoneNum = "";
            String password = "";

            System.out.print("Enter city (blank if no change)> ");
            String city = scanner.nextLine().trim();
            if (!city.isEmpty()) {
                serviceProviderToUpdate.setCity(city);
            }

            System.out.print("Enter business address (blank if no change)> ");
            String businessAdd = scanner.nextLine().trim();
            if (!businessAdd.isEmpty()) {
                serviceProviderToUpdate.setBizAddress(businessAdd);
            }

            while (true) {
                System.out.print("Enter emaill address (blank if no change> ");
                email = scanner.nextLine().trim();
                if (!email.isEmpty() & checkEmailIsValid(email)) {
                    serviceProviderToUpdate.setEmail(email);
                    break;
                } else if (email.isEmpty()) {
                    break;
                } else {
                    System.out.println("Please input a valid email address.");
                }
            }

            while (true) {
                System.out.print("Enter phone number (blank if no change> ");
                phoneNum = scanner.nextLine().trim();
                if (!phoneNum.isEmpty() && checkNoLetters(phoneNum) && phoneNum.length() == 8) {
                    serviceProviderToUpdate.setPhoneNum(phoneNum);
                    break;
                } else if (phoneNum.isEmpty()) {
                    break;
                } else {
                    System.out.println("Please input a valid phone number with 8 digits.");
                }
            }

            while (true) {
                System.out.print("Enter password (blank if no change> ");
                password = scanner.nextLine().trim();
                if (!password.isEmpty() && password.length() == 6 && checkNoLetters(password)) {
                    serviceProviderToUpdate.setPassword(password);
                    break;
                } else if (password.isEmpty()) {
                    break;
                } else {
                    System.out.println("Please input a valid password with 6 digits.");
                }
            }

            if (city.isEmpty() && businessAdd.isEmpty() && email.isEmpty() && phoneNum.isEmpty() && password.isEmpty()) {
                System.out.println("No changes detected. Profile has not been updated!\n");
            } else {
                try {
                    serviceProviderEntitySessionBeanRemote.updateServiceProviderEntity(serviceProviderToUpdate);
                    System.out.println("Profile has been updated successfully!\n");
                } catch (EntityAttributeNullException | UniqueFieldExistsException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (ServiceProviderNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void viewAppointments() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Service Provider Terminal :: View Appointments ***");
        System.out.println();
        System.out.println("Appointments\n");
        List<AppointmentEntity> appointments = new ArrayList<AppointmentEntity>();
        System.out.printf("%-20s%-14s%-10s%-15s%-20s\n", "Name", "Date", "Time", "Appoint No.", "Status");
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
                String apptStatus = "";
                if (a.getCancelled()) {
                    apptStatus = "Cancelled";
                } else if (a.getStartTimestamp().getTime() - new Date(System.currentTimeMillis()).getTime() < 0) {
                    apptStatus = "Already taken";
                } else {
                    apptStatus = "Active";
                }
                System.out.printf("%-20s%-14s%-10s%-15s%-20s\n", 
                        a.getCustomerEntity().getFirstName() + " " + a.getCustomerEntity().getLastName(),
                        dateFormat.format(a.getStartTimestamp()),
                        timeFormat.format(a.getStartTimestamp()),
                        a.getAppointmentNum(), apptStatus);
            }
        }
        System.out.println("Press any key to go back to the main menu");
        scanner.nextLine();
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
