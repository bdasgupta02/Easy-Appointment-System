package customerwebserviceclient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.client.AppointmentAlreadyExistsException_Exception;
import ws.client.AppointmentCancellationException_Exception;
import ws.client.AppointmentEntity;
import ws.client.AppointmentNotFoundException_Exception;
import ws.client.CategoryEntity;
import ws.client.CustomerAlreadyExistsException_Exception;
import ws.client.CustomerEntity;
import ws.client.CustomerNotFoundException_Exception;
import ws.client.DateProcessingException_Exception;
import ws.client.EntityAttributeNullException_Exception;
import ws.client.InvalidLoginException_Exception;
import ws.client.RatingWithoutAppointmentException_Exception;
import ws.client.ServiceProviderAlreadyRatedException_Exception;
import ws.client.ServiceProviderEntity;
import ws.client.ServiceProviderNotFoundException_Exception;

public class CustomerWebServiceClient {

    private CustomerEntity customerEntity;
    private String loggedEmail;
    private String loggedPassword;
    public static final int DID_NOT_MATCH = -1;
    public static final boolean SEARCH_ONLY = true;
    public static final boolean NOT_SEARCH_ONLY = false;
    public static final int FIRST_APT_HOUR = 8;
    public static final double UNRATED = -1.00;
    public static final boolean TO_CANCEL = true;
    public static final boolean TO_VIEW = false;

    public CustomerWebServiceClient() {
    }

    public static void main(String[] args) {
        CustomerWebServiceClient customerWebServiceClient = new CustomerWebServiceClient();
        customerWebServiceClient.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        int response = 0;
        while (true) {
            System.out.println("*** Welcome to Customer terminal ***\n");
            System.out.println("1: Registration");
            System.out.println("2: Login");
            System.out.println("3: Exit\n");
            response = 0;
            while (response < 1 || response > 3) {
                System.out.print("> ");
                if (!scanner.hasNextInt()) {
                    scanner.nextLine();
                    System.out.println("Error: Invalid input type entered! Please enter the correct input.\n");
                    break;
                } else {
                    response = scanner.nextInt();
                    scanner.nextLine();
                    switch (response) {
                        case 1:
                            registerInterface();
                            break;
                        case 2:
                            loginInterface();
                            break;
                        case 3:
                            break;
                        default:
                            System.out.println("Error: Invalid input value! Please enter the correct input.\n");
                            break;
                    }
                }
            }
            if (response == 3) {
                break;
            }
        }
    }

    public void loginInterface() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Customer terminal :: Login ***\n");

