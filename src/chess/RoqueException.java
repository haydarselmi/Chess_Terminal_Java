package chess;

/**
 * Lancée lorsqu'un déplacement Roque est impossible.
 */
public class RoqueException extends Exception {
    // Constructor
    public RoqueException(java.lang.String message) {
        super(message);
    }
}
