package util.exception;

public class ServiceProviderNotFoundException extends Exception {
    
    public ServiceProviderNotFoundException() {
    }
    
    public ServiceProviderNotFoundException(String msg) {
        super(msg);
    }
    
}