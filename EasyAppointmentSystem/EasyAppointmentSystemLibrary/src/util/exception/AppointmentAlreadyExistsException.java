package util.exception;

public class AppointmentAlreadyExistsException extends Exception {

    public AppointmentAlreadyExistsException() {
    }

    public AppointmentAlreadyExistsException(String msg) {
        super(msg);
    }
}
