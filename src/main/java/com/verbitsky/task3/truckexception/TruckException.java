package com.verbitsky.task3.truckexception;

public class TruckException extends Exception {
    public TruckException() {
        super();
    }

    public TruckException(String message) {
        super(message);
    }

    public TruckException(String message, Throwable cause) {
        super(message, cause);
    }

    public TruckException(Throwable cause) {
        super(cause);
    }
}
