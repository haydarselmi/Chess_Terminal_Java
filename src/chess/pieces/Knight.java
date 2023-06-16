package chess.pieces;

import chess.Chessboard;
import chess.util.*;

/**
 * Classe représentant le cavalier.
 */
public class Knight extends Piece {
    /* Méthodes d'instance */

    /**
     * Constructeur.
     * @param board - échiquier auquel la pièce appartient
     * @param position - position initiale de la pièce
     * @param color - couleur de la pièce
     */
    public Knight(Chessboard board, Position position, Color color) {
        super(board, position, color, "knight", color == Color.BLACK ? Symbol.BLACK_KNIGHT : Symbol.WHITE_KNIGHT);
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

        // Verification du mouvement du cavalier
        if(start.getManhattanDistance(destination) == 3) {
            if(!start.isOnSameLineAs(destination) && !start.isOnSameColumnAs(destination)
                    && !start.isOnSameDiagonalAs(destination)) {
                if(destinationPiece != null) {
                    return destinationPiece.getColor() != this.getColor();
                } else {
                    return true;
                }
            }
        }

        return false;
    }
}