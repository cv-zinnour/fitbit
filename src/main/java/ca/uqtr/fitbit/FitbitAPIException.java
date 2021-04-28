package ca.uqtr.fitbit;

public class FitbitAPIException extends Exception {
    public FitbitAPIException(String errorMessage) {
        super(errorMessage);
    }
}