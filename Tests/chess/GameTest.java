package chess;

import chess.pieces.King;
import chess.pieces.Piece;
import chess.util.ChessMoveException;
import chess.util.Color;
import chess.util.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void turn() throws ChessMoveException {
        Game game = new Game("test1", "test2");

        assertThrows(ChessMoveException.class, () -> game.turn(new Position("A3"), new Position("A4")));
        assertThrows(ChessMoveException.class, () -> game.turn(new Position("A7"), new Position("A5")));
        assertThrows(ChessMoveException.class, () -> game.turn(new Position("A2"), new Position("A5")));

        game.turn(new Position("A2"), new Position("A4"));
        assertNull(game.getBoard().getPiece(new Position("A2")));
        assertNotNull(game.getBoard().getPiece(new Position("A4")));
    }

    @Test
    void isCheck() {
        Game chessGame = new Game("test1", "test2");
        assertFalse(chessGame.isCheck(Color.WHITE));
        assertFalse(chessGame.isCheck(Color.BLACK));
        Chessboard board = chessGame.getBoard();

        board.setPiece(new Position("E2"), null);
        board.setPiece(new Position("E7"), null);
        Piece kW = new King(board, new Position("E7"), Color.WHITE);
        Piece kB = new King(board, new Position("E2"), Color.BLACK);
        board.setPiece(new Position("E7"), kW);

        board.setPiece(new Position("E2"), kB);

        assertTrue(chessGame.isCheck(Color.WHITE));
        assertTrue(chessGame.isCheck(Color.BLACK));
    }
}