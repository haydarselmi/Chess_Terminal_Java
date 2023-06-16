package chess.pieces;

import chess.Chessboard;
import chess.Game;
import chess.util.ChessMoveException;
import chess.util.Color;
import chess.util.Position;

/**
 * Classe de base représentant une pièce du jeu d'échecs
 */
public abstract class Piece {
    /* Attributs d'instance */
    /**
     * Position de la pièce sur l'échiquier
     */
    private Position position;

    /**
     * Symbole de la pièce
     */
    private final char symbol;

    /**
     * Couleur de la pièce (Color.WHITE ou Color.BLACK)
     */
    private final Color color;

    /**
     * Nom de la pièce (Roi, Reine, ...)
     */
    private final String name;

    /**
     * Échiquier auquel la pièce appartient
     */
    protected Chessboard board;

    /* Méthodes d'instance */

    // Constructor
    /**
     * Constructeur
     * @param chessboard échiquier auquel la pièce appartient
     * @param position position initiale de la pièce
     * @param color couleur de la pièce
     * @param name nom de la pièce
     * @param symbol symbole de la pièce
     */
    Piece(Chessboard chessboard, Position position, Color color, java.lang.String name, char symbol) {
        this.board = chessboard;
        this.position = position;
        this.color = color;
        this.name = name;
        this.symbol = symbol;
    }

    // Getters
    /**
     * Retourne la position de la pièce sur l'échiquier.
     * @return la position de la pièce sur l'échiquier
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * Retourne le symbole de la pièce
     * @return le symbole de la pièce
     */
    public char getSymbol() {
        return this.symbol;
    }

    /**
     * Retourne la couleur de la pièce
     * @return la couleur de la pièce
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Retourne le nom de la pièce.
     * @return le nom de la pièce
     */
    public String getName() {
        return this.name;
    }

    // Methods
    /**
     * teste la couleur de la pièce.
     * @return true si la pièce est noire, false sinon
     */
    public boolean isBlack() {
        return this.color == Color.BLACK;
    }

    /**
     * teste la couleur de la pièce.
     * @return true si la pièce est blanche, false sinon
     */
    public boolean isWhite() {
        return this.color == Color.WHITE;
    }

    /**
     * déplace la pièce sur la case indiquée.
     * @param destination - position de la case de destination du déplacement.
     * @throws ChessMoveException - si le mouvement n'est pas possible
     */
    public void moveTo(Position destination, Game game) throws ChessMoveException {
        if(!this.isValidMove(destination)) {
            throw new ChessMoveException("Il n'y a pas de mouvements ou le mouvement n'est pas possible" +
                    " pour la nature de la piece", this.position, destination);
        }

        // Verification Échec et mat avant le mouvement de la pièce
        Position playerInitialPos = this.getPosition();
        Piece playerPiece = this.board.getPiece(this.getPosition());
        Piece destinationPiece = this.board.getPiece(destination);

        this.board.setPiece(this.position, null);
        this.position = destination;
        this.board.setPiece(this.position, this);

        if(game.isCheck(playerPiece.getColor())) {
            this.getPosition().setX(playerInitialPos.getX());
            this.getPosition().setY(playerInitialPos.getY());
            this.board.setPiece(playerInitialPos, this);
            this.board.setPiece(destination, destinationPiece);
            throw new ChessMoveException("Mouvement Impossible car ce mouvement laisse votre roi en échec" +
                    " et donc en échec et mat dans le prochain tour du joueur adverse", this.position, destination);
        }
    }

    /**
     * teste la validité d'un déplacement
     * @param destination - position de la case de destination du déplacement
     * @return true si le mouvement est possible, false sinon
     */
    public abstract boolean isValidMove(Position destination);
}
