package chess;

import chess.pieces.*;
import chess.util.Color;
import chess.util.Position;
import chess.util.Symbol;

import java.util.ArrayList;

/**
 * Classe représentant un échiquier lors d'une partie d'échecs
 */
public class Chessboard {
    /* Attributs d'instance */
    /**
     * tableau de pièces représentant les cases de l'échiquier (une case vide vaut null).
     */
    private Piece[][] pieces;

    /* Méthode d'instance */

    // Constructors
    /**
     * Constructeur par défaut.
     * Constructeur par défaut. Initialise l'échiquier avec toutes les pièces sur leur case de départ.
     */
    public Chessboard() {
        this.pieces = new Piece[8][8];
        for(int i = 0; i < 8; i++) {
            // Dans un jeu basique on a deux joueurs de couleurs différentes
            // Initialisation des pieces Pawn pions de chaque joueur lignes 1 et 6

            pieces[1][i] = new Pawn(this, new Position(i, 1), Color.WHITE);
            pieces[6][i] = new Pawn(this, new Position(i, 6), Color.BLACK);

            // Initialisation des pieces de premiere range pour les deux joueurs ligne 0 et 7
            // ligne 0 étant le joueur whitePlayer blanc
            switch(i) {
                case 0:
                case 7:
                    pieces[0][i] = new Rook(this, new Position(i, 0), Color.WHITE);
                    pieces[7][i] = new Rook(this, new Position(i, 7), Color.BLACK);
                    break;
                case 1:
                case 6:
                    pieces[0][i] = new Knight(this, new Position(i, 0), Color.WHITE);
                    pieces[7][i] = new Knight(this, new Position(i, 7), Color.BLACK);
                    break;
                case 2:
                case 5:
                    pieces[0][i] = new Bishop(this, new Position(i, 0), Color.WHITE);
                    pieces[7][i] = new Bishop(this, new Position(i, 7), Color.BLACK);
                    break;
                case 3:
                    pieces[0][i] = new Queen(this, new Position(i, 0), Color.WHITE);
                    pieces[7][i] = new Queen(this, new Position(i, 7), Color.BLACK);
                    break;
                case 4:
                    pieces[0][i] = new King(this, new Position(i, 0), Color.WHITE);
                    pieces[7][i] = new King(this, new Position(i, 7), Color.BLACK);
                    break;
            }
        }
    }

    // Getters

    /**
     * Retourne la pièce de la case (x,y) de l'échiquier ou null si la case est vide
     * @param x - abscisse de la case (0 à 7)
     * @param y - ordonnée de la case (0 à 7)
     * @return pièce située sur la case ou null si la case est vide
     */
    public Piece getPiece(int x, int y) {
        if(this.pieces[y][x] != null) {
            return this.pieces[y][x];
        }
        return null;
    }

    /**
     * Retourne la pièce de la case indiquée par pos ou null si la case est vide
     * @param pos - position de la case
     * @return pièce située sur la case ou null si la case est vide
     */
    public Piece getPiece(Position pos) {
        if(this.pieces[pos.getY()][pos.getX()] != null) {
            return this.pieces[pos.getY()][pos.getX()];
        }
        return null;
    }