        try {
            System.out.print("Enter Email Address> ");
            String email = scanner.nextLine().trim();
            System.out.print("Enter Password> ");
            String password = scanner.nextLine().trim();
            this.customerEntity = CustomerWebServiceClient.login(email, password);
            if (this.customerEntity != null) {
                loggedEmail = email;
                loggedPassword = password;
                System.out.println("Login successful!\n");
                mainMenu();
            } else {
                System.out.println("Login unsuccessful. Please enter a valid password!.\n");
            }
        } catch (NullPointerException | InvalidLoginException_Exception ex) {
            System.out.println("Login unsuccessful. Please enter valid login details. Details: " + ex.getMessage());
        }
    }

    public void registerInterface() {
        Scanner scanner = new Scanner(System.in);
        String idNumber;
        String password;
        String firstName;
        String lastName;
        String gender;
        Integer age;
        Long phone;
        String address;
        String city;
        String email;

        try {
            System.out.println("*** Customer Terminal :: Registration Operation ***\n");

            System.out.print("Enter Identity Number (NRIC or Passport)> ");
            idNumber = scanner.nextLine().trim();

            while (true) {
                System.out.print("Enter Password> ");
                password = scanner.nextLine().trim();
                if (password.length() == 6) {
                    break;
                } else {
                    System.out.println("Error: Invalid password! Please enter 6-digit numeric password.");
                }
            }

            System.out.print("Enter First Name> ");
            firstName = scanner.nextLine().trim();

            System.out.print("Enter Last Name> ");
            lastName = scanner.nextLine().trim();

            while (true) {
                System.out.print("Enter Gender (M/F)> ");
                gender = scanner.nextLine().trim();
                if (gender.charAt(0) == 'M' || gender.charAt(0) == 'F') {
                    break;
                } else {
                    System.out.println("Error: Invalid password! Please enter M or F.");
                }
            }

            System.out.print("Enter Age> ");
            age = scanner.nextInt();
            scanner.nextLine();

            while (true) {
                System.out.print("Enter Phone> ");
                phone = scanner.nextLong();
                if (String.valueOf(phone).length() == 8) {
                    break;
                } else {
                    System.out.println("Error: Invalid phone number! PLease enter a valid phone number with 8 digits.");
                }
            }
            scanner.nextLine();

            System.out.print("Enter Address> ");
            address = scanner.nextLine().trim();

            System.out.print("Enter City> ");
            city = scanner.nextLine().trim();

            while (true) {
                System.out.print("Enter Email> ");
                email = scanner.nextLine().trim();
                if (checkEmailIsValid(email)) {
                    break;
                } else {
                    System.out.println("Error: Invalid email! Please enter a valid email.");
                }
            }

            createCustomerEntity(idNumber, firstName, lastName, address, gender, age, city, email, phone, password);
            System.out.println("You have successfully registered as a customer! Please proceed to login.");
        } catch (InputMismatchException ex) {
            System.out.println("Input is invalid! Please try again.");
        } catch (EntityAttributeNullException_Exception | CustomerAlreadyExistsException_Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println();
        }
    }

    private void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
         

        while (true) {
            System.out.println("*** Customer terminal :: Main ***\n");
            System.out.println("You are logged in as " + customerEntity.getFirstName() + " " + customerEntity.getLastName() + " \n");
            System.out.println("1: Search Operation");
            System.out.println("2: Add Appointment");
            System.out.println("3: View Appointments");
            System.out.println("4: Cancel Appointment");
            System.out.println("5: Rate Service Provider");
            System.out.println("6: Logout\n");
            response = 0;
            OUTER:
            while (response < 1 || response > 6) {
                System.out.print("> ");
                if (!scanner.hasNextInt()) {
                    scanner.nextLine();
                    System.out.println("Error: Invalid input type entered! Please enter the correct input.\n");
                } else {
                    response = scanner.nextInt();
                    scanner.nextLine();
                    switch (response) {
                        case 1:
                            addOrSearchAppointments(SEARCH_ONLY);
                            break;
                        case 2:
                            addOrSearchAppointments(NOT_SEARCH_ONLY);
                            break;
                        case 3:
                            viewOrCancelAppointments(TO_VIEW);
                            break;
                        case 4:
                            viewOrCancelAppointments(TO_CANCEL);
                            break;
                        case 5:
                            rateProvider();
                            break;
                        case 6:
                            break OUTER;
                        default:
                            System.out.println("Error: Invalid input value! Please enter the correct input.\n");
                            break;
                    }
                }
            }
            if (response == 6) {
                break;
            }
        }
    }

    public void addOrSearchAppointments(boolean searchOnly) {
        if (searchOnly) {
            System.out.println("*** Customer Terminal :: Search Operation ***\n");
        } else {
            System.out.println("*** Customer Terminal :: Add Appointment ***\n");
        }
        Scanner scanner = new Scanner(System.in);
        Long categoryId;
        String city;
        String dateString;
        Date date;
        Long response;
        try {
            List<CategoryEntity> allCategories = getAllCategories();
            for (int i = 1; i <= allCategories.size(); i++) {
                System.out.print(" " + i + " " + allCategories.get(i - 1).getCategory() + " ");
                if (i != allCategories.size()) {
                    System.out.print("| ");
                }
            }

            System.out.println();

            while (true) {
                System.out.print("Enter Business Category number from above> ");
                try {
                    categoryId = new Long(scanner.nextInt() - 1);
                    scanner.nextLine();
                    break;
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.out.println("Error: No such category exists!\n");
                }
            }

            while (true) {
                System.out.print("Enter Date (YYYY-MM-DD)> ");
                dateString = scanner.nextLine().trim();
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
                    break;
                } catch (ParseException ex) {
                    System.out.println("Error: Invalid date! Please try again.\n");
                }
            }
            
            if (!searchOnly && date.before(new Date(System.currentTimeMillis()))) {
                System.out.println("Error: You cannot set an appointment in the past! \n");
                return;
            } 

            System.out.print("Enter City> ");
            city = scanner.nextLine().trim();

            List<ServiceProviderEntity> serviceProviders = null;
            try {
                serviceProviders = searchServiceProvidersByCategoryCityDate(categoryId, city, toXMLGregorianCalendar(date), loggedEmail, loggedPassword);
            } catch (InvalidLoginException_Exception ex) {
               System.out.println("Error: "+ ex.getMessage());
            }
            System.out.printf("%-5s%-20s%-20s%-15s%-35s%-25s%-15s\n", "Id ", "Name", "Business Reg. No.", "City", "Address", "Email", "Average Rating");
            HashSet<Long> searchSet = new HashSet<>();
            serviceProviders.forEach(s -> {
                try {
                    Double rating = getRatingForService(s.getServiceProviderId());
                    System.out.printf("%-5s%-20s%-20s%-15s%-35s%-25s%-15s\n",
                            s.getServiceProviderId(),
                            s.getName(),
                            s.getBizRegNum(),
                            s.getCity(),
                            s.getBizAddress(),
                            s.getEmail(),
                            rating == UNRATED ? "0 (Unrated)" : String.format("%.2f", rating));
                } catch (ServiceProviderNotFoundException_Exception ex) {
                    System.out.println("Error: Service Provider with Id: " + s.getServiceProviderId() + " does not exist!\n");
                }
                searchSet.add(s.getServiceProviderId());
            });

            if (searchOnly) {
                if (serviceProviders.size() <= 0) {
                    System.out.println("No Service Providers were found by your criteria!\n");
                }
                while (true) {
                    System.out.println();
                    System.out.println("Enter 0 to go back to the previous menu.");
                    System.out.print("Exit> ");
                    if (!scanner.hasNextInt()) {
                        System.out.println("Error: Incorrect input type! Please try again.\n");
                    } else {
                        int responseExit = scanner.nextInt();
                        if (responseExit == 0) {
                            break;
                        } else {
                            System.out.println("Error: Please enter 0 if you wish to go back!\n");
                        }
                    }
                    scanner.nextLine();
                }
                return;
            }

            if (serviceProviders.size() > 0) {
                while (true) {
                    System.out.println();
                    System.out.println("Enter 0 to go back to the previous menu.");
                    System.out.print("Service Provider Id> ");
                    response = scanner.nextLong();
                    if (response == 0) {
                        break;
                    } else if (!searchSet.contains(response)) {
                        System.out.println("Error: Please input the correct Service Provider Id from the list given!\n");
                        break;
                    }
                    scanner.nextLine();
                    System.out.println();

                    System.out.println("Available Appointment slots:");
                    List<XMLGregorianCalendar> slots;
                    try {
                        slots = freeSlotsPerServiceProviderAndDate(response, dateString, loggedEmail, loggedPassword);
                        for (int i = 1; i <= slots.size(); i++) {
                            Date slotDate = slots.get(i - 1).toGregorianCalendar().getTime();
                            System.out.print(new SimpleDateFormat("hh:mm").format(slotDate) + " ");
                            if (i != slots.size()) {
                                System.out.print("| ");
                            }
                        }
                        System.out.println();
                        System.out.println("Enter 0 to go back to previous menu.");
                        System.out.print("Enter Time (HH:MM)> ");
                        String timeString = scanner.nextLine().trim();
                        if ((timeString.length() != 1 || timeString.charAt(0) != '0') && checkTimeString(timeString)) {
                            Integer endTimeHour = Integer.parseInt(timeString.substring(0, 2));
                            endTimeHour = endTimeHour <= 12 ? endTimeHour + 1 : 01;
                            String endTimeHourString = (endTimeHour + "").length() < 2 ? "0" + endTimeHour : endTimeHour + "";
                            String endTimeString = endTimeHourString + timeString.substring(2, 5);
                            String startTwentyFour = twentyFourHourDateStringConverter(timeString);
                            String endTwentyFour = twentyFourHourDateStringConverter(endTimeString);
                            try {
                                int chosenIdx = matchServiceProviderId(serviceProviders, response);
                                if (chosenIdx > DID_NOT_MATCH) {
                                    createAppointment(customerEntity.getCustomerId(), response,
                                            new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateString + " " + startTwentyFour),
                                            new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateString + " " + endTwentyFour),
                                            slots, serviceProviders.get(chosenIdx));
                                    while (true) {
                                        System.out.println("Enter 0 to go back to the previous menu.");
                                        System.out.print("Exit> ");
                                        int responseExit = scanner.nextInt();
                                        scanner.nextLine();
                                        if (responseExit == 0) {
                                            break;
                                        }
                                    }
                                } else {
                                    System.out.println("Error: Incorrect Service Provider index!\n");
                                }
                                break;
                            } catch (ParseException ex) {
                                System.out.println("Error: Date parsing error while creating appointment!\n");
                            }
                        } else if (timeString.length() == 1 && timeString.charAt(0) == '0') {
                        } else {
                            System.out.println("Error: Please enter a valid time!\n");
                        }
                    } catch (ServiceProviderNotFoundException_Exception | DateProcessingException_Exception ex) {
                        System.out.println("Error: Service Provider ID is incorrect! Please try again.\n");
                    } catch (InvalidLoginException_Exception ex) {
                       System.out.println("Error: " + ex.getMessage());
                    }
                }
            } else {
                System.out.println("No Service Providers were found by your criteria!\n");
            }
        } catch (InputMismatchException ex) {
            System.out.println("Error: Incorrect input type! Please try again.\n");
        }
    }

    // suited to the appointment limits of 8:30am -> 5:30pm
    private String twentyFourHourDateStringConverter(String twelveHour) {
        String hours = twelveHour.substring(0, 2);
        String minutes = twelveHour.substring(3, 5);
        int hoursInt = Integer.parseInt(hours);
        if (hoursInt >= FIRST_APT_HOUR) {
            return hoursInt + ":" + minutes;
        } else {
            hoursInt = hoursInt + 12;
            return hoursInt + ":" + minutes;
        }
    }

    private void createAppointment(Long customerId, Long serviceProviderId, Date start, Date end, List<XMLGregorianCalendar> slots, ServiceProviderEntity serviceProvider) {
        boolean valid = false;
        for (int i = 1; i <= slots.size(); i++) {
            Date slotDate = slots.get(i - 1).toGregorianCalendar().getTime();
            if (slotDate.equals(start)) {
                valid = true;
            }
        }

        if (valid) {
            try {
                Long appointmentId = createAppointmentEntity(serviceProviderId, toXMLGregorianCalendar(start), toXMLGregorianCalendar(end), loggedEmail, loggedPassword);
                System.out.println("The appointment with " + serviceProvider.getName() + " at " + new SimpleDateFormat("hh:mm").format(start) + " on " + new SimpleDateFormat("yyyy-MM-dd").format(start) + " is confirmed.\n");
            } catch (InvalidLoginException_Exception ex) {
                System.out.println("Error creating appointment: Customer with Id: " + customerId + " is not found!\n");
            } catch (EntityAttributeNullException_Exception ex) {
                System.out.println("Error creating appointment: Some values were null!\n");
            } catch (ServiceProviderNotFoundException_Exception ex) {
                System.out.println("Error creating appointment: Service Provider with Id: " + serviceProviderId + " is not found!\n");
            } catch (AppointmentAlreadyExistsException_Exception ex) {
                 System.out.println("Error creating appointment: You already have an appointment at this time!\n");
            }
        } else {
            System.out.println("Error: Your slot chosen is invalid! Please try again.\n");
        }
    }

    public int matchServiceProviderId(List<ServiceProviderEntity> serviceProviders, Long serviceProviderId) {
        for (int i = 0; i < serviceProviders.size(); i++) {
            if (Objects.equals(serviceProviders.get(i).getServiceProviderId(), serviceProviderId)) {
                return i;
            }
        }
        return DID_NOT_MATCH;
    }

    public void viewOrCancelAppointments(boolean toCancel) {
        Scanner scanner = new Scanner(System.in);
        try {
            List<AppointmentEntity> appointments = retrieveAppointmentsByCustomerId(customerEntity.getCustomerId(), loggedEmail, loggedPassword);
            if (toCancel) {
                System.out.println("*** Customer Terminal :: Cancel Appointment ***\n");
            } else {
                System.out.println("*** Customer Terminal :: View All Appointments ***\n");
            }
            System.out.printf("%-20s%-30s%-15s%-10s%-25s%-20s\n", "Appointment Id", "Service Provider", "Date", "Time", "Appointment No.", "Status");
            for (AppointmentEntity a : appointments) {
                ServiceProviderEntity serviceProvider = getServiceProviderFromAppointmentId(a.getAppointmentId());
                System.out.printf("%-20s%-30s%-15s%-10s%-25s%-20s\n",
                        a.getAppointmentId(),
                        serviceProvider.getName(),
                        new SimpleDateFormat("yyyy-MM-dd").format(a.getStartTimestamp().toGregorianCalendar().getTime()),
                        new SimpleDateFormat("hh:mm").format(a.getStartTimestamp().toGregorianCalendar().getTime()),
                        a.getAppointmentNum(),
                        a.isCancelled() ? "Cancelled"
                        : a.getStartTimestamp().toGregorianCalendar().getTime().after(new Date(System.currentTimeMillis())) ? "Active" : "Already taken");
            }

            if (!toCancel) {
                while (true) {
                    System.out.println();
                    System.out.println("Enter 0 to go back to the previous menu.");
                    System.out.print("Exit> ");
                    if (!scanner.hasNextInt()) {
                        System.out.println("Error: Incorrect input type! Please try again.\n");
                    } else {
                        int responseExit = scanner.nextInt();
                        if (responseExit == 0) {
                            break;
                        } else {
                            System.out.println("Error: Please enter 0 if you wish to go back!\n");
                        }
                    }
                    scanner.nextLine();
                }
                return;
            }

            while (true) {
                System.out.println();
                System.out.println("Enter 0 to go back to previous menu.");
                System.out.print("Enter Appointment Id to cancel> ");
                try {
                    Long response = scanner.nextLong();
                    if (response == 0) {
                        break;
                    } else {
                        int chosenIdx = matchAppointmentId(appointments, response);
                        if (chosenIdx > DID_NOT_MATCH) {
                            try {
                                cancelAppointment(appointments.get(chosenIdx).getAppointmentId(), loggedEmail, loggedPassword);
                                System.out.println("You have successfully cancelled Appointment Id: " + appointments.get(chosenIdx).getAppointmentId());
                            } catch (AppointmentCancellationException_Exception ex) {
                                System.out.println(ex.getMessage());
                            } catch (InvalidLoginException_Exception ex) {
                                System.out.println("Error: "+ ex.getMessage());
                            }
                        } else {
                            System.out.println("Error: Please enter a valid Appointment Id from the list above!\n");
                        }
                    }
                } catch (InputMismatchException ex) {
                    System.out.println("Error: Incorrect input type! Please try again.\n");
                }
                scanner.nextLine();
            }

        } catch (CustomerNotFoundException_Exception | AppointmentNotFoundException_Exception ex) {
            System.out.println(ex.getMessage());
        } catch (InvalidLoginException_Exception ex) {
         System.out.println("Error: "+ ex.getMessage());
        }
    }

    public int matchAppointmentId(List<AppointmentEntity> appointments, Long appointmentId) {
        for (int i = 0; i < appointments.size(); i++) {
            if (Objects.equals(appointments.get(i).getAppointmentId(), appointmentId)) {
                return i;
            }
        }
        return DID_NOT_MATCH;
    }

    public void rateProvider() {
        Scanner scanner = new Scanner(System.in);
        Long serviceProviderId;
        Integer rating;

        while (true) {
            System.out.println("*** Customer Terminal :: Rate Service Provider ***\n");
            try {
                System.out.print("Enter Service Provider Id> ");
                serviceProviderId = scanner.nextLong();
                scanner.nextLine();
                System.out.print("Enter your rating (1 - 5)> ");
                rating = scanner.nextInt();
                scanner.nextLine();
                if (rating > 0 && rating < 6) {
                    break;
                } else {
                    System.out.println("Error: Incorrect rating value! Please try again.\n");
                }
            } catch (InputMismatchException ex) {
                System.out.println("Error: Incorrect input type! Please try again.\n");
            }
            scanner.nextLine();
        }

        try {
            rateServiceProvider(serviceProviderId, rating, loggedEmail, loggedPassword);
            System.out.println("You have successfully rated Service Provider with Id: " + serviceProviderId + " with a rating of " + rating + " out of 5!\n");
        } catch (ServiceProviderNotFoundException_Exception | EntityAttributeNullException_Exception
                | RatingWithoutAppointmentException_Exception | CustomerNotFoundException_Exception | ServiceProviderAlreadyRatedException_Exception ex) {
            System.out.println(ex.getMessage());
        } catch (InvalidLoginException_Exception ex) {
           System.out.println("Error: "+ ex.getMessage());
        }
    }

    // check for 12 hour clock
    private boolean checkTimeString(String time) {
        int hour = Integer.parseInt(time.substring(0,2));
        int minute = Integer.parseInt(time.substring(3,5));
        if (time.length() != 5) {
            return false;
        } else if (time.charAt(2) != ':') {
            return false;
        } else if (hour > 12 || hour < 0) {
            return false;
        } else if (minute > 60 || minute < 0) {
            return false;
        }
        return true;
    }

    private boolean checkEmailIsValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
        if (email.isEmpty()) {
            return false;
        } else {
            Pattern pattern = Pattern.compile(emailRegex);
            return pattern.matches(emailRegex, email);
        }
    }

    private XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
        GregorianCalendar gCalendar = new GregorianCalendar();
        gCalendar.setTime(date);
        XMLGregorianCalendar xmlDate = null;
        try {
            xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
        } catch (DatatypeConfigurationException ex) {
            System.out.println("Error: Invalid date for web service conversion!\n");
        }
        return xmlDate;
    }

    private static void cancelAppointment(java.lang.Long arg0, java.lang.String arg1, java.lang.String arg2) throws AppointmentCancellationException_Exception, InvalidLoginException_Exception {
        ws.client.CustomerEntityWebService_Service service = new ws.client.CustomerEntityWebService_Service();
        ws.client.CustomerEntityWebService port = service.getCustomerEntityWebServicePort();
        port.cancelAppointment(arg0, arg1, arg2);
    }

    private static java.util.List<javax.xml.datatype.XMLGregorianCalendar> freeSlotsPerServiceProviderAndDate(java.lang.Long arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3) throws DateProcessingException_Exception, ServiceProviderNotFoundException_Exception, InvalidLoginException_Exception {
        ws.client.CustomerEntityWebService_Service service = new ws.client.CustomerEntityWebService_Service();
        ws.client.CustomerEntityWebService port = service.getCustomerEntityWebServicePort();
        return port.freeSlotsPerServiceProviderAndDate(arg0, arg1, arg2, arg3);
    }

    private static java.util.List<ws.client.CategoryEntity> getAllCategories() {
        ws.client.CustomerEntityWebService_Service service = new ws.client.CustomerEntityWebService_Service();
        ws.client.CustomerEntityWebService port = service.getCustomerEntityWebServicePort();
        return port.getAllCategories();
    }

    private static Double getRatingForService(java.lang.Long arg0) throws ServiceProviderNotFoundException_Exception {
        ws.client.CustomerEntityWebService_Service service = new ws.client.CustomerEntityWebService_Service();
        ws.client.CustomerEntityWebService port = service.getCustomerEntityWebServicePort();
        return port.getRatingForService(arg0);
    }

    private static ServiceProviderEntity getServiceProviderFromAppointmentId(java.lang.Long arg0) throws AppointmentNotFoundException_Exception {
        ws.client.CustomerEntityWebService_Service service = new ws.client.CustomerEntityWebService_Service();
        ws.client.CustomerEntityWebService port = service.getCustomerEntityWebServicePort();
        return port.getServiceProviderFromAppointmentId(arg0);
    }

    private static ServiceProviderEntity getServiceProviderFromAppointmentId_1(java.lang.Long arg0) throws AppointmentNotFoundException_Exception {
        ws.client.CustomerEntityWebService_Service service = new ws.client.CustomerEntityWebService_Service();
        ws.client.CustomerEntityWebService port = service.getCustomerEntityWebServicePort();
        return port.getServiceProviderFromAppointmentId(arg0);
    }

    private static CustomerEntity login(java.lang.String arg0, java.lang.String arg1) throws InvalidLoginException_Exception {
        ws.client.CustomerEntityWebService_Service service = new ws.client.CustomerEntityWebService_Service();
        ws.client.CustomerEntityWebService port = service.getCustomerEntityWebServicePort();
        return port.login(arg0, arg1);
    }

    private static void rateServiceProvider(java.lang.Long arg0, java.lang.Integer arg1, java.lang.String arg2, java.lang.String arg3) throws RatingWithoutAppointmentException_Exception, InvalidLoginException_Exception, ServiceProviderNotFoundException_Exception, EntityAttributeNullException_Exception, ServiceProviderAlreadyRatedException_Exception, CustomerNotFoundException_Exception {
        ws.client.CustomerEntityWebService_Service service = new ws.client.CustomerEntityWebService_Service();
        ws.client.CustomerEntityWebService port = service.getCustomerEntityWebServicePort();
        port.rateServiceProvider(arg0, arg1, arg2, arg3);
    }

    private static java.util.List<ws.client.AppointmentEntity> retrieveAppointmentsByCustomerId(java.lang.Long arg0, java.lang.String arg1, java.lang.String arg2) throws CustomerNotFoundException_Exception, InvalidLoginException_Exception {
        ws.client.CustomerEntityWebService_Service service = new ws.client.CustomerEntityWebService_Service();
        ws.client.CustomerEntityWebService port = service.getCustomerEntityWebServicePort();
        return port.retrieveAppointmentsByCustomerId(arg0, arg1, arg2);
    }

    private static java.util.List<ws.client.ServiceProviderEntity> searchApprovedServiceProviders() {
        ws.client.CustomerEntityWebService_Service service = new ws.client.CustomerEntityWebService_Service();
        ws.client.CustomerEntityWebService port = service.getCustomerEntityWebServicePort();
        return port.searchApprovedServiceProviders();
    }

    private static java.util.List<ws.client.ServiceProviderEntity> searchServiceProvidersByCategoryCityDate(java.lang.Long arg0, java.lang.String arg1, javax.xml.datatype.XMLGregorianCalendar arg2, java.lang.String arg3, java.lang.String arg4) throws InvalidLoginException_Exception {
        ws.client.CustomerEntityWebService_Service service = new ws.client.CustomerEntityWebService_Service();
        ws.client.CustomerEntityWebService port = service.getCustomerEntityWebServicePort();
        return port.searchServiceProvidersByCategoryCityDate(arg0, arg1, arg2, arg3, arg4);
    }

    private static Long createAppointmentEntity(java.lang.Long arg0, javax.xml.datatype.XMLGregorianCalendar arg1, javax.xml.datatype.XMLGregorianCalendar arg2, java.lang.String arg3, java.lang.String arg4) throws InvalidLoginException_Exception, ServiceProviderNotFoundException_Exception, EntityAttributeNullException_Exception, AppointmentAlreadyExistsException_Exception {
        ws.client.CustomerEntityWebService_Service service = new ws.client.CustomerEntityWebService_Service();
        ws.client.CustomerEntityWebService port = service.getCustomerEntityWebServicePort();
        return port.createAppointmentEntity(arg0, arg1, arg2, arg3, arg4);
    }

    private static Long createCustomerEntity(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3, java.lang.String arg4, java.lang.Integer arg5, java.lang.String arg6, java.lang.String arg7, java.lang.Long arg8, java.lang.String arg9) throws CustomerAlreadyExistsException_Exception, EntityAttributeNullException_Exception {
        ws.client.CustomerEntityWebService_Service service = new ws.client.CustomerEntityWebService_Service();
        ws.client.CustomerEntityWebService port = service.getCustomerEntityWebServicePort();
        return port.createCustomerEntity(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
    }

}