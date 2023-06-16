package chess.pieces;

import chess.Chessboard;
import chess.util.Color;
import chess.util.Position;
import chess.util.Symbol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {
    private Chessboard board;

    @BeforeEach
    void setUp() {
        this.board = new Chessboard();
    }

    @Test
    void getPosition() {
        Pawn pawn = new Pawn(this.board, new Position("D3"), Color.WHITE);
        board.setPiece(new Position("D3"), pawn);
        assertEquals(new Position("D3"), pawn.getPosition());
        assertNotEquals(new Position("D4"), pawn.getPosition());
    }

    @Test
    void getSymbol() {
        Pawn pawn = new Pawn(this.board, new Position("D3"), Color.WHITE);
        board.setPiece(new Position("D3"), pawn);
        assertEquals(Symbol.WHITE_PAWN, pawn.getSymbol());
        assertNotEquals(Symbol.BLACK_PAWN, pawn.getSymbol());
    }

    @Test
    void getColor() {
        Pawn pawn = new Pawn(this.board, new Position("D3"), Color.WHITE);
        board.setPiece(new Position("D3"), pawn);
        assertEquals(Color.WHITE, pawn.getColor());
        assertNotEquals(Color.BLACK, pawn.getColor());
    }

    @Test
    void getName() {
        Pawn pawn = new Pawn(this.board, new Position("D3"), Color.WHITE);
        board.setPiece(new Position("D3"), pawn);
        assertEquals("pawn", pawn.getName());
        assertNotEquals("rook", pawn.getName());
    }

    @Test
    void isWhite() {
        Pawn pawn = new Pawn(this.board, new Position("D3"), Color.WHITE);
        board.setPiece(new Position("D3"), pawn);
        assertTrue(pawn.isWhite());
        assertFalse(pawn.isBlack());
    }

    @Test
    void isBlack() {
        Pawn pawn = new Pawn(this.board, new Position("D3"), Color.BLACK);
        board.setPiece(new Position("D3"), pawn);
        assertFalse(pawn.isWhite());
        assertTrue(pawn.isBlack());
    }
}