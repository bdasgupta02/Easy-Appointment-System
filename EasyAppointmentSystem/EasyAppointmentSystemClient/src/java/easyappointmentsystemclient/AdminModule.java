package easyappointmentsystemclient;

import ejb.session.stateless.AdminEntitySessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.AdminEntity;
import entity.AppointmentEntity;
import entity.ServiceProviderEntity;
import java.util.List;
import java.util.Scanner;
import util.enumeration.ServiceProviderStatusEnum;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginException;
import util.exception.ServiceProviderAlreadyBlockedException;
import util.exception.ServiceProviderNotFoundException;

public class AdminModule {

    private AdminEntitySessionBeanRemote adminEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    private AdminEntity loggedAdmin;

    public AdminModule() {
    }

    public AdminModule(AdminEntitySessionBeanRemote adminEntitySessionBeanRemote,
            ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote) {
        this.adminEntitySessionBeanRemote = adminEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
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
            System.out.println("Login unsuccessful. Please enter valid login details.\n");
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
                    + "10: Logout\n");
            System.out.print("> ");

            while (response < 1 || response > 9) {
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

                    } else if (response == 7) {
                    } else if (response == 8) {
                    } else if (response == 9) {
                        break;
                    } else {
                        System.out.println("Error: Invalid input value! Please enter the correct input.");
                    }
                } else {
                    System.out.println("Error: Invalid input type entered! Please enter the correct input.");
                }
            }
            if (response == 11) {
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
                                    a.getDate(),
                                    a.getStartTime(),
                                    a.getAppointmentNum());
                        }

                        System.out.println("Enter 0 to go back to the previous menu.\n");
                        viewed = true;
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
                        System.out.printf("%20s%15s%15s%15s%15s\n", "Name ", "| Business Category ", "| Date ", "| Time ", "| Appointment No. ");

                        //FORMATTING ISSUES
                        for (AppointmentEntity a : appointments) {
                            System.out.printf("%20s%15s%15s%15s%15s\n", a.getCustomerEntity().getFirstName() + " " + a.getCustomerEntity().getLastName(),
                                    a.getServiceProviderEntity().getBizCategory(),
                                    a.getDate(),
                                    a.getStartTime(),
                                    a.getAppointmentNum());
                        }
                        System.out.println("Enter 0 to go back to the previous menu.\n");
                        viewed = true;
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
            System.out.printf("%10s%15s%15s%15s%15s%15s\n",
                    s.getServiceProviderId(),
                    s.getName(),
                    s.getBizCategory(),
                    s.getCity(),
                    s.getAvgRating(),
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
                    } catch (ServiceProviderNotFoundException ex) {
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
                        } catch (ServiceProviderAlreadyBlockedException ex) {
                            System.out.println(ex.getMessage() + " Please try again.");
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
}
