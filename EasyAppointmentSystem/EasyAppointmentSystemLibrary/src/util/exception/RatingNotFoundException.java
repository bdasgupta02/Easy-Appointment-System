package util.exception;

public class RatingNotFoundException extends Exception{

    public RatingNotFoundException() {
    }

    public RatingNotFoundException(String msg) {
        super(msg);
    }
}