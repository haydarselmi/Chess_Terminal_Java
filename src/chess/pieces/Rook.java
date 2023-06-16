package chess.pieces;

import chess.Chessboard;
import chess.Game;
import chess.util.*;

/**
 * Classe représentant la tour.
 */
public class Rook extends Piece {
    /* Attributs d'instance */
    private boolean notMovedYet;

    /* Méthodes d'instance */

    /**
     * Constructeur.
     * @param board - échiquier auquel la pièce appartient
     * @param position - position initiale de la pièce
     * @param color - couleur de la pièce
     */
    public Rook(Chessboard board, Position position, Color color) {
        super(board, position, color, "rook", color == Color.BLACK ? Symbol.BLACK_ROOK : Symbol.WHITE_ROOK);
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
        Position start = this.getPosition();
        Piece destinationPiece = this.board.getPiece(destination.getX(), destination.getY());

        // Verification du mouvement en colonne
        if(start.isOnSameColumnAs(destination) && !this.board.isPiecePresentOnSameColumnBetween(start, destination)
            && start.getY() != destination.getY()) {
            if(destinationPiece != null) {
                if(destinationPiece.getColor() != this.getColor()) {
                    return true;
                }
            } else {
                return true;
            }
        }

        // Verification du mouvement en ligne
        if(start.isOnSameLineAs(destination) && !this.board.isPiecePresentOnSameLineBetween(start, destination)
            && start.getX() != destination.getX()) {
            if(destinationPiece != null) {
                if(destinationPiece.getColor() != this.getColor()) {
                    return true;
                }
            } else {
                return true;
            }
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
            throw new ChessMoveException("Il n'y a pas de mouvements ou le mouvement n'est pas possible pour la nature de la piece",
                    this.getPosition(), destination);
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

    // Getters
    /**
     * Accesseur sur notMovedYet
     * @return true si la Tour n'a pas encore bougé, false sinon
     */
    public boolean isNotMovedYet() {
        return this.notMovedYet;
    }
}