package chess.pieces;

import chess.Game;
import chess.util.Position;
import chess.util.ChessMoveException;
import chess.Chessboard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KnightTest {

    @Test
    void isValidMove() throws ChessMoveException {
        Game game = new Game("test", "test");
        Chessboard board = game.getBoard();
        assertFalse(board.getPiece(new Position("B1")).isValidMove(new Position("B3")));
        assertFalse(board.getPiece(new Position("B1")).isValidMove(new Position("B2")));
        assertTrue(board.getPiece(new Position("B1")).isValidMove(new Position("C3")));
        assertTrue(board.getPiece(new Position("B1")).isValidMove(new Position("A3")));

        board.getPiece(new Position("B1")).moveTo(new Position("C3"), game);
        board.getPiece(new Position("C3")).moveTo(new Position("B5"), game);

        assertFalse(board.getPiece(new Position("B5")).isValidMove(new Position("D5")));
        assertTrue(board.getPiece(new Position("B5")).isValidMove(new Position("C7")));
        assertTrue(board.getPiece(new Position("B5")).isValidMove(new Position("A7")));
        assertTrue(board.getPiece(new Position("B5")).isValidMove(new Position("C3")));
        assertTrue(board.getPiece(new Position("B5")).isValidMove(new Position("A3")));

        assertFalse(board.getPiece(new Position("B5")).isValidMove(new Position("B5")));
        assertFalse(board.getPiece(new Position("B5")).isValidMove(new Position("B6")));
        assertFalse(board.getPiece(new Position("B5")).isValidMove(new Position("C6")));
        assertFalse(board.getPiece(new Position("B5")).isValidMove(new Position("C5")));
        assertFalse(board.getPiece(new Position("B5")).isValidMove(new Position("C4")));
        assertFalse(board.getPiece(new Position("B5")).isValidMove(new Position("B4")));
        assertFalse(board.getPiece(new Position("B5")).isValidMove(new Position("A4")));
        assertFalse(board.getPiece(new Position("B5")).isValidMove(new Position("A5")));
        assertFalse(board.getPiece(new Position("B5")).isValidMove(new Position("A6")));

        // Test throw exception
        assertThrows(ChessMoveException.class, () -> {
            board.getPiece(new Position("B5")).moveTo(new Position("A6"), game);
        });
    }
}