package util.exception;

public class ServiceProviderAlreadyRatedException extends Exception {

    public ServiceProviderAlreadyRatedException() {
    }

    public ServiceProviderAlreadyRatedException(String msg) {
        super(msg);
    }
}
