package chess.pieces;

import chess.Chessboard;
import chess.Game;
import chess.util.ChessMoveException;
import chess.util.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RookTest {

    @Test
    void isValidMove() throws ChessMoveException {
        Game game = new Game("test", "test");
        Chessboard board = game.getBoard();
        assertFalse(board.getPiece(new Position("A1")).isValidMove(new Position("A3")));

        board.getPiece(new Position("A2")).moveTo(new Position("A4"), game);
        board.getPiece(new Position("A4")).moveTo(new Position("A5"), game);
        board.getPiece(new Position("A5")).moveTo(new Position("A6"), game);
        board.getPiece(new Position("A6")).moveTo(new Position("B7"), game);
        assertNull(board.getPiece(new Position("A6")));

        // Test colonne
        assertTrue(board.getPiece(new Position("A1")).isValidMove(new Position("A2")));
        assertTrue(board.getPiece(new Position("A1")).isValidMove(new Position("A7")));
        assertFalse(board.getPiece(new Position("A1")).isValidMove(new Position("A8")));
        board.getPiece(new Position("A1")).moveTo(new Position("A7"), game);

        // Test Ligne
        assertFalse(board.getPiece(new Position("A7")).isValidMove(new Position("H7")));
        assertFalse(board.getPiece(new Position("A7")).isValidMove(new Position("B7")));
        assertFalse(board.getPiece(new Position("A7")).isValidMove(new Position("A7")));

        board.getPiece(new Position("A7")).moveTo(new Position("A6"), game);

        board.getPiece(new Position("A6")).moveTo(new Position("C6"), game);
        board.getPiece(new Position("C6")).moveTo(new Position("C7"), game);

        assertTrue(board.getPiece(new Position("C7")).isValidMove(new Position("D7")));
        assertFalse(board.getPiece(new Position("C7")).isValidMove(new Position("E7")));
        assertFalse(board.getPiece(new Position("C7")).isValidMove(new Position("D6")));

        // Test Throw Exception mouvement en diagonale
        assertThrows(ChessMoveException.class, () -> {
            board.getPiece(new Position("C7")).moveTo(new Position("D6"), game);
        });
    }
}