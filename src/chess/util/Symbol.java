package chess.util;

/**
 * Codes (UTF16) des symboles du jeu d'échecs pour un affichage en mode texte. <br><i>Thank's to JM Nourrit<i/>
 *
 * @author Eric Desjardin
 *
 */
public class Symbol {

    /** On ne peut pas créer une instance de Symbol */
    private Symbol() {
    }

    /** Symbole du roi blanc (code UTF-16 : 0x2654) */
    public static final char WHITE_KING = 0x2654;

    /** Symbole de la reine blanche (code UTF-16 : 0x2655) */
    public static final char WHITE_QUEEN = 0x2655;

    /** Symbole de la tour blanche (code UTF-16 : 0x2656) */
    public static final char WHITE_ROOK = 0x2656;

    /** Symbole du fou blanc (code UTF-16 : 0x2657) */
    public static final char WHITE_BISHOP = 0x2657;

    /** Symbole du cavalier blanc (code UTF-16 : 0x2658) */
    public static final char WHITE_KNIGHT = 0x2658;

    /** Symbole du pion blanc (code UTF-16 : 0x2659) */
    public static final char WHITE_PAWN = 0x2659;

    /** Symbole du roi noir (code UTF-16 : 0x265A) */
    public static final char BLACK_KING = 0x265A;

    /** Symbole de la reine noire (code UTF-16 : 0x265B) */
    public static final char BLACK_QUEEN = 0x265B;

    /** Symbole de la tour noire (code UTF-16 : 0x265C) */
    public static final char BLACK_ROOK = 0x265C;

    /** Symbole du fou noir (code UTF-16 : 0x265D) */
    public static final char BLACK_BISHOP = 0x265D;

    /** Symbole du cavalier noir (code UTF-16 : 0x265E) */
    public static final char BLACK_KNIGHT = 0x265E;

    /** Symbole du pion noir (code UTF-16 : 0x265F) */
    public static final char BLACK_PAWN = 0x265F;

}
