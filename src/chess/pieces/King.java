package chess.pieces;

import chess.Chessboard;
import chess.Game;
import chess.util.*;

/**
 * Classe représentant le roi.
 */
public class King extends Piece {
    /* Attributs d'instance */
    private boolean notMovedYet;

    /* Méthodes d'instance */

    /**
     * Constructeur.
     * @param board - échiquier auquel la pièce appartient
     * @param position - position initiale de la pièce
     * @param color - couleur de la pièce
     */
    public King(Chessboard board, Position position, Color color) {
        super(board, position, color, "king", color == Color.BLACK ? Symbol.BLACK_KING : Symbol.WHITE_KING);
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

        // Verification du mouvement de la reine
        if(start.getManhattanDistance(destination) >= 1) {
            // Verification du mouvement de la raine dans son carre de voisinage direct
            // V représente la carré de voisinage direct
            //
            //         V V V
            //         V R V
            //         V V V
            //
            if (start.getManhattanDistance(destination) == 1
                    || (start.getManhattanDistance(destination) == 2 && start.isOnSameDiagonalAs(destination))) {
                if (destinationPiece != null) {
                    return destinationPiece.getColor() != this.getColor();
                } else {
                    return true;
                }
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
     * @return true si le Roi n'a pas encore bougé, false sinon
     */
    public boolean isNotMovedYet() {
        return this.notMovedYet;
    }
}
