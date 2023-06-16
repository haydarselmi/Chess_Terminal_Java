package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import chess.pieces.Piece;
import chess.util.ChessMoveException;
import chess.util.Color;
import chess.util.Position;
import chess.util.Symbol;

public class Game {
    /* Attributs d'instance */
    final private String whitePlayerName;
    final private String blackPlayerName;
    private Chessboard board;
    private Color currentColor;
    /** attributs permettant de garder la position des rois de chaque joueur */
    private Position whiteKingPosition;
    private Position blackKingPosition;
    /**
     * Content les règles de gestion du Roque et de la prise en passant
     */
    private ChessRules rules;
    /**
     * Nombre contenant le numéro du tour actuel
     */
    private int turnNum;
    /**
     * Liste contenant l'historique des actions.
     */
    private ArrayList<Action> actions;
    /* Méthodes d'instance */

    public static void main(String[] args) {
        System.out.print("Partie d'échecs Joueur blanc Veuillez entrez votre nom : ");
        Scanner sc = new Scanner(System.in);
        String currentAction, player;
        String whitePlayerName = sc.next();
        System.out.print("Partie d'échecs Joueur noir Veuillez entrez votre nom : ");
        String blackPlayerName = sc.next();
        Game game = new Game(whitePlayerName, blackPlayerName);
        String startPos, destinationPos;
        boolean isWhiteKingPresent = false, isBlackKingPresent = false, forfeit = false;
        ArrayList<String> availableAction = new ArrayList<>(Arrays.asList("A", "M", "GR", "PR", "H", "GP", "DP"));

        // Game Loop
        do {
            System.out.println("====================== Joueur " + game.getCurrentColor() + " : "
                    + (game.getCurrentColor() == Color.WHITE ? game.getWhitePlayerName() : game.getBlackPlayerName()
            ) + " ======================");
            System.out.println(game.board);

            if(game.isCheck(game.currentColor))
                System.out.println("\n==========\n Vous êtes en Échec \n==========");

            if(game.rules.isEnPassantPossible())
                System.out.println("Vous pouvez effectuer une prise en passant");

            player = (game.getCurrentColor() == Color.WHITE ? game.getWhitePlayerName() : game.getBlackPlayerName());
            do {
                System.out.println("Indiquez le type d'action a effectuer soit un Mouvement M, Abandon A, Grand Roque GR, Petit Roque PR," +
                        " Prise en Passant Gauche GP, Prise en Passant Droite DP ou Historique H : ");
                currentAction = sc.next();
                sc = new Scanner(System.in);
                if(!availableAction.contains(currentAction))
                    System.out.println("Cette Action n'est pas possible Veuillez entrer une Action valide");
            } while(!availableAction.contains(currentAction));

            switch(currentAction) {
                case "A":
                    game.actions.add(new Action(game.turnNum, player, currentAction, "", ""));
                    System.out.println(game.actions.get(game.actions.size() - 1));
                    forfeit = true;
                    break;
                case "DP":
                case "GP":
                    try {
                        game.rules.enPassant(currentAction.equals("GP") ? ChessRules.LEFT_SIDE : ChessRules.RIGHT_SIDE);
                        // Recuperation du mouvement prise en passant pour l'historique
                        game.actions.add(new Action(game.turnNum, player, currentAction,
                                game.rules.getLastStartMove().toAlgebraicNotation(), game.rules.getLastDestinationMove().toAlgebraicNotation()));
                        game.turnNum++;
                        // Remise a null car la prise en passant ne s'effectue pas sur les pions ayant fait une prise en passant
                        // Pas de déplacement de 2 sur la même colonne
                        game.rules.setLastStartMove(null);
                        game.rules.setLastDestinationMove(null);
                        game.currentColor = game.currentColor == Color.BLACK ? Color.WHITE : Color.BLACK;
                    } catch(EnPassantException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "M":
                    System.out.println("Veuillez a la forme algébrique entrez en premier" +
                            " la position de la piece a déplacer et la destination de la piece separe par un espace : ");
                    sc = new Scanner(System.in);
                    startPos = sc.next();
                    destinationPos = sc.next();
                    try {
                        game.turn(new Position(startPos), new Position(destinationPos));
                        game.actions.add(new Action(game.turnNum, player, currentAction, startPos, destinationPos));
                        game.turnNum++;
                        game.rules.setLastStartMove(new Position(startPos));
                        game.rules.setLastDestinationMove(new Position(destinationPos));
                        game.currentColor = game.currentColor == Color.BLACK ? Color.WHITE : Color.BLACK;
                    } catch(ChessMoveException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "GR":
                    try {
                        game.rules.QueenSideRoque();
                        game.turnNum++;
                        game.currentColor = game.currentColor == Color.BLACK ? Color.WHITE : Color.BLACK;
                        // Pas besoin de donner les dernières positions car la prise en passant ne concerne que les pions
                        game.rules.setLastStartMove(null);
                        game.rules.setLastDestinationMove(null);
                    } catch(RoqueException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "PR":
                    try {
                        game.rules.KingSideRoque();
                        game.turnNum++;
                        game.currentColor = game.currentColor == Color.BLACK ? Color.WHITE : Color.BLACK;
                        // Pas besoin de donner les dernières positions car la prise en passant ne concerne que les pions
                        game.rules.setLastStartMove(null);
                        game.rules.setLastDestinationMove(null);
                    } catch(RoqueException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "H":
                    game.displayAllActions();
                    break;
            }

        } while(!game.rules.isCheckMate(game.getCurrentColor()) && !forfeit);
        System.out.println("================== Fin de la Partie ==================");
        System.out.println("Le vainqueur est "
                + (game.getCurrentColor() == Color.WHITE ? game.getBlackPlayerName() : game.getWhitePlayerName()));
    }

    /**
     * Constructeur.
     * @param whitePlayerName - nom du joueur ayant les pièces blanches
     * @param blackPlayerName - nom du joueur ayant les pièces noires
     */
    public Game(String whitePlayerName, String blackPlayerName) {
        this.whitePlayerName = whitePlayerName;
        this.blackPlayerName = blackPlayerName;
        this.board = new Chessboard();
        this.currentColor = Color.WHITE;
        this.whiteKingPosition = new Position("E1");
        this.blackKingPosition = new Position("E8");
        this.rules = new ChessRules(this, this.board);
        this.turnNum = 1;
        this.actions = new ArrayList<>();
    }

    /**
     * Permet de verifier si le joueur de la couleur donne est mis en échec
     * @param color - la couleur du joueur a verifier
     * @return true si le joueur est mis en échec sinon false
     */
    public boolean isCheck(Color color) {
        ArrayList<Piece> opponentPieces =
                this.board.getAllPiecesWithColor(color == Color.WHITE ? Color.BLACK : Color.WHITE);
        Position kingPosition = color == Color.WHITE ? this.whiteKingPosition : this.blackKingPosition;

        for (Piece opponentPiece : opponentPieces) {
            if (opponentPiece.isValidMove(kingPosition)) {
                return true;
            }
        }

        return false;
    }

    /**
     * tour du joueur courant
     * @param start - position de la pièce à déplacer
     * @param end - destination du déplacement
     * @throws ChessMoveException ChessMoveException - si la case de départ est vide, si elle contient une pièce de l'adversaire, ou si le déplacement est invalide.
     */
    public void turn(Position start, Position end) throws ChessMoveException {
        Piece startPiece = this.board.getPiece(start);
        if(startPiece == null) {
            throw new ChessMoveException("La case de depart ne contient pas de pieces" +
                    "pour le mouvement : ", start, end);
        }
        if(startPiece.getColor() != this.currentColor) {
            throw new ChessMoveException("La case de depart contient la piece de l'adversaire et pas une piece " +
                    this.currentColor + " pour le mouvement : ", start, end);
        }
        if(!startPiece.isValidMove(end)) {
            throw new ChessMoveException("Le mouvement indiquée est invalide ou impossible pour la nature de la piece "
                    , start, end);
        }

        startPiece.moveTo(end, this);


        // Mise a jour des positions des rois de chaque joueur
        if(startPiece.getSymbol() == Symbol.WHITE_KING) {
            this.whiteKingPosition = end;
        } else if(startPiece.getSymbol() == Symbol.BLACK_KING) {
            this.blackKingPosition = end;
        }
    }

    /**
     * Permet d'afficher l'historique des actions de la partie a la demande.
     */
    public void displayAllActions() {
        System.out.println("\n\n ====================== HISTORIQUE DES COUPS ======================");
        for(Action action : this.actions) {
            System.out.println(action + "\n");
        }
    }

    /**
     * Retourne le nom du joueur ayant les pièces blanches
     * @return le nom du joueur ayant les pièces blanches
     */
    public String getWhitePlayerName() {
        return whitePlayerName;
    }

    /**
     * Retourne le nom du joueur ayant les pièces noires
     * @return le nom du joueur ayant les pièces noires
     */
    public String getBlackPlayerName() {
        return blackPlayerName;
    }

    /**
     * Retourne la couleur des pièces du joueur dont c'est le tour
     * @return la couleur des pièces du joueur dont c'est le tour
     */
    public Color getCurrentColor() {
        return currentColor;
    }

    /**
     * Retourne l'échiquier
     * @return l'échiquier
     */
    public Chessboard getBoard() {
        return board;
    }

    /**
     * Accesseur sur la position actuelle du Roi du joueur Blanc
     * @return La position du Roi du joueur blanc
     */
    public Position getWhiteKingPosition() {
        return this.whiteKingPosition;
    }

    /**
     * Accesseur sur la position actuelle du Roi du joueur Noir
     * @return La position du Roi du joueur Noir
     */
    public Position getBlackKingPosition() {
        return this.blackKingPosition;
    }

    /**
     * Accesseur sur les actions de la partie
     * @return l'historique des coups de la partie
     */
    public ArrayList<Action> getActions() {
        return this.actions;
    }

    // Setters
    /**
     * Pour Test unitaire
     * Modificateur sur la position actuelle du roi blanc dans l'échiquier
     * @param whiteKingPosition - la nouvelle position du roi dans l'échiquier
     */
    public void setWhiteKingPosition(Position whiteKingPosition) {
        this.whiteKingPosition = whiteKingPosition;
    }
}
