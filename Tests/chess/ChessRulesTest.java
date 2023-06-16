package chess;

import chess.pieces.*;
import chess.util.ChessMoveException;
import chess.util.Color;
import chess.util.Position;
import chess.util.Symbol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChessRulesTest {

    // Prise en Passant Test
    @Test
    void enPassant() throws EnPassantException {
        Game game = new Game("test", "test");
        Chessboard board = game.getBoard();
        ChessRules rules = new ChessRules(game, board);

        assertThrows(EnPassantException.class, () -> rules.enPassant(ChessRules.LEFT_SIDE));
        assertThrows(EnPassantException.class, () -> rules.enPassant(ChessRules.RIGHT_SIDE));

        board.setPiece(new Position("A5"), new Pawn(board, new Position("A5"), Color.WHITE));
        board.setPiece(new Position("B5"), new Pawn(board, new Position("B5"), Color.BLACK));
        rules.setLastStartMove(new Position("B7"));
        rules.setLastDestinationMove(new Position("B5"));
        rules.enPassant(ChessRules.LEFT_SIDE);
        assertNull(board.getPiece(new Position("B5")));
        assertNull(board.getPiece(new Position("A5")));
        assertNotNull(board.getPiece(new Position("B6")));
    }

    @Test
    void isEnPassantPossible() throws ChessMoveException {
        Game game = new Game("test", "test");
        Chessboard board = game.getBoard();
        ChessRules rules = new ChessRules(game, board);

        assertFalse(rules.isEnPassantPossible());

        // Test Prise en Passant depuis un pion gauche
        board.getPiece(new Position("A2")).moveTo(new Position("A4"), game);
        board.getPiece(new Position("A4")).moveTo(new Position("A5"), game);
        board.getPiece(new Position("B7")).moveTo(new Position("B5"), game);
        rules.setLastStartMove(new Position("B7"));
        rules.setLastDestinationMove(new Position("B5"));
        assertTrue(rules.isEnPassantPossible());
        rules.setLastStartMove(new Position("B6"));
        assertFalse(rules.isEnPassantPossible());

        // Test Prise en Passant depuis un pion droit
        board.setPiece(new Position("A5"), null);
        board.setPiece(new Position("C5"), new Pawn(board, new Position("C5"), Color.WHITE));
        rules.setLastStartMove(new Position("B7"));
        assertTrue(rules.isEnPassantPossible());
    }

    @Test
    void checkSidePassant() throws ChessMoveException {
        Game game = new Game("test", "test");
        Chessboard board = game.getBoard();
        ChessRules rules = new ChessRules(game, board);

        assertFalse(rules.checkSidePassant(ChessRules.LEFT_SIDE));
        assertFalse(rules.checkSidePassant(ChessRules.RIGHT_SIDE));

        board.setPiece(new Position("A5"), new Pawn(board, new Position("A5"), Color.WHITE));
        board.getPiece(new Position("B7")).moveTo(new Position("B5"), game);
        rules.setLastStartMove(new Position("B7"));
        rules.setLastDestinationMove(new Position("B5"));
        assertFalse(rules.checkSidePassant(ChessRules.RIGHT_SIDE));
        assertTrue(rules.checkSidePassant(ChessRules.LEFT_SIDE));

        board.setPiece(new Position("A5"), null);

        board.setPiece(new Position("C5"), new Pawn(board, new Position("C5"), Color.WHITE));
        assertTrue(rules.checkSidePassant(ChessRules.RIGHT_SIDE));
        assertFalse(rules.checkSidePassant(ChessRules.LEFT_SIDE));
    }

    // Grand Roque et Petit Roque Tests
    @Test
    void QueenSideRoque() throws RoqueException {
        Game game = new Game("test", "test");
        Chessboard board = game.getBoard();
        ChessRules rules = new ChessRules(game, board);

        assertThrows(RoqueException.class, rules::QueenSideRoque);
        board.setPiece(new Position("D1"), null);
        assertThrows(RoqueException.class, rules::QueenSideRoque);
        board.setPiece(new Position("C1"), null);
        assertThrows(RoqueException.class, rules::QueenSideRoque);
        board.setPiece(new Position("B1"), null);

        assertNull(board.getPiece(new Position("D1")));
        assertNull(board.getPiece(new Position("C1")));
        assertNull(board.getPiece(new Position("B1")));
        rules.QueenSideRoque();

        assertEquals(board.getPiece(new Position("C1")).getSymbol(), Symbol.WHITE_KING);
        assertEquals(board.getPiece(new Position("D1")).getSymbol(), Symbol.WHITE_ROOK);

        assertThrows(RoqueException.class, rules::QueenSideRoque);
    }

    @Test
    void KingSideRoque() throws RoqueException {
        Game game = new Game("test", "test");
        Chessboard board = game.getBoard();
        ChessRules rules = new ChessRules(game, board);

        assertThrows(RoqueException.class, rules::KingSideRoque);
        board.setPiece(new Position("F1"), null);
        assertThrows(RoqueException.class, rules::KingSideRoque);
        board.setPiece(new Position("G1"), null);

        assertNull(board.getPiece(new Position("F1")));
        assertNull(board.getPiece(new Position("G1")));
        rules.KingSideRoque();

        assertEquals(board.getPiece(new Position("G1")).getSymbol(), Symbol.WHITE_KING);
        assertEquals(board.getPiece(new Position("F1")).getSymbol(), Symbol.WHITE_ROOK);

        assertThrows(RoqueException.class, rules::QueenSideRoque);
    }

    @Test
    void canRoqueQueenSide() throws ChessMoveException {
        Game game = new Game("test", "test");
        Chessboard board = game.getBoard();
        ChessRules rules = new ChessRules(game, board);

        board.setPiece(new Position("D1"), null);
        assertFalse(rules.canRoqueQueenSide());

        board.setPiece(new Position("C1"), null);
        assertFalse(rules.canRoqueQueenSide());

        board.setPiece(new Position("B1"), null);
        assertTrue(rules.canRoqueQueenSide());

        board.setPiece(new Position("D2"), new Pawn(board, new Position("D2"), Color.BLACK));
        assertFalse(rules.canRoqueQueenSide());

        board.setPiece(new Position("D2"), null);
        assertTrue(rules.canRoqueQueenSide());

        board.setPiece(new Position("B2"), new Pawn(board, new Position("B2"), Color.BLACK));
        assertFalse(rules.canRoqueQueenSide());

        board.setPiece(new Position("B2"), null);
        assertTrue(rules.canRoqueQueenSide());

        board.getPiece(4, 0).moveTo(new Position(3, 0), game);
        board.getPiece(3, 0).moveTo(new Position(4, 0), game);
        assertTrue(rules.canRoqueQueenSide());
    }

    @Test
    void canRoqueKingSide() throws ChessMoveException {
        Game game = new Game("test", "test");
        Chessboard board = game.getBoard();
        ChessRules rules = new ChessRules(game, board);

        board.setPiece(new Position("F1"), null);
        assertFalse(rules.canRoqueKingSide());

        board.setPiece(new Position("G1"), null);
        assertTrue(rules.canRoqueKingSide());

        board.setPiece(new Position("F2"), new Pawn(board, new Position("F2"), Color.BLACK));
        assertFalse(rules.canRoqueKingSide());

        board.setPiece(new Position("F2"), null);
        assertTrue(rules.canRoqueKingSide());

        board.getPiece(4, 0).moveTo(new Position(5, 0), game);
        board.getPiece(5, 0).moveTo(new Position(4, 0), game);
        assertFalse(rules.canRoqueKingSide());
    }

    // Test Détection de l'échec et mat
    @Test
    void isCheckMate() throws ChessMoveException {
        Game game = new Game("test", "test");
        Chessboard board = game.getBoard();
        ChessRules rules = new ChessRules(game, board);

        assertFalse(game.isCheck(Color.WHITE) || rules.isCheckMate(Color.WHITE));

        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                board.setPiece(new Position(x, y), null);
            }
        }

        // Test Échec et Mat
        board.setPiece(new Position("E8"), new King(board, new Position("E8"), Color.WHITE));
        game.turn(new Position("E8"), new Position("E7"));
        board.setPiece(new Position("H8"), new Rook(board, new Position("H8"), Color.BLACK));
        board.setPiece(new Position("H7"), new Rook(board, new Position("H7"), Color.BLACK));
        assertTrue(rules.isCheckMate(Color.WHITE));
        assertFalse(rules.isCheckMate(Color.BLACK));

        // Test échec
        board.setPiece(new Position("H8"), null);
        assertFalse(rules.isCheckMate(Color.WHITE));

        // Test échec unique Échappatoire en bloquant la piece
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                board.setPiece(new Position(x, y), null);
            }
        }
        board.setPiece(new Position("H8"), new King(board, new Position("H8"), Color.WHITE));
        board.setPiece(new Position("H7"), new Bishop(board, new Position("H7"), Color.WHITE));
        board.setPiece(new Position("G8"), new Bishop(board, new Position("G8"), Color.WHITE));

        board.setPiece(new Position("F6"), new Bishop(board, new Position("F6"), Color.BLACK));
        // Pour actualiser la position du roi
        game.setWhiteKingPosition(new Position("H8"));
        assertFalse(rules.isCheckMate(Color.WHITE));
        assertFalse(rules.isCheckMate(Color.BLACK));

        // Test échec unique Échappatoire en effectuant un petit Roque ou en bougeant le roi
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                board.setPiece(new Position(x, y), null);
            }
        }
        board.setPiece(new Position("E1"), new King(board, new Position("E1"), Color.WHITE));
        board.setPiece(new Position("H1"), new Rook(board, new Position("H1"), Color.WHITE));
        board.setPiece(new Position("D1"), new Pawn(board, new Position("D1"), Color.WHITE));
        board.setPiece(new Position("D2"), new Pawn(board, new Position("D2"), Color.WHITE));
        board.setPiece(new Position("E2"), new Pawn(board, new Position("E2"), Color.WHITE));

        board.setPiece(new Position("G3"), new Bishop(board, new Position("G3"), Color.BLACK));
        // Pour actualiser la position du roi
        game.setWhiteKingPosition(new Position("E1"));
        assertFalse(rules.isCheckMate(Color.WHITE));
        assertFalse(rules.isCheckMate(Color.BLACK));

        // Test échec avec petit roque interdit mais unique Échappatoire en bougeant le roi
        board.setPiece(new Position("G1"), new Pawn(board, new Position("G1"), Color.WHITE));
        assertFalse(rules.isCheckMate(Color.WHITE));

        // Test échec et mat
        board.setPiece(new Position("E2"), null);
        board.setPiece(new Position("F1"), new Pawn(board, new Position("F1"), Color.WHITE));
        board.setPiece(new Position("E3"), new Rook(board, new Position("E3"), Color.BLACK));
        assertTrue(rules.isCheckMate(Color.WHITE));
    }
}