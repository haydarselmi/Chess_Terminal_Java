package chess;

import chess.pieces.*;
import chess.util.Color;
import chess.util.Position;
import chess.util.Symbol;

import java.util.ArrayList;

/////////////////// A FAIRE ////////////////////////
// A FAIRE A LA FIN DE LA V2
// CLASSE PEUT ETRE SÉPARÉ EN CRÉANT UNE CLASSE MERE ABSTRAITE QUI HÉRITE UNE CLASSE FILLE POUR LE ROQUE
// UNE CLASSE FILLE POUR LA PRISE EN PASSANT ET UNE AUTRE CLASSE FILLE POUR LA DETECTION DE L'ÉCHEC ET MAT
/////////////////// A FAIRE ////////////////////////

/**
 Classe de gestion des règles du jeu d'échecs
 Elle permet la détection de l'échec et mat
 Classe permettant sur le joueur courant
 la verification et l'application des mouvements Roque et passant sur un jeu d'échecs
 */
public class ChessRules {
    public static final String LEFT_SIDE = "GP";
    public static final String RIGHT_SIDE = "DP";
    /* Attributs d'instance */
    private Game game;
    private Chessboard board;
    private Position lastStartMove;
    private Position lastDestinationMove;

    /* Méthodes d'instance */
    /**
     * Constructeur
     * @param game - Le jeu sur lequels les regles vont etre appliquees
     * @param board - l'echiquier du jeu.
     */
    public ChessRules(Game game, Chessboard board) {
        this.game = game;
        this.board = board;
        this.lastStartMove = null;
        this.lastDestinationMove = null;
    }

    // Setters - Utiles pour la gestion de la règle de prise en passant d'un pion
    /**
     * Permet de modifier la position de départ de la dernière piece jouée
     * @param lastStartMove - la position de départ  de la piece
     */
    public void setLastStartMove(Position lastStartMove) {
        this.lastStartMove = lastStartMove;
    }

    /**
     * Permet de modifier la position d'arrivé de la dernière piece jouée
     * @param lastDestinationMove - la position d'arrivé de la piece
     */
    public void setLastDestinationMove(Position lastDestinationMove) {
        this.lastDestinationMove = lastDestinationMove;
    }

    /**
     * Effectue une prise en passant du coté indiqué par le joueur
     * @param side - le coté du pion du joueur actuel effectuant la prise en passant par rapport au pion qui vient pion adverse
     * @throws EnPassantException si la prise en passant n'est pas possible pour la situation, ou si la prise en passant
     * est possible mais pas pour le coté indiqué par le joueur.
     */
    public void enPassant(String side) throws EnPassantException {
        if(!this.isEnPassantPossible()) {
            throw new EnPassantException("La prise en passant est impossible pour la situation actuelle");
        }

        if(!this.checkSidePassant(side)) {
            throw new EnPassantException("La prise en passant est possible mais pas pour le pion du coté indiqué" +
                    " mais plutôt pour votre pion du coté " + (side.equals(LEFT_SIDE) ? "droit" : "gauche"));
        }

        this.board.setPiece(this.lastDestinationMove, null);
        int sideX = side.equals(LEFT_SIDE) ? -1 : 1;
        int lineY = this.game.getCurrentColor() == Color.WHITE ? 1 : -1;
        Piece playerPiece = this.board.getPiece(new Position(this.lastDestinationMove.getX() + sideX, this.lastDestinationMove.getY()));
        this.lastStartMove = playerPiece.getPosition();
        this.board.setPiece(new Position(playerPiece.getPosition().getX(), this.lastDestinationMove.getY()), null);
        playerPiece.getPosition().setX(this.lastDestinationMove.getX());
        playerPiece.getPosition().setY(this.lastDestinationMove.getY() + lineY);
        this.board.setPiece(playerPiece.getPosition(), playerPiece);
        this.lastDestinationMove = playerPiece.getPosition();
    }

    /**
     * Permet de vérifier si l'application de la prise en passant est possible pour le joueur courant
     * @return true si la prise en passant est possible, false sinon
     */
    public boolean isEnPassantPossible() {
        if(this.lastStartMove == null) return false;
        if(this.lastStartMove.getManhattanDistance(this.lastDestinationMove) == 2) {
            if(this.lastStartMove.isOnSameColumnAs(this.lastDestinationMove)) {
                if(this.board.getPiece(this.lastDestinationMove) instanceof Pawn) {
                    if(this.game.getCurrentColor() != this.board.getPiece(this.lastDestinationMove).getColor()) {
                        return this.checkSidePassant(LEFT_SIDE) || this.checkSidePassant(RIGHT_SIDE);
                    }
                }
            }
        }
        return false;
    }

