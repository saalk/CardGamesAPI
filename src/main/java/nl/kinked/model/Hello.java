package nl.kinked.model;

/**
 * Simple model class.
 * @author klaas
 */
public class Hello {
    private final String message;

    public Hello(final String world) {
        this.message = world;
    }

    public String getMessage() {
        return this.message;
    }
}
