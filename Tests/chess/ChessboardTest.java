package chess;

import chess.pieces.Knight;
import chess.pieces.Rook;
import chess.util.Color;
import chess.util.Position;
import chess.util.Symbol;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.zip.CheckedInputStream;

import static org.junit.jupiter.api.Assertions.*;

class ChessboardTest {
    private Chessboard board;

    @BeforeEach
    public void setUp() {
        board = new Chessboard();
    }

    @Test
    void testGetPiece() {
        // TEST WITH getPiece(int, int)
        // Test Pawn
        assertEquals(Symbol.WHITE_PAWN, this.board.getPiece(0, 1).getSymbol());
        assertEquals(Color.WHITE, this.board.getPiece(0, 1).getColor());
        assertEquals("pawn", this.board.getPiece(0, 1).getName());

        // Test Rook
        assertEquals(Symbol.WHITE_ROOK, this.board.getPiece(0, 0).getSymbol());
        assertEquals(Color.WHITE, this.board.getPiece(0, 0).getColor());
        assertEquals("rook", this.board.getPiece(0, 0).getName());

        // Test Bishop
        assertEquals(Symbol.BLACK_BISHOP, this.board.getPiece(2, 7).getSymbol());
        assertEquals(Color.BLACK, this.board.getPiece(2, 7).getColor());
        assertEquals("bishop", this.board.getPiece(2, 7).getName());

        // Test null Piece
        for(int i = 2; i < 6; i++) {
            for(int j = 0; j < 8; j++) {
                assertNull(this.board.getPiece(j, i));
            }
        }

        // TEST WITH getPiece(Position(int, int))

        // Test null Piece
        for(int i = 2; i < 6; i++) {
            for(int j = 0; j < 8; j++) {
                assertNull(this.board.getPiece(new Position(j, i)));
            }
        }
    }

    @Test
    void testGetAllPiecesWithColor() {
        assertEquals(16, this.board.getAllPiecesWithColor(Color.WHITE).size());
        assertEquals(16, this.board.getAllPiecesWithColor(Color.BLACK).size());

        this.board.setPiece(new Position("A1"), null);
        assertEquals(15, this.board.getAllPiecesWithColor(Color.WHITE).size());
        assertEquals(16, this.board.getAllPiecesWithColor(Color.BLACK).size());

        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                this.board.setPiece(new Position(x, y), null);
            }
        }
        assertEquals(0, this.board.getAllPiecesWithColor(Color.BLACK).size()
                + this.board.getAllPiecesWithColor(Color.WHITE).size());
    }

    @Test
    void testSetPiece() {
        this.board.setPiece(new Position("A1"), new Knight(this.board, new Position("A1"), Color.BLACK));
        assertEquals(Color.BLACK, this.board.getPiece(new Position("A1")).getColor());
        assertEquals(Symbol.BLACK_KNIGHT, this.board.getPiece(new Position("A1")).getSymbol());

        this.board.setPiece(new Position("A1"), new Rook(this.board, new Position("A1"), Color.WHITE));
        assertEquals(Color.WHITE, this.board.getPiece(new Position("A1")).getColor());
        assertEquals(Symbol.WHITE_ROOK, this.board.getPiece(new Position("A1")).getSymbol());
    }

    @Test
    void testIsPiecePresentOnSameDiagonalBetween() {
        assertTrue(this.board.isPiecePresentOnSameDiagonalBetween(new Position("A1"), new Position("H8")));
        assertFalse(this.board.isPiecePresentOnSameDiagonalBetween(new Position("A1"), new Position("B2")));
        assertTrue(this.board.isPiecePresentOnSameDiagonalBetween(new Position("H2"), new Position("B8")));
        assertFalse(this.board.isPiecePresentOnSameDiagonalBetween(new Position("A1"), new Position("A1")));
        // Test Throw
        assertThrows(IllegalArgumentException.class, () -> {
            this.board.isPiecePresentOnSameDiagonalBetween(new Position("A1"), new Position("C4"));
        });
    }

    @Test
    void isPiecePresentOnSameColumnBetween() {
        assertTrue(this.board.isPiecePresentOnSameColumnBetween(new Position("A1"), new Position("A8")));
        assertFalse(this.board.isPiecePresentOnSameColumnBetween(new Position("A2"), new Position("A7")));
        assertFalse(this.board.isPiecePresentOnSameColumnBetween(new Position("A1"), new Position("A2")));
        assertFalse(this.board.isPiecePresentOnSameColumnBetween(new Position("A1"), new Position("A1")));
        // Test Throw
        assertThrows(IllegalArgumentException.class, () -> {
            this.board.isPiecePresentOnSameLineBetween(new Position("A1"), new Position("B7"));
        });
    }

    @Test
    void isPiecePresentOnSameLineBetween() {
        assertTrue(this.board.isPiecePresentOnSameLineBetween(new Position("A1"), new Position("H1")));
        assertFalse(this.board.isPiecePresentOnSameLineBetween(new Position("A1"), new Position("B1")));
        assertFalse(this.board.isPiecePresentOnSameLineBetween(new Position("A1"), new Position("B1")));
        assertFalse(this.board.isPiecePresentOnSameLineBetween(new Position("A1"), new Position("A1")));
        // Test Throw
        assertThrows(IllegalArgumentException.class, () -> {
            this.board.isPiecePresentOnSameLineBetween(new Position("A1"), new Position("A3"));
        });
    }

    @Test
    void testToString() {
        System.out.println(this.board.toString());
    }
}