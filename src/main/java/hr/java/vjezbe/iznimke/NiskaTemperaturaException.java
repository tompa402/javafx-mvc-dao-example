package hr.java.vjezbe.iznimke;

public class NiskaTemperaturaException extends RuntimeException {

    public NiskaTemperaturaException() {
        super ("Dogodila se pogreška u radu programa!");
    }

    public NiskaTemperaturaException(String message) {
        super(message);
    }

    public NiskaTemperaturaException(String message, Throwable cause) {
        super(message, cause);
    }

    public NiskaTemperaturaException(Throwable cause) {
        super(cause);
    }
}