    /**
     * Vérifie si la prise en passant est possible par le pion du coté donné du pion qui vient d'avancer de 2.
     * @param side - le coté du pion du joueur actuel par rapport au pion qui vient d'avancer de 2
     * @return true s'il le joueur a bien un pion du coté indiqué et que la prise s'effectue bien sur le joueur adverse
     * , false sinon
     */
    public boolean checkSidePassant(String side) {
        if(this.lastStartMove == null) return false;
        int sideX = side.equals(LEFT_SIDE) ? -1 : 1;
        if (this.lastDestinationMove.getX() + sideX >= 0 && this.lastDestinationMove.getX() + sideX < 8) {
            Piece leftCurrentPlayer = this.board.getPiece(new Position(this.lastDestinationMove.getX() + sideX,
                    this.lastDestinationMove.getY()));
            if (leftCurrentPlayer != null) {
                return leftCurrentPlayer.getColor() != this.board.getPiece(this.lastDestinationMove).getColor();
            }
        }
        return false;
    }

    // Getters
    public Position getLastStartMove() {
        return lastStartMove;
    }

    public Position getLastDestinationMove() {
        return lastDestinationMove;
    }

    /**
     * Permet d'effectuer un Grand Roque pour le joueur courant
     * @throws RoqueException si le Grand Roque n'est pas possible
     */
    public void QueenSideRoque() throws RoqueException {
        if(!this.canRoqueQueenSide()) {
            throw new RoqueException("Grand Roque impossible car il y a peut être des pieces dans l'aile ou " +
                    "le roi est en échec pendant le déplacement");
        }

        Color currentColor = this.game.getCurrentColor();
        Position kingPos = currentColor == Color.WHITE ? this.game.getWhiteKingPosition() : this.game.getBlackKingPosition();

        Piece king = this.board.getPiece(kingPos);
        this.board.setPiece(kingPos, null);
        king.getPosition().setX(kingPos.getX() - 2);
        this.board.setPiece(king.getPosition(), king);
        kingPos = king.getPosition();

        Position initialRookPos = (currentColor == Color.WHITE ? new Position("A1") : new Position("A8"));
        Piece rook = this.board.getPiece(initialRookPos);
        this.board.setPiece(initialRookPos, null);
        rook.getPosition().setX(rook.getPosition().getX() + 3);
        this.board.setPiece(rook.getPosition(), rook);
    }

    /**
     * Permet d'effectuer un Petit Roque pour le joueur courant
     * @throws RoqueException si le Petit Roque n'est pas possible
     */
    public void KingSideRoque() throws RoqueException {
        if(!this.canRoqueKingSide()) {
            throw new RoqueException("Petit Roque impossible car il y a peut être des pieces dans l'aile ou " +
                    "le roi est en échec pendant le déplacement ou le roi ou la tour ont deja effectué un mouvement");
        }

        Color currentColor = this.game.getCurrentColor();
        Position kingPos = currentColor == Color.WHITE ? this.game.getWhiteKingPosition() : this.game.getBlackKingPosition();

        Piece king = this.board.getPiece(kingPos);
        this.board.setPiece(kingPos, null);
        king.getPosition().setX(kingPos.getX() + 2);
        this.board.setPiece(king.getPosition(), king);
        kingPos = king.getPosition();

        Position initialRookPos = (currentColor == Color.WHITE ? new Position("H1") : new Position("H8"));
        Piece rook = this.board.getPiece(initialRookPos);
        this.board.setPiece(initialRookPos, null);
        rook.getPosition().setX(rook.getPosition().getX() - 2);
        this.board.setPiece(rook.getPosition(), rook);
    }

