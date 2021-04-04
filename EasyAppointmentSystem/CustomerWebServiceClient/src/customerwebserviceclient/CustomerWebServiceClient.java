package customerwebserviceclient;

import ws.client.AppointmentCancellationException_Exception;
import ws.client.CustomerEntity;
import ws.client.CustomerNotFoundException_Exception;
import ws.client.DateProcessingException_Exception;
import ws.client.EntityAttributeNullException_Exception;
import ws.client.RatingWithoutAppointmentException_Exception;
import ws.client.ServiceProviderNotFoundException_Exception;


public class CustomerWebServiceClient {
    
    public CustomerWebServiceClient() {
    }

    public static void main(String[] args) {
        CustomerWebServiceClient customerWebServiceClient = new CustomerWebServiceClient();
        customerWebServiceClient.run();
    }
    
    public void run() {
        
    }

    private static void cancelAppointment(java.lang.Long arg0) throws AppointmentCancellationException_Exception {
        ws.client.CustomerEntityWebService_Service service = new ws.client.CustomerEntityWebService_Service();
        ws.client.CustomerEntityWebService port = service.getCustomerEntityWebServicePort();
        port.cancelAppointment(arg0);
    }

    private static Long createAppointmentEntity(java.lang.Long arg0, java.lang.Long arg1, javax.xml.datatype.XMLGregorianCalendar arg2, javax.xml.datatype.XMLGregorianCalendar arg3, java.lang.String arg4) throws ServiceProviderNotFoundException_Exception, CustomerNotFoundException_Exception {
        ws.client.CustomerEntityWebService_Service service = new ws.client.CustomerEntityWebService_Service();
        ws.client.CustomerEntityWebService port = service.getCustomerEntityWebServicePort();
        return port.createAppointmentEntity(arg0, arg1, arg2, arg3, arg4);
    }

    private static Long createCustomerEntity(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3, java.lang.Integer arg4, java.lang.Integer arg5, java.lang.String arg6, java.lang.String arg7, java.lang.Long arg8, java.lang.String arg9) throws EntityAttributeNullException_Exception {
        ws.client.CustomerEntityWebService_Service service = new ws.client.CustomerEntityWebService_Service();
        ws.client.CustomerEntityWebService port = service.getCustomerEntityWebServicePort();
        return port.createCustomerEntity(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
    }

    private static java.util.List<javax.xml.datatype.XMLGregorianCalendar> freeSlotsPerServiceProviderAndDate(java.lang.Long arg0, java.lang.String arg1) throws DateProcessingException_Exception, ServiceProviderNotFoundException_Exception {
        ws.client.CustomerEntityWebService_Service service = new ws.client.CustomerEntityWebService_Service();
        ws.client.CustomerEntityWebService port = service.getCustomerEntityWebServicePort();
        return port.freeSlotsPerServiceProviderAndDate(arg0, arg1);
    }

    private static CustomerEntity login(java.lang.String arg0, java.lang.String arg1) throws CustomerNotFoundException_Exception {
        ws.client.CustomerEntityWebService_Service service = new ws.client.CustomerEntityWebService_Service();
        ws.client.CustomerEntityWebService port = service.getCustomerEntityWebServicePort();
        return port.login(arg0, arg1);
    }

    private static void rateServiceProvider(java.lang.Long arg0, java.lang.Long arg1, java.lang.Integer arg2) throws CustomerNotFoundException_Exception, EntityAttributeNullException_Exception, ServiceProviderNotFoundException_Exception, RatingWithoutAppointmentException_Exception {
        ws.client.CustomerEntityWebService_Service service = new ws.client.CustomerEntityWebService_Service();
        ws.client.CustomerEntityWebService port = service.getCustomerEntityWebServicePort();
        port.rateServiceProvider(arg0, arg1, arg2);
    }

    private static java.util.List<ws.client.AppointmentEntity> retrieveAppointmentsByCustomerId(java.lang.Long arg0) throws CustomerNotFoundException_Exception {
        ws.client.CustomerEntityWebService_Service service = new ws.client.CustomerEntityWebService_Service();
        ws.client.CustomerEntityWebService port = service.getCustomerEntityWebServicePort();
        return port.retrieveAppointmentsByCustomerId(arg0);
    }

    private static java.util.List<ws.client.ServiceProviderEntity> searchApprovedServiceProviders() {
        ws.client.CustomerEntityWebService_Service service = new ws.client.CustomerEntityWebService_Service();
        ws.client.CustomerEntityWebService port = service.getCustomerEntityWebServicePort();
        return port.searchApprovedServiceProviders();
    }

    private static java.util.List<ws.client.ServiceProviderEntity> searchServiceProvidersByCategoryCityDate(java.lang.Long arg0, java.lang.String arg1, javax.xml.datatype.XMLGregorianCalendar arg2) {
        ws.client.CustomerEntityWebService_Service service = new ws.client.CustomerEntityWebService_Service();
        ws.client.CustomerEntityWebService port = service.getCustomerEntityWebServicePort();
        return port.searchServiceProvidersByCategoryCityDate(arg0, arg1, arg2);
    }
}
