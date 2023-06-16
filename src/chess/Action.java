package chess;

import chess.pieces.Piece;
import chess.util.Position;

/**
 * Classe permettant de stocker une action d'un joueur
 */
public class Action {
    /* Attributs d'instance */
    /**
     * Contient le numéro du tour
     */
    final private int turn;
    /**
     * Contient le nom du joueur effectuant l'action
     */
    final private String playerName;
    /**
     * Contient l'action choisie par le joueur
     * Peut être Abandon, Grand Roque, Petit Roque ou Déplacement d'une piece
     */
    final private String action;
    /**
     * Contient la position de depart lors du mouvement d'une piece en action
     */
    private Position startPos;
    /**
     * Contient la position d'arrivé lors du mouvement d'une piece en action
     */
    private Position destinationPos;
    /**
     * La pièce jouée lors d'un mouvement
     */
    private Piece playerPiece;

    /* Méthodes d'instance */

    /**
     * Constructeur.
     * @param turn - le numéro du coup joue
     * @param player - le nom du joueur effectuant le coup
     * @param action - le type d'action jouée
     * @param start - le depart lors d'un mouvement
     * @param destination - la destination lors d'un mouvement
     */
    public Action(int turn, String player, String action, String start, String destination) {
        this.turn = turn;
        this.playerName = player;
        switch(action) {
            case "A":
                this.action = "Abandon";
                this.startPos = this.destinationPos = null;
                break;
            case "M":
                this.action = "Mouvement d'une pièce";
                this.startPos = new Position(start);
                this.destinationPos = new Position(destination);
                break;
            case "GR":
                this.action = "Grand Roque";
                this.startPos = this.destinationPos = null;
                break;
            case "PR":
                this.action = "Petit Roque";
                this.startPos = this.destinationPos = null;
                break;
            case "GP":
                this.action = "Gauche Passant";
                this.startPos = new Position(start);
                this.destinationPos = new Position(destination);
                break;
            case "DP":
                this.action = "Droit Passant";
                this.startPos = new Position(start);
                this.destinationPos = new Position(destination);
                break;
            default:
                this.action = "Erreur Action inexistante";
                break;
        }
    }

    /**
     * Permet de convertir l'action en chaine de caractères affichable sur console
     * @return l'action en chaine de caractères
     */
    @Override
    public String toString() {
        String display = "ACTION Numéro " + this.turn + " de " + this.playerName + " ---------------------"
                + "\n" + this.action;
        if(this.action == "Mouvement d'une pièce") {
            display += " de " + this.startPos + " à " + this.destinationPos;
        }
        return display;
    }
}