    /**
     * Retourne une liste de toutes les pieces de la couleur donne sur l'échiquier.
     * @param color - La couleur des pieces a retourner
     * @return - Une liste des pieces de la couleur color
     */
    public ArrayList<Piece> getAllPiecesWithColor(Color color) {
        ArrayList<Piece> pieces = new ArrayList<Piece>();
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                if(this.getPiece(x, y) != null) {
                    if(this.getPiece(x, y).getColor() == color)
                        pieces.add(this.getPiece(x, y));
                }
            }
        }

        return pieces;
    }

    // Setters

    /**
     * Remplace la pièce située sur la case indiquée
     * @param pos - pos de la case de destination
     * @param newPiece - nouvelle pièce de la case
     */
    public void setPiece(Position pos, Piece newPiece) throws IllegalArgumentException {
        if(pos.getX() < 0 || pos.getY() < 0 || pos.getX() > 7 || pos.getY() > 7) {
            throw new IllegalArgumentException("La Position de la piece dans l'échiquier n'est pas valide" +
                    " x : " + pos.getX() + " y : " + pos.getY());
        }
        this.pieces[pos.getY()][pos.getX()] = newPiece;
    }

    /**
     * teste la présence d'une pièce sur la diagonale comprise entre les positions start et end (exclues)
     * @param start - première extrémité
     * @param end - seconde extrémité
     * @return true s'il y a une pièce sur la diagonale comprise entre les positions start et end (exclues), false sinon.
     * @throws java.lang.IllegalArgumentException - si les positions start et end ne sont pas sur la même diagonale
     */
    public boolean isPiecePresentOnSameDiagonalBetween(Position start, Position end) throws IllegalArgumentException {
        if (!start.isOnSameDiagonalAs(end)) {
            throw new IllegalArgumentException("Les position start " + start + " et end " + end +
                    " ne sont pas sur la meme diagonale");
        }

        if (start.getX() == end.getX() || Math.abs(start.getX() - end.getX()) == 1)
            return false;
        /*
          On vérifie la difference des 2 positions sur x et y pour
          savoir dans quel sens se dirige la diagonale
          ou S est start et E est end :
          - comme dans ce cas x et y positif qui se dirige vers le haut a droite
          E4      |      E1
                  |
                  |
          ////////S////////
                  |
                  |
          E3      |      E2
          */
        int incrementY = (end.getY() - start.getY()) > 0 ? 1 : -1;
        int incrementX = (end.getX() - start.getX()) > 0 ? 1 : -1;
        int x_start = start.getX() + incrementX;
        int y_start = start.getY() + incrementY;

        while (x_start != end.getX()) {
            if (this.getPiece(x_start, y_start) != null) {
                return true;
            }
            x_start += incrementX;
            y_start += incrementY;
        }

        return false;
    }

    /**
     * teste la présence d'une pièce sur la colonne comprise entre les positions start et end (exclues)
     * @param start - première extrémité
     * @param end - seconde extrémité
     * @return true s'il y a une pièce sur la colonne comprise entre les positions start et end (exclues), false sinon.
     * @throws IllegalArgumentException - si les positions start et end ne sont pas sur la même colonne
     */
    public boolean isPiecePresentOnSameColumnBetween(Position start, Position end) throws IllegalArgumentException {
        if (!start.isOnSameColumnAs(end)) {
            throw new IllegalArgumentException("Les position start " + start + " et end " + end +
                    " ne sont pas sur la meme colonne");
        }

        if(start.getY() == end.getY() || Math.abs(start.getY() - end.getY()) == 1)
            return false;

        Position pos_bas = start.getY() < end.getY() ? start : end;
        Position pos_haut = start == pos_bas ? end : start;

        int pos_basX = pos_bas.getX();
        int pos_basY = pos_bas.getY() + 1;
        while(pos_basY != pos_haut.getY()) {
            if(this.getPiece(pos_basX, pos_basY) != null) {
                return true;
            }
            pos_basY++;
        }

        return false;
    }

    /**
     * teste la présence d'une pièce sur la ligne comprise entre les positions start et end (exclues)
     * @param start - première extrémité
     * @param end - seconde extrémité
     * @return true s'il y a une pièce sur la ligne comprise entre les positions start et end (exclues), false sinon.
     * @throws IllegalArgumentException - si les positions start et end ne sont pas sur la même ligne
     */
    public boolean isPiecePresentOnSameLineBetween(Position start, Position end) throws IllegalArgumentException {
        if(!start.isOnSameLineAs(end)) {
            throw new IllegalArgumentException("Les position start " + start + " et end " + end +
                    " ne sont pas sur la meme ligne");
        }

        if(start.getX() == end.getX() || Math.abs(start.getX() - end.getX()) == 1)
            return false;

        Position pos_g = start.getX() < end.getX() ? start : end;
        Position pos_d = (start == pos_g) ? end : start;

        int pos_g_X = pos_g.getX() + 1;
        int pos_g_Y = pos_g.getY();
        while(pos_g_X != pos_d.getX()) {
            if(this.getPiece(pos_g_X, pos_g_Y) != null)
                return true;

            pos_g_X++;
        }
        return false;
    }

    /**
     * @return Retourne une chaîne de caractères représentant l'échiquier. Exemple :
     *       A   B   C   D   E   F   G   H
     *    ┏━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┓
     *  8 ┃ ♝ ┃ ♞ ┃ ♟ ┃   ┃ ♚ ┃ ♝ ┃   ┃   ┃ 8
     *    ┣━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━┫
     *  7 ┃ ♟ ┃ ♖ ┃   ┃ ♟ ┃   ┃ ♟ ┃   ┃ ♟ ┃ 7
     *    ┣━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━┫
     *  6 ┃ ♙ ┃   ┃   ┃ ♟ ┃   ┃   ┃   ┃   ┃ 6
     *    ┣━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━┫
     *  5 ┃ ♜ ┃   ┃   ┃ ♕ ┃   ┃ ♙ ┃   ┃   ┃ 5
     *    ┣━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━┫
     *  4 ┃ ♗ ┃ ♙ ┃   ┃   ┃   ┃   ┃   ┃   ┃ 4
     *    ┣━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━┫
     *  3 ┃   ┃ ♜ ┃ ♙ ┃ ♟ ┃   ┃   ┃ ♘ ┃   ┃ 3
     *    ┣━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━┫
     *  2 ┃ ♙ ┃ ♙ ┃   ┃ ♟ ┃   ┃   ┃ ♙ ┃   ┃ 2
     *    ┣━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━┫
     *  1 ┃ ♞ ┃ ♙ ┃ ♗ ┃ ♛ ┃ ♔ ┃   ┃ ♘ ┃ ♖ ┃ 1
     *    ┗━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┛
     *      A   B   C   D   E   F   G   H
     */
    @Override
    public String toString() {
        StringBuilder display = new StringBuilder();
        display.append(" ************************************\n");
        display.append("    A   B   C   D   E   F   G   H \n");
        display.append("  ┏━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┓\n");
        Piece currPiece;
        for(int i = 8; i >= 1; i--) {
            display.append(i + " ┃");
            for(int j = 0; j < 8; j++) {
                currPiece = this.pieces[i-1][j];
                if(currPiece != null)
                    display.append(" " + currPiece.getSymbol() + " ┃");
                else
                    display.append("   ┃");
            }
            if(i > 1)
                display.append("\n  ┣━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━┫\n");
        }
        display.append("\n  ┗━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┛\n");
        display.append("    A   B   C   D   E   F   G   H ");

        return display.toString();
    }
}
