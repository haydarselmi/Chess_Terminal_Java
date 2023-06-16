package chess.util;

/**
 * Lancée lorsqu'un déplacement est invalide.
 */
public class ChessMoveException extends Exception {
    // Constructor
    public ChessMoveException(java.lang.String message, Position startingPosition, Position destination) {
        super(message + " " + startingPosition.toAlgebraicNotation() + " " + destination.toAlgebraicNotation());
    }
}
