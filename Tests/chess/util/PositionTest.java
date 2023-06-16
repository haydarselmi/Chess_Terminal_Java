package chess.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {
    @Test
    public void testPosition() {
        Position pos1 = new Position("A1");
        Position pos2 = new Position("B2");
        Position pos3 = new Position("C3");
        Position pos4 = new Position("D4");
        Position pos5 = new Position("E5");
        Position pos6 = new Position("F6");
        Position pos7 = new Position("G7");
        Position pos8 = new Position("H8");

        assertEquals(0, pos1.getX() + pos1.getY());
        assertEquals(2, pos2.getX() + pos2.getY());
        assertEquals(4, pos3.getX() + pos3.getY());
        assertEquals(6, pos4.getX() + pos4.getY());
        assertEquals(8, pos5.getX() + pos5.getY());
        assertEquals(10, pos6.getX() + pos6.getY());
        assertEquals(12, pos7.getX() + pos7.getY());
        assertEquals(14, pos8.getX() + pos8.getY());
    }

    @Test
    public void testIsOnSameLineAs() {
        Position pos1 = new Position("A1");
        Position pos2 = new Position("B1");
        assertTrue(pos1.isOnSameLineAs(pos2));
    }

    @Test
    public void testIsOnSameColumnAs() {
        Position pos1 = new Position("A1");
        Position pos2 = new Position("A8");
        assertTrue(pos1.isOnSameColumnAs(pos2));
    }

    @Test
    public void testIsOnSameDiagonalAs() {
        Position pos1 = new Position("A1");
        Position pos2 = new Position("B2");
        Position pos3 = new Position("A5");
        Position pos4 = new Position("C3");
        Position pos5 = new Position("C2");
        Position pos6 = new Position("D8");

        assertTrue(pos1.isOnSameDiagonalAs(pos2));
        assertTrue(pos3.isOnSameDiagonalAs(pos4));
        assertFalse(pos2.isOnSameDiagonalAs(pos5));
        assertTrue(pos3.isOnSameDiagonalAs(pos6));
    }

    @Test
    public void testGetManhattanDistance() {
        Position pos1 = new Position("A1");
        Position pos2 = new Position("H8");
        assertEquals(14, pos1.getManhattanDistance(pos2));
    }

    @Test
    public void testToAlgebraicNotation() {
        Position pos1 = new Position(1,1);
        Position pos2 = new Position(2,2);
        assertEquals("A1", pos1.toAlgebraicNotation());
        assertEquals("B2", pos2.toAlgebraicNotation());
    }
}