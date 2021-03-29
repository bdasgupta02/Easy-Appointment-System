package util.exception;

public class AppointmentCancellationException extends Exception {

    public AppointmentCancellationException() {
    }

    public AppointmentCancellationException(String msg) {
        super(msg);
    }
}