    /**
     * Permet de verifier si le grand Roque est possible sur l'aile de la reine
     * @return true si le grand Roque est possible, false sinon
     */
    public boolean canRoqueQueenSide() {
        Color currentColor = this.game.getCurrentColor();
        Position initialKingPos = new Position(4, (currentColor == Color.WHITE ? 0 : 7));
        Position initialRookPos = (currentColor == Color.WHITE ? new Position("A1") : new Position("A8"));
        Position kingPos = currentColor == Color.WHITE ? this.game.getWhiteKingPosition() : this.game.getBlackKingPosition();
        King king = (King) this.board.getPiece(initialKingPos);
        Rook rightRook = (Rook) this.board.getPiece(initialRookPos);
        char kingSymbol = currentColor == Color.WHITE ? Symbol.WHITE_KING : Symbol.BLACK_KING;
        char rookSymbol = currentColor == Color.WHITE ? Symbol.WHITE_ROOK : Symbol.BLACK_ROOK;
        // Verification si les pieces sont bien a leur places
        if(king == null || kingSymbol != king.getSymbol() || rightRook == null || rookSymbol != rightRook.getSymbol())
            return false;
        // Verification de l'absence de pieces entre le Roi et la Tour
        if(!this.board.isPiecePresentOnSameLineBetween(initialRookPos, initialKingPos)) {
            // Verification de l'absence d'échec lors du mouvement
            for(int i = 0; i < 3; i++) {
                this.board.setPiece(king.getPosition(), null);
                kingPos.setX(king.getPosition().getX() - 1);
                king.getPosition().setX(kingPos.getX());
                this.board.setPiece(king.getPosition(), king);
                if(this.game.isCheck(currentColor)) {
                    kingPos.setX(initialKingPos.getX());
                    this.board.setPiece(king.getPosition(), null);
                    king.getPosition().setX(initialKingPos.getX());
                    this.board.setPiece(initialKingPos, king);
                    return false;
                }
            }
            kingPos.setX(initialKingPos.getX());
            this.board.setPiece(king.getPosition(), null);
            king.getPosition().setX(initialKingPos.getX());
            this.board.setPiece(initialKingPos, king);
            return true;
        }
        return false;
    }

