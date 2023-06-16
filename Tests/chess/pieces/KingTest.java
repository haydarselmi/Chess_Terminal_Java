package chess.pieces;

import chess.Chessboard;
import chess.util.Color;
import chess.util.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KingTest {

    @Test
    void isValidMove() {
        Chessboard board = new Chessboard();

        assertFalse(board.getPiece(new Position("E1")).isValidMove(new Position("E2")));
        assertFalse(board.getPiece(new Position("E1")).isValidMove(new Position("D1")));
        assertFalse(board.getPiece(new Position("E1")).isValidMove(new Position("F1")));

        board.setPiece(new Position("E2"), null);
        assertTrue(board.getPiece(new Position("E1")).isValidMove(new Position("E2")));
        board.setPiece(new Position("E2"), new King(board, new Position("E2"), Color.WHITE));
        assertFalse(board.getPiece(new Position("E2")).isValidMove(new Position("E8")));

        board.setPiece(new Position("E6"), new King(board, new Position("E6"), Color.WHITE));
        assertFalse(board.getPiece(new Position("E6")).isValidMove(new Position("E8")));
        assertTrue(board.getPiece(new Position("E6")).isValidMove(new Position("E7")));
        assertTrue(board.getPiece(new Position("E6")).isValidMove(new Position("F7")));
        assertTrue(board.getPiece(new Position("E6")).isValidMove(new Position("D7")));
    }
}