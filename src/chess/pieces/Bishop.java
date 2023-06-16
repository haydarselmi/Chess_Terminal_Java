package chess.pieces;

import chess.Chessboard;
import chess.util.*;

/**
 * Classe représentant le fou.
 */
public class Bishop extends Piece {
    /* Méthodes d'instance */

    /**
     * Constructeur.
     * @param board - échiquier auquel la pièce appartient
     * @param position - position initiale de la pièce
     * @param color - couleur de la pièce
     */
    public Bishop(Chessboard board, Position position, Color color) {
        super(board, position, color, "bishop", color == Color.BLACK ? Symbol.BLACK_BISHOP : Symbol.WHITE_BISHOP);
    }

    /**
     * teste la validité d'un déplacement
     * @param destination - position de la case de destination du déplacement
     * @return true si le mouvement est possible, false sinon
     */
    public boolean isValidMove(Position destination) {
        // Verification si la destination est bien dans l'échiquier
        if(destination.getX() < 0 || destination.getY() < 0 || destination.getX() >= 8 || destination.getY() >= 8) {
            return false;
        }

        Position start = this.getPosition();
        Piece destinationPiece = this.board.getPiece(destination.getX(), destination.getY());

        // Verification du mouvement en diagonale
        if(start.isOnSameDiagonalAs(destination) && !this.board.isPiecePresentOnSameDiagonalBetween(start, destination)
            && start.getY() != destination.getY() && start.getX() != destination.getX()) {
            if(destinationPiece != null) {
                return destinationPiece.getColor() != this.getColor();
            } else {
                return true;
            }
        }

        return false;
    }
}