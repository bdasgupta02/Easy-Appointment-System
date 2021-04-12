package util.exception;

public class CustomerAlreadyExistsException extends Exception {

    public CustomerAlreadyExistsException() {
    }

    public CustomerAlreadyExistsException(String msg) {
        super(msg);
    }
}
