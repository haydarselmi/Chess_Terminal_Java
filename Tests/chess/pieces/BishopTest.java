package chess.pieces;

import chess.Chessboard;
import chess.Game;
import chess.util.Position;
import chess.util.ChessMoveException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BishopTest {
    @Test
    void isValidMove() throws ChessMoveException {
        Game game = new Game("test", "test");
        Chessboard board = game.getBoard();
        assertFalse(board.getPiece(new Position("C1")).isValidMove(new Position("H6")));
        board.getPiece(new Position("D2")).moveTo(new Position("D4"), game);
        assertTrue(board.getPiece(new Position("C1")).isValidMove(new Position("H6")));
        board.getPiece(new Position("C1")).moveTo(new Position("H6"), game);
        assertTrue(board.getPiece(new Position("H6")).isValidMove(new Position("G7")));
        assertFalse(board.getPiece(new Position("H6")).isValidMove(new Position("F8")));

        // Test throw exception
        assertThrows(ChessMoveException.class, () -> {
            board.getPiece(new Position("H6")).moveTo(new Position("F8"), game);
        });
    }
}