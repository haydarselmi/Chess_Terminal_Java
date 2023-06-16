package chess;

/**
 * Lancée lorsqu'une prise en passant est impossible.
 */
public class EnPassantException extends Exception {
    // Constructor
    public EnPassantException(java.lang.String message) {
        super(message);
    }
}
