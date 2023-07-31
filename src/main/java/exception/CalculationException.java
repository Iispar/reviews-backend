package exception;

public class CalculationException extends RuntimeException{
    public CalculationException(String error) {
        super(error);
    }
}