    /**
     * Permet de verifier si le petit Roque est possible sur l'aile du roi
     * @return true si le petit Roque est possible, false sinon
     */
    public boolean canRoqueKingSide() {
        Color currentColor = this.game.getCurrentColor();
        Position initialKingPos = new Position(4, (currentColor == Color.WHITE ? 0 : 7));
        Position initialRookPos = (currentColor == Color.WHITE ? new Position("H1") : new Position("H8"));
        Position kingPos = currentColor == Color.WHITE ? this.game.getWhiteKingPosition() : this.game.getBlackKingPosition();
        King king = (King) this.board.getPiece(initialKingPos);
        Rook rightRook = (Rook) this.board.getPiece(initialRookPos);
        char kingSymbol = currentColor == Color.WHITE ? Symbol.WHITE_KING : Symbol.BLACK_KING;
        char rookSymbol = currentColor == Color.WHITE ? Symbol.WHITE_ROOK : Symbol.BLACK_ROOK;
        // Verification si les pieces sont bien a leur places
        if(king == null || kingSymbol != king.getSymbol() || rightRook == null || rookSymbol != rightRook.getSymbol())
            return false;

        // Verification si les pieces n'ont pas encore bougé
        if(king.isNotMovedYet() && rightRook.isNotMovedYet()) {
            // Verification de l'absence de pieces entre le Roi et la Tour
            if(!this.board.isPiecePresentOnSameLineBetween(initialKingPos, initialRookPos)) {
                // Verification de l'absence d'échec lors du mouvement
                for(int i = 0; i < 2; i++) {
                    this.board.setPiece(king.getPosition(), null);
                    kingPos.setX(king.getPosition().getX() + 1);
                    king.getPosition().setX(kingPos.getX());
                    this.board.setPiece(king.getPosition(), king);
                    if(this.game.isCheck(currentColor)) {
                        kingPos.setX(initialKingPos.getX());
                        this.board.setPiece(king.getPosition(), null);
                        king.getPosition().setX(initialKingPos.getX());
                        this.board.setPiece(initialKingPos, king);
                        return false;
                    }
                }
                kingPos.setX(initialKingPos.getX());
                this.board.setPiece(king.getPosition(), null);
                king.getPosition().setX(initialKingPos.getX());
                this.board.setPiece(initialKingPos, king);
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si le roi de la couleur indiquée est échec et mat
     * @param color - couleur du roi à vérifier
     * @return true si le roi est échec et mat, false sinon
     */
    public boolean isCheckMate(Color color) {
        if(!this.game.isCheck(color)) return false;

        ArrayList<Piece> opponentPieces =
                this.board.getAllPiecesWithColor(color == Color.WHITE ? Color.BLACK : Color.WHITE);
        Position kingPosition = color == Color.WHITE ? this.game.getWhiteKingPosition() : this.game.getWhiteKingPosition();

        if(this.canKingWithColorEscapeWithRoque(color)) return false;
        if(this.canKingWithColorEscapeOrCounterAttack(color)) return false;
        if(opponentPieces.size() == 1) {
            return !this.canKingWithColorBeDefended(color);
        }

        return true;
    }

    /**
     * Permet de verifier si le roi a la possibilité d'échapper ou contre-attaquer un échec
     * @param color - La couleur du roi a verifier
     * @return true si le roi a une possibilité d'échapper ou d'effectuer uen contre attaque
     */
    private boolean canKingWithColorEscapeOrCounterAttack(Color color) {
        ArrayList<Piece> opponentPieces =
                this.board.getAllPiecesWithColor(color == Color.WHITE ? Color.BLACK : Color.WHITE);
        Position kingPosition = color == Color.WHITE ? this.game.getWhiteKingPosition() : this.game.getWhiteKingPosition();

        for(int y = kingPosition.getY(); y < kingPosition.getY() + 3; y++) {
            for(int x = kingPosition.getX(); x < kingPosition.getX() + 3; x++) {
                if(x >= 0 && x < 8 && y >= 0 && y < 8) {
                    if (this.board.getPiece(kingPosition).isValidMove(new Position(x, y))) {
                        // Verification de la possibilité du mouvement vers les Cases voisines
                        for (Piece opponentPiece : opponentPieces) {
                            if (opponentPiece.isValidMove(new Position(x, y))) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Vérifie si le Roi de la couleur donnée peut échapper l'échec avec un Roque
     * @param color - la couleur du roi à vérifier
     * @return true si le roi peut échapper à l'échec avec un Roque, false sinon
     */
    private boolean canKingWithColorEscapeWithRoque(Color color) {
        ArrayList<Piece> opponentPieces =
                this.board.getAllPiecesWithColor(color == Color.WHITE ? Color.BLACK : Color.WHITE);
        Position kingPos = color == Color.WHITE ? this.game.getWhiteKingPosition() : this.game.getWhiteKingPosition();
        Position roqueDestination = new Position(kingPos.getX(), kingPos.getY());

        if(this.canRoqueKingSide()) {
            roqueDestination.setX(roqueDestination.getX() + 2);
        } else if(this.canRoqueQueenSide()) {
            roqueDestination.setX(roqueDestination.getX() - 3);
        }

        for(Piece opponentPiece : opponentPieces) {
            if (opponentPiece.isValidMove(roqueDestination)) {
                return false;
            }
        }

        return true;
    }

    /**
     * APRES FIN DE LA V2 : A FAIRE : FACTORISER CETTE FONCTION
     * Vérifie s'il est possible de défendre le roi avec une pièce de la pièce mettant en échec le roi
     * @param color - La couleur du roi à vérifier
     * @return true si le roi peut être défendu.
     */
    private boolean canKingWithColorBeDefended(Color color) {
        ArrayList<Piece> opponentPieces =
                this.board.getAllPiecesWithColor(color == Color.WHITE ? Color.BLACK : Color.WHITE);
        ArrayList<Piece> playerPieces =
                this.board.getAllPiecesWithColor(color);
        Position kingPosition = color == Color.WHITE ? this.game.getWhiteKingPosition() : this.game.getWhiteKingPosition();
        Piece opponentPiece = opponentPieces.get(0);

        if(opponentPiece instanceof Rook) {
            if(opponentPiece.getPosition().isOnSameColumnAs(kingPosition) && opponentPiece.getPosition().getManhattanDistance(kingPosition) >= 2) {
                Position pos_b = opponentPiece.getPosition().getY() < kingPosition.getY() ? opponentPiece.getPosition() : kingPosition;
                Position pos_h = pos_b == opponentPiece.getPosition() ? kingPosition : opponentPiece.getPosition();
                pos_b.setY(pos_b.getY() + 1);
                while(pos_b.getY() != pos_h.getY()) {
                    if(this.board.getPiece(pos_b) == null) {
                        for(Piece playerPiece : playerPieces) {
                            if(playerPiece.isValidMove(pos_b)) return true;
                            pos_b.setX(pos_b.getY() + 1);
                        }
                    }
                }
            } else if(opponentPiece.getPosition().isOnSameLineAs(kingPosition) && opponentPiece.getPosition().getManhattanDistance(kingPosition) >= 2) {
                Position pos_g = opponentPiece.getPosition().getX() < kingPosition.getX() ? opponentPiece.getPosition() : kingPosition;
                Position pos_d = pos_g == opponentPiece.getPosition() ? kingPosition : opponentPiece.getPosition();
                pos_g.setY(pos_g.getX() + 1);
                while(pos_g.getY() != pos_d.getY()) {
                    if(this.board.getPiece(pos_g) == null) {
                        for(Piece playerPiece : playerPieces) {
                            if(playerPiece.isValidMove(pos_g)) return true;
                            pos_g.setX(pos_g.getX() + 1);
                        }
                    }
                }
            }
        } else if(opponentPiece instanceof Bishop && opponentPiece.getPosition().getManhattanDistance(kingPosition) > 2) {
            int incrementY = opponentPiece.getPosition().getY() < kingPosition.getY() ? 1 : -1;
            int incrementX = opponentPiece.getPosition().getX() < kingPosition.getX() ? 1 : -1;
            Position actualPos = opponentPiece.getPosition();
            actualPos.setX(actualPos.getX() + incrementX);
            actualPos.setY(actualPos.getY() + incrementY);
            while(actualPos.getY() != kingPosition.getY() && actualPos.getX() != kingPosition.getX()) {
                if(this.board.getPiece(actualPos) == null) {
                    for(Piece playerPiece : playerPieces) {
                        if(playerPiece.isValidMove(actualPos)) return true;
                        actualPos.setX(actualPos.getX() + incrementX);
                        actualPos.setY(actualPos.getY() + incrementY);
                    }
                }
            }
        } else if(opponentPiece instanceof Queen) {
            if(opponentPiece.getPosition().isOnSameDiagonalAs(kingPosition)
                    && opponentPiece.getPosition().getManhattanDistance(kingPosition) > 2) {
                int incrementY = opponentPiece.getPosition().getY() < kingPosition.getY() ? 1 : -1;
                int incrementX = opponentPiece.getPosition().getX() < kingPosition.getX() ? 1 : -1;
                Position actualPos = opponentPiece.getPosition();
                actualPos.setX(actualPos.getX() + incrementX);
                actualPos.setY(actualPos.getY() + incrementY);
                while(actualPos.getY() != kingPosition.getY() && actualPos.getX() != kingPosition.getX()) {
                    if(this.board.getPiece(actualPos) == null) {
                        for(Piece playerPiece : playerPieces) {
                            if(playerPiece.isValidMove(actualPos)) return true;
                            actualPos.setX(actualPos.getX() + incrementX);
                            actualPos.setY(actualPos.getY() + incrementY);
                        }
                    }
                }
            } else if(opponentPiece.getPosition().getManhattanDistance(kingPosition) >= 2) {
                if(opponentPiece.getPosition().isOnSameColumnAs(kingPosition)) {
                    Position pos_b = opponentPiece.getPosition().getY() < kingPosition.getY() ? opponentPiece.getPosition() : kingPosition;
                    Position pos_h = pos_b == opponentPiece.getPosition() ? kingPosition : opponentPiece.getPosition();
                    pos_b.setY(pos_b.getY() + 1);
                    while(pos_b.getY() != pos_h.getY()) {
                        if(this.board.getPiece(pos_b) == null) {
                            for(Piece playerPiece : playerPieces) {
                                if(playerPiece.isValidMove(pos_b)) return true;
                                pos_b.setX(pos_b.getY() + 1);
                            }
                        }
                    }
                } else if(opponentPiece.getPosition().isOnSameLineAs(kingPosition)) {
                    Position pos_g = opponentPiece.getPosition().getX() < kingPosition.getX() ? opponentPiece.getPosition() : kingPosition;
                    Position pos_d = pos_g == opponentPiece.getPosition() ? kingPosition : opponentPiece.getPosition();
                    pos_g.setY(pos_g.getX() + 1);
                    while(pos_g.getY() != pos_d.getY()) {
                        if(this.board.getPiece(pos_g) == null) {
                            for(Piece playerPiece : playerPieces) {
                                if(playerPiece.isValidMove(pos_g)) return true;
                                pos_g.setX(pos_g.getX() + 1);
                            }
                        }
                    }
                }
            }

        }

        return false;
    }

}