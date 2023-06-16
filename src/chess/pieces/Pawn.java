package chess.pieces;

import chess.Chessboard;
import chess.Game;
import chess.util.*;

/**
 * Classe représentant le pion.
 */
public class Pawn extends Piece {
    /* Attributs d'instance */
    /**
     * vrai si le pion n'a pas encore été déplacé. Mis à faux dès le premier déplacement.
     */
    private boolean notMovedYet;

    /* Méthodes d'instance */

    /**
     * Constructeur.
     * @param board - échiquier auquel la pièce appartient
     * @param position - position initiale de la pièce
     * @param color - couleur de la pièce
     */
    public Pawn(Chessboard board, Position position, Color color) {
        super(board, position, color, "pawn", color == Color.BLACK ? Symbol.BLACK_PAWN : Symbol.WHITE_PAWN);
        this.notMovedYet = true;
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
        boolean valid = false;
        Position start = this.getPosition();
        Piece destinationPiece = this.board.getPiece(destination);

        // Verification du sens du mouvement selon la couleur de la piece ou si la piece est vraiment bouge
        if(this.isBlack() && (start.getY() - destination.getY()) <= 0) {
            return false;
        } else if(this.isWhite() && (start.getY() - destination.getY()) >= 0) {
            return false;
        }

        // Verification pour le mouvement en avant
        if(start.isOnSameColumnAs(destination)) {
            if(start.getManhattanDistance(destination) == 1) {
                    return destinationPiece == null;
            } else return start.getManhattanDistance(destination) == 2 && this.notMovedYet &&
                    destinationPiece == null;
        } else if(start.isOnSameDiagonalAs(destination)) {
            // Verification pour les mouvements d'attaque en diagonale
            return start.getManhattanDistance(destination) == 2 && destinationPiece != null &&
                    (destinationPiece.getColor() != this.getColor());
        }

        return false;
    }

    /**
     * déplace la pièce sur la case indiquée.
     * @param destination - position de la case de destination du déplacement.
     * @throws ChessMoveException - si le mouvement n'est pas possible
     */
    @Override
    public void moveTo(Position destination, Game game) throws ChessMoveException {
        if(!this.isValidMove(destination)) {
            throw new ChessMoveException("Il n'y a pas de mouvements ou  le mouvement est impossible car soit la destination ne se trouve pas sur l'échiquier" +
                    "soit une piece devant bloque ou soit il n'y a pas de pieces on diagonale a attaquer", this.getPosition(), destination);
        }

        if(this.notMovedYet) {
            this.notMovedYet = false;
        }

        // Verification Échec et mat avant le mouvement de la pièce
        Position playerInitialPos = this.getPosition();
        Piece playerPiece = this.board.getPiece(this.getPosition());
        Piece destinationPiece = this.board.getPiece(destination);

        // Mouvement de la piece
        this.board.setPiece(this.getPosition(), null);
        this.getPosition().setX(destination.getX());
        this.getPosition().setY(destination.getY());
        this.board.setPiece(this.getPosition(), this);

        if(game.isCheck(playerPiece.getColor())) {
            this.getPosition().setX(playerInitialPos.getX());
            this.getPosition().setY(playerInitialPos.getY());
            this.board.setPiece(playerInitialPos, this);
            this.board.setPiece(destination, destinationPiece);
            throw new ChessMoveException("Mouvement Impossible car ce mouvement laisse votre roi en échec" +
                    " et donc en échec et mat dans le prochain tour du joueur adverse", this.getPosition(), destination);
        }
    }
}
