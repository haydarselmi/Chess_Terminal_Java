package chess.pieces;

import chess.Chessboard;
import chess.Game;
import chess.util.ChessMoveException;
import chess.util.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueenTest {

    @Test
    void isValidMove() throws ChessMoveException {
        Game game = new Game("test", "test");
        Chessboard board = game.getBoard();

        assertFalse(board.getPiece(new Position("D1")).isValidMove(new Position("D2")));
        assertFalse(board.getPiece(new Position("D1")).isValidMove(new Position("C1")));
        assertFalse(board.getPiece(new Position("D1")).isValidMove(new Position("E1")));
        board.setPiece(new Position("D2"), null);
        assertTrue(board.getPiece(new Position("D1")).isValidMove(new Position("D2")));

        assertTrue(board.getPiece(new Position("D1")).isValidMove(new Position("D3")));
        board.getPiece(new Position("D1")).moveTo(new Position("D3"), game);

        assertFalse(board.getPiece(new Position("D3")).isValidMove(new Position("D8")));
        assertTrue(board.getPiece(new Position("D3")).isValidMove(new Position("D7")));
        assertTrue(board.getPiece(new Position("D3")).isValidMove(new Position("H7")));
        board.getPiece(new Position("D3")).moveTo(new Position("H7"), game);
        assertTrue(board.getPiece(new Position("H7")).isValidMove(new Position("G8")));

        assertFalse(board.getPiece(new Position("H7")).isValidMove(new Position("D9")));
    }
}