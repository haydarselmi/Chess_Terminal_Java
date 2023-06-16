package chess.util;

/**
 * classe Position qui sert à la manipulation des pions du jeu d'échecs
 */
public class Position {
    /* Attributs d'instance */
    private int x;
    private int y;

    /* Méthodes d'instance */

    // Constructors
    /**
     * Constructeur de la Position avec les coordonnées x, y
     * @param x position sur x
     * @param y position sur y
     * @throws IllegalArgumentException exception
     */
    public Position(int x, int y)
    {
        try {
            this.setX(x);
            this.setY(y);
        } catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Constructeur de Position avec la notation algébrique du jeu
     * @param algebraicNotation notation algébrique de la position de A1 à H8
     * @throws IllegalArgumentException exception
     */
    public Position(String algebraicNotation) {
        char lettre = Character.toUpperCase(algebraicNotation.charAt(0));
        char chiffre = algebraicNotation.charAt(1);
        int posLettre = lettre - 'A';
        int posChiffre = chiffre - '1';
        try {
            this.setX(posLettre);
            this.setY(posChiffre);
        } catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // Getters
    /**
     * Accesseur sur la coordonnée x
     * @return la coordonnée x
     */
    public int getX() {
        return this.x;
    }

    /**
     * Accesseur sur la coordonnée y
     * @return la coordonnée y
     */
    public int getY() {
        return this.y;
    }

    // Setters
    /**
     * Modificateur sur la coordonnée x
     * @param x la nouvelle coordonnée x
     * @throws IllegalArgumentException exception
     */
    public void setX(int x) {
        if(x < 0 || x > 7)
            throw new IllegalArgumentException("Position invalide : " + x);
        this.x = x;
    }

    /**
     * Modificateur sur la coordonnée y
     * @param y la nouvelle coordonnée y
     * @throws IllegalArgumentException exception
     */
    public void setY(int y) {
        if(y < 0 || y > 7)
            throw new IllegalArgumentException("Position invalide : " + y);
        this.y = y;
    }

    // Methods
    /**
     * Vérifie si la position courante est sur la même ligne qu'une autre position
     * @param pos position à tester
     * @return true si la position courante est sur la même ligne que pos
     */
    public boolean isOnSameLineAs(Position pos) {
        return this.y == pos.getY();
    }

    /**
     * Vérifie si la position courante est sur la même colonne qu'une autre position
     * @param pos position à tester
     * @return true si la position courante est sur la même colonne que pos
     */
    public boolean isOnSameColumnAs(Position pos) {
        return this.x == pos.getX();
    }

    /**
     * Vérifie si la position courante est sur la même diagonale qu'une autre position
     * @param pos position à tester
     * @return true si la position courante est sur la même diagonale que pos
     */
    public boolean isOnSameDiagonalAs(Position pos) {
        return Math.abs(this.x - pos.getX()) == Math.abs(this.y - pos.getY());
    }

    /**
     * Retourne la distance de Manhattan entre la position courante et une autre position.
     * @param pos position dont ono souhaite connaître la distance
     * @return distance de Manhattan entre la position courante et otherPosition
     */
    public int getManhattanDistance(Position pos) {
        return Math.abs(this.x - pos.getX()) + Math.abs(this.y - pos.getY());
    }

    /**
     * Permet d'afficher la Position directement en String
     * @return la Position (x,y)
     */
    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    /**
     * Permet de verifier si deux positions sont égales
     * @return le résultat de la verification
     */
    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }

        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        Position position = (Position) o;

        if(x != position.x)
            return false;

        return y == position.y;
    }

    /**
     * Donne la position algébrique de la position allant de A1 a H8
     * @return la position algébrique
     */
    public String toAlgebraicNotation() {
        char lettre, chiffre;
        chiffre = (char) (48 + this.y);
        lettre = (char) (96 + this.x);
        return Character.toString(Character.toUpperCase(lettre)) + Character.toUpperCase(chiffre);
    }
}
