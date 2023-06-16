package chess.pieces;

import chess.Chessboard;
import chess.Game;
import chess.util.ChessMoveException;
import chess.util.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {
    private Game game;
    private Chessboard board;

    @BeforeEach
    public void setUp() {
        game = new Game("test", "test");
        board = game.getBoard();
    }

   @Test
    void isValidMove() throws ChessMoveException {
        assertTrue(this.board.getPiece(new Position("A2")).isValidMove(new Position("A4")));
        this.board.getPiece(new Position("A2")).moveTo(new Position("A4"), game);

        assertNull(this.board.getPiece(new Position("A2")));
        assertFalse(this.board.getPiece(new Position("A4")).isValidMove(new Position("A6")));
        assertTrue(this.board.getPiece(new Position("A4")).isValidMove(new Position("A5")));

        this.board.getPiece(new Position("A4")).moveTo(new Position("A5"), game);
        this.board.getPiece(new Position("A5")).moveTo(new Position("A6"), game);
        assertTrue(this.board.getPiece(new Position("A6")).isValidMove(new Position("B7")));

        // Test exception
        assertThrows(ChessMoveException.class, () -> {
            this.board.getPiece(new Position("A6")).moveTo(new Position("A7"), game);
        });
    }

    @Test
    void moveTo() throws ChessMoveException {
        this.board.getPiece(new Position("B2")).moveTo(new Position("B4"), game);
        assertNull(this.board.getPiece(new Position("B2")));
        this.board.getPiece(new Position("B4")).moveTo(new Position("B5"), game);
        assertNotNull(this.board.getPiece(new Position("B5")));
        assertNull(this.board.getPiece(new Position("B4")));

        // Test exception
        assertThrows(ChessMoveException.class, () -> {
            this.board.getPiece(new Position("B5")).moveTo(new Position("B7"), game);
        });

        // Test exception hors échiquier
        assertThrows(ChessMoveException.class, () -> {
            this.board.getPiece(new Position("B5")).moveTo(new Position("B10"), game);
        });
    }
}