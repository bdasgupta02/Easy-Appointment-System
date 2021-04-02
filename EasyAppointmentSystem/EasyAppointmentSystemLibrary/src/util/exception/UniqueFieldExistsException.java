package util.exception;


public class UniqueFieldExistsException extends Exception {

 
    public UniqueFieldExistsException() {
    }


    public UniqueFieldExistsException(String msg) {
        super(msg);
    }
}
