package chess.pieces;

import chess.Chessboard;
import chess.util.*;

/**
 * Classe représentant la reine.
 */
public class Queen extends Piece {
    /* Méthodes d'instance */

    /**
     * Constructeur.
     * @param board - échiquier auquel la pièce appartient
     * @param position - position initiale de la pièce
     * @param color - couleur de la pièce
     */
    public Queen(Chessboard board, Position position, Color color) {
        super(board, position, color, "queen", color == Color.BLACK ? Symbol.BLACK_QUEEN : Symbol.WHITE_QUEEN);
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
        // Verification du mouvement de la reine
        if(start.getManhattanDistance(destination) >= 1) {
            // Verification du mouvement de la raine dans son carre de voisinage direct
            // V représente la carré de voisinage direct
            //
            //         V V V
            //         V R V
            //         V V V
            //
            if(start.getManhattanDistance(destination) == 1
                    || (start.getManhattanDistance(destination) == 2 && start.isOnSameDiagonalAs(destination))) {
                if(destinationPiece != null) {
                    return destinationPiece.getColor() != this.getColor();
                } else {
                    return true;
                }
            }
            if(start.isOnSameColumnAs(destination)) {
                if(!this.board.isPiecePresentOnSameColumnBetween(start, destination)) {
                    if(destinationPiece != null) {
                        return destinationPiece.getColor() != this.getColor();
                    } else {
                        return true;
                    }
                }
            } else if(start.isOnSameLineAs(destination)) {
                if(!this.board.isPiecePresentOnSameLineBetween(start, destination)) {
                    if(destinationPiece != null) {
                        return destinationPiece.getColor() != this.getColor();
                    } else {
                        return true;
                    }
                }
            } else if(start.isOnSameDiagonalAs(destination)) {
                if(!this.board.isPiecePresentOnSameDiagonalBetween(start, destination)) {
                    if(destinationPiece != null) {
                        return destinationPiece.getColor() != this.getColor();
                    } else {
                        return true;
                    }
                }
            }

        }
    return false;
    }
}
