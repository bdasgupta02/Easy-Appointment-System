package easyappointmentsystemadminclient;

import ejb.session.stateless.AdminEntitySessionBeanRemote;
import ejb.session.stateless.CategoryEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.AdminEntity;
import entity.AppointmentEntity;
import entity.CategoryEntity;
import entity.ServiceProviderEntity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.ServiceProviderStatusEnum;
import util.exception.CategoryInUseException;
import util.exception.CategoryNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.EntityAttributeNullException;
import util.exception.InvalidLoginException;
import util.exception.ServiceProviderAlreadyBlockedException;
import util.exception.ServiceProviderNotFoundException;

public class AdminModule {

    private AdminEntitySessionBeanRemote adminEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    private CategoryEntitySessionBeanRemote categorySessionBeanRemote;
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote ;
    private AdminEntity loggedAdmin;

    public AdminModule() {
    }

    public AdminModule(AdminEntitySessionBeanRemote adminEntitySessionBeanRemote,
                       ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote, 
                       CategoryEntitySessionBeanRemote categorySessionBeanRemote,
                       CustomerEntitySessionBeanRemote customerSessionBeanRemote)
    {
        this.adminEntitySessionBeanRemote = adminEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerSessionBeanRemote;
    }

    public void adminStartMenu() {
        Scanner sc = new Scanner(System.in);
        int response = 0;

        while (true) {
            System.out.println("*** Welcome to Admin terminal ***");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;

            while (response < 1 || response > 2) {

                // fixed input type check
                System.out.print("> ");

                if (!sc.hasNextInt()) {
                    sc.nextLine();
                    System.out.println("Error: Invalid input type entered! Please enter the correct input.");
                    System.out.println();
                } else {
                    response = sc.nextInt();

                    if (response == 1) {
                        adminLoginMenu();
                    } else if (response == 2) {
                        break;
                    } else {
                        System.out.println("Error: Invalid input value! Please enter the correct input.");
                        break;
                    }
                    break;
                }
            }
            if (response == 2) {
                break;
            }
        }

    }

    public void adminLoginMenu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: Login ***\n");

        try {
            System.out.print("Enter Email Address> ");
            String email = sc.nextLine().trim();
            System.out.print("Enter Password> ");
            String password = sc.nextLine().trim();
            loggedAdmin = adminEntitySessionBeanRemote.adminLogin(email, password);
            System.out.println("Login successful!");
            System.out.println();
            adminMainMenu();

        } catch (NullPointerException | InvalidLoginException ex) {
            System.out.println("Login unsuccessful. Please enter valid login details ." + ex.getMessage() + "\n");
        }

    }

    private void adminMainMenu() {
        Scanner sc = new Scanner(System.in);
        int response = 0;

        while (true) {
            response = 0;

            System.out.println("*** Admin terminal :: Main ***\n");
            System.out.println("You are login as " + loggedAdmin.getFirstName());
            System.out.println("1: View Appointments for customers\n"
                    + "2: View Appointments for service providers\n"
                    + "3: View service providers\n"
                    + "4: Approve service provider\n"
                    + "5: Block service provider\n"
                    + "6: Add Business category\n"
                    + "7: Remove Business category\n"
                    + "8: Send reminder email\n"
                    + "9: Delete Customer\n"
                    + "10: Logout\n");
            System.out.print("> ");

            while (response < 1 || response > 10) {
                if (sc.hasNextInt()) {
                    response = sc.nextInt();
                    if (response == 1) {
                        viewCustomerAppointments();
                    } else if (response == 2) {
                        viewServiceProviderAppointments();
                    } else if (response == 3) {
                        viewServiceProviders();
                    } else if (response == 4) {
                        approveServiceProvider();
                    } else if (response == 5) {
                        blockServiceProvider();
                    } else if (response == 6) {
                       addBusinessCategory();
                    } else if (response == 7) {
                        removeBusinessCategory();
                    } else if (response == 8) {
                        sendReminderEmail();
                    } else if (response == 9){
                        deleteCustomer();
                    } else if (response == 10) {
                        break;
                    } else {
                        System.out.println("Error: Invalid input value! Please enter the correct input.");
                    }
                } else {
                    System.out.println("Error: Invalid input type entered! Please enter the correct input.");
                }
            }
            if (response == 9) {
                break;
            }
        }
    }

    private void viewCustomerAppointments() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: View Appointments for customers ***\n");
        long customerId = 0;
        boolean viewed = false;
        List<AppointmentEntity> appointments;

        // date and time formatting
        String datePattern = "yyyy-MM-dd";
        String timePattern = "HH:mm";
        DateFormat dateFormat = new SimpleDateFormat(datePattern);
        DateFormat timeFormat = new SimpleDateFormat(timePattern);
        
        while (true) {
            System.out.print("Enter customer Id> ");

            if (sc.hasNextLong()) {
                customerId = sc.nextLong();
                sc.nextLine();
                if (customerId < 0) {
                    System.out.println("Error: Invalid input value! Please enter the correct input.");
                } else if (customerId == 0) {
                    break;
                } else if (!viewed) {
                    try {
                        appointments = adminEntitySessionBeanRemote.retrieveAppointmentEntityByCustomerId(customerId);
                        System.out.printf("%20s%15s%15s%15s%15s\n", "Name ", "| Business Category ", "| Date ", "| Time ", "| Appointment No. ");
                        
                        // lazy fetching issues fixed
                        // need to print business category in text
                        for (AppointmentEntity a : appointments) {
                            System.out.printf("%20s%15s%15s%15s%15s\n", a.getCustomerEntity().getFirstName() + " " + a.getCustomerEntity().getLastName(),
                                    a.getServiceProviderEntity().getBizCategory(),
                                    dateFormat.format(a.getStartTimestamp()),
                                    timeFormat.format(a.getStartTimestamp()),
                                    a.getAppointmentNum());
                        }

                        System.out.println("Enter 0 to go back to the previous menu.\n");
                        //viewed = true; If this is set to true, this loop never entered again. 
                    } catch (CustomerNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            } else {
                System.out.println("Error: Invalid input type entered! Please enter the correct input.");
            }
        }
    }

    private void viewServiceProviderAppointments() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: View Appointments for service providers ***\n");
        long serviceProviderId = 0;
        boolean viewed = false;
        List<AppointmentEntity> appointments;
        
        // date and time formatting
        String datePattern = "yyyy-MM-dd";
        String timePattern = "HH:mm";
        DateFormat dateFormat = new SimpleDateFormat(datePattern);
        DateFormat timeFormat = new SimpleDateFormat(timePattern);

        while (true) {
            System.out.print("Enter service provider Id> ");

            if (sc.hasNextLong()) {
                serviceProviderId = sc.nextLong();
                sc.nextLine();
                if (serviceProviderId < 0) {
                    System.out.println("Error: Invalid input value! Please enter the correct input.");
                } else if (serviceProviderId == 0) {
                    break;
                } else if (!viewed) {
                    try {
                        appointments = adminEntitySessionBeanRemote.retrieveAppointmentEntityByServiceProviderId(serviceProviderId);
                        System.out.printf("%4s%27s%16s%16s%27s\n", "Name", "| Business Category", "| Date", "| Time", "| Appointment No. ");
                        System.out.println();
                        //FORMATTING ISSUES
                        for (AppointmentEntity a : appointments) {
                            System.out.printf("%4s%27s%16s%16s%27s\n", a.getCustomerEntity().getFirstName() + " " + a.getCustomerEntity().getLastName(),
                                    a.getServiceProviderEntity().getBizCategory(),
                                    dateFormat.format(a.getStartTimestamp()),
                                    timeFormat.format(a.getStartTimestamp()),
                                    a.getAppointmentNum());
                        }
                        System.out.println("Enter 0 to go back to the previous menu.\n");
                       // viewed = true; --> If this is set to true, then this loop will not be added. 
                    } catch (ServiceProviderNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            } else {
                System.out.println("Error: Invalid input type entered! Please enter the correct input.");
            }
        }
    }

    private void viewServiceProviders() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: View service provider ***\n");
        System.out.printf("%10s%15s%15s%15s%15s%15s\n", "Id ", "| Name ", "| Business category ", "| City ", "| Overall rating ", "| Status ");

        List<ServiceProviderEntity> serviceProviders = serviceProviderEntitySessionBeanRemote.retrieveAllServiceProviders();

        for (ServiceProviderEntity s : serviceProviders) {
            double averageRating = serviceProviderEntitySessionBeanRemote.getAverageRating(s);
            String doubleFormat = String.format("%.2f", averageRating);
            System.out.printf("%10s%15s%15s%15s%15s%15s\n",
                    s.getServiceProviderId(),
                    s.getName(),
                    s.getBizCategory(),
                    s.getCity(),
                    averageRating == -1.0 ? "Not rated yet!": doubleFormat,
                    statusEnumConverter(s.getStatus()));
        }

        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }

    private String statusEnumConverter(ServiceProviderStatusEnum status) {
        if (status == ServiceProviderStatusEnum.APPROVED) {
            return "Approved";
        } else if (status == ServiceProviderStatusEnum.BLOCKED) {
            return "Blocked";
        } else {
            return "Pending";
        }
    }

    private void approveServiceProvider() {
        Scanner sc = new Scanner(System.in);
        Long serviceProviderId;
        System.out.println("*** Admin terminal :: Approve service provider ***\n");
        System.out.println("List of service providers with pending approval:\n");
        System.out.printf("%10s%15s%15s%15s%15s%20s%15s%15s\n", "Id ", "| Name ", "| Business category ", "| Business Reg. No. ", "| City ", "| Address ", "| Email ", "| Phone ");

        List<ServiceProviderEntity> serviceProviders = serviceProviderEntitySessionBeanRemote.retrieveAllPendingServiceProviders();

        for (ServiceProviderEntity s : serviceProviders) {
            System.out.printf("%10s%15s%15s%15s%15s%20s%15s%15s\n",
                    s.getServiceProviderId(),
                    s.getName(),
                    s.getBizCategory(),
                    s.getBizRegNum(),
                    s.getCity(),
                    s.getBizAddress(),
                    s.getEmail(),
                    s.getPhoneNum());
        }

        while (true) {
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print("Enter service provider Id> ");

            if (sc.hasNextLong()) {
                serviceProviderId = sc.nextLong();
                sc.nextLine();
                if (serviceProviderId < 0) {
                    System.out.println("Error: Invalid input value! Please enter the correct input.");
                } else if (serviceProviderId == 0) {
                    break;
                } else {
                    try {
                        ServiceProviderEntity serviceProviderEntity = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityById(serviceProviderId);
                        serviceProviderEntitySessionBeanRemote.approveServiceProviderStatus(serviceProviderEntity);
                        System.out.println(serviceProviderEntity.getName() + "'s registration is approved.");
                    } catch (EntityAttributeNullException | ServiceProviderNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            } else {
                System.out.println("Error: Invalid input type entered! Please enter the correct input.");
            }
        }
    }

    private void blockServiceProvider() {
        Scanner sc = new Scanner(System.in);
        Long serviceProviderId;
        System.out.println("*** Admin terminal :: Block service provider ***\n");
        System.out.println("List of service providers with pending approval:\n");
        System.out.printf("%10s%15s%15s%15s%15s%20s%15s%15s\n", "Id ", "| Name ", "| Business category ", "| Business Reg. No. ", "| City ", "| Address ", "| Email ", "| Phone ");

        List<ServiceProviderEntity> serviceProviders = serviceProviderEntitySessionBeanRemote.retrieveAllServiceProviders();

        for (ServiceProviderEntity s : serviceProviders) {
            System.out.printf("%10s%15s%15s%15s%15s%20s%15s%15s\n",
                    s.getServiceProviderId(),
                    s.getName(),
                    s.getBizCategory(),
                    s.getBizRegNum(),
                    s.getCity(),
                    s.getBizAddress(),
                    s.getEmail(),
                    s.getPhoneNum());
        }

        while (true) {
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print("Enter service provider Id> ");

            if (sc.hasNextLong()) {
                serviceProviderId = sc.nextLong();
                sc.nextLine();
                if (serviceProviderId < 0) {
                    System.out.println("Error: Invalid input value! Please enter the correct input.");
                } else if (serviceProviderId == 0) {
                    break;
                } else {
                    try {
                        ServiceProviderEntity serviceProviderEntity = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityById(serviceProviderId);
                        try {
                            serviceProviderEntitySessionBeanRemote.blockServiceProviderStatus(serviceProviderEntity);
                        } catch (EntityAttributeNullException | ServiceProviderAlreadyBlockedException ex) {
                            System.out.println(ex.getMessage());
                        }
                        System.out.println(serviceProviderEntity.getName() + "'s registration is approved.");
                    } catch (ServiceProviderNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            } else {
                System.out.println("Error: Invalid input type entered! Please enter the correct input.");
            }
        }
    }

    private void addBusinessCategory() {
        Scanner sc = new Scanner(System.in);
        String name = null;
        System.out.println("*** Admin terminal :: Add a Business category ***\n");
        
        while (true) {
            System.out.print("\nEnter 0 to go back to the previous menu.\n" +
                                "Enter a new business category> ");
            name = sc.nextLine().trim();
            System.out.println("Name is: " + name);
            if(name != null)
            {
               if(name.equals("0"))
               {
                   break;
               } else 
               {
                   try {
                       CategoryEntity newCat = new CategoryEntity(name); 
                        categorySessionBeanRemote.addNewCategory(newCat); //THROWING NULL POINTER
                      System.out.println("The business category “"+ name +"” is added.");
                   } catch (EntityAttributeNullException | NullPointerException ex) {
                       System.out.println("Unable to create category. Please enter a valid input!");
                   }
               }
            } else {
                System.out.println("Please enter a valid input!");
            }
        }
    }

    private void removeBusinessCategory() {
          Scanner sc = new Scanner(System.in);
          int idToDelete = -1;
          System.out.println("*** Admin terminal :: Remove a Business category ***\n");
        
          while(true){
              System.out.println("Categories: ");
               List<CategoryEntity> allCategories = categorySessionBeanRemote.retrieveAllCategories();
              for (CategoryEntity category : allCategories) {
                System.out.print(category.getCategoryId() + "  " + category.getCategory() + " ");
                if (allCategories.indexOf(category) != allCategories.size() - 1) {
                    System.out.print("| ");
                }
            }
            System.out.println();  
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print("Enter Business Category> ");
             if (sc.hasNextInt()) {
                 idToDelete = sc.nextInt();
                 sc.nextLine();
              if (idToDelete < 0) {
                  System.out.println("Error: Invalid input value! Please enter the correct input.");
              } else if (idToDelete == 0) {
                  break;
              } else {
                     try {
                         categorySessionBeanRemote.deleteCategory(allCategories.get(idToDelete).getCategory());
                     } catch (CategoryNotFoundException | CategoryInUseException ex) {
                         System.out.println("Category could not be deleted because " + ex.getMessage());
                     }
              }
            } else {
                System.out.println("Error: Invalid input type entered! Please enter the correct input.");
            }
          }
    }

    private void sendReminderEmail() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void deleteCustomer() {
       Scanner sc = new Scanner(System.in);
        Long customerId;
        System.out.println("*** Admin terminal :: Delete Customer ***\n");
       

        while (true) {
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print("Enter customer Id> ");

            if (sc.hasNextLong()) {
                customerId = sc.nextLong();
                sc.nextLine();
                if (customerId < 0) {
                    System.out.println("Error: Invalid input value! Please enter the correct input.");
                } else if (customerId == 0) {
                    break;
                } else {
                    try {
                        customerEntitySessionBeanRemote.deleteCustomerEntity(customerId);
                        System.out.println("Customer with id: " + customerId + " deleted successfully!");
                    } catch (CustomerNotFoundException ex) {
                        System.out.println("Could not delete customer: " + ex.getMessage());
                    }
                }
            } else {
                System.out.println("Error: Invalid input type entered! Please enter the correct input.");
            }
        }
    }
}

