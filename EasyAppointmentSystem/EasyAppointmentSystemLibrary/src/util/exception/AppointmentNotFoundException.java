package util.exception;

public class AppointmentNotFoundException extends Exception {
    
    public AppointmentNotFoundException() {
    }
    
    public AppointmentNotFoundException(String msg) {
        super(msg);
    }
    
}