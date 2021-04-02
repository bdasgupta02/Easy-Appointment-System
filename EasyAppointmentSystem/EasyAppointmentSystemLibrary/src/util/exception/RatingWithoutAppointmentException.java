
package util.exception;


public class RatingWithoutAppointmentException extends Exception {


    public RatingWithoutAppointmentException() {
    }
    
    
    public RatingWithoutAppointmentException(String msg) {
        super(msg);
    }
}
