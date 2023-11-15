package matrix;

import static org.junit.jupiter.api.Assertions.*;

import resources.matrix.exceptions.MatrixDimensionsException;
import resources.matrix.exceptions.MatrixNullException;
import resources.matrix.exceptions.MatrixOutOfBoundsException;
import resources.matrix.Matrix;
import org.junit.jupiter.api.Test;

public class MatrixBasicsTest {

    Matrix m;
    Matrix m2;

    @Test
    public void identityMatrix() {
        m = Matrix.Identity(5);
        assertEquals(m.rows(), m.cols());
        assertEquals(m.rows(), 5);
        assertEquals(m.size(), 25);
        assertTrue(m.isSquare());
        assertTrue(m.equals(
            new Matrix("1 0 0 0 0 ; 0 1 0 0 0 ; 0 0 1 0 0 ; 0 0 0 1 0 ; 0 0 0 0 1")
        ));
    }

    @Test
    public void identityMatrixUnhappyPath() {
        assertNull(Matrix.Identity(0));
        assertNull(Matrix.Identity(-1));
    }

    @Test
    public void zeroVector() {
        m = Matrix.ZeroVector(5);
        assertEquals(m.rows(), 5);
        assertEquals(m.cols(), 1);
        assertEquals(m.size(), 5);
        assertFalse(m.isSquare());
        assertTrue(m.equals(
            new Matrix("0 ; 0  ; 0  ; 0  ; 0")
        ));
    }

    @Test
    public void zeroVectorUnhappyPath() {
        assertNull(Matrix.ZeroVector(0));
        assertNull(Matrix.ZeroVector(-1));
    }

    @Test
    public void equals() {
        m = new Matrix("1 2 3 ; 52 6 2 ; 9 0 0");
        m2 = new Matrix("1 2 3 ; 52 6 2 ; 9 0 0");
        assertTrue(m.equals(m2));

        m2.setValue(0, 0, 0);
        assertFalse(m.equals(m2));

        m2 = new Matrix(" 0 0 0 ; 52 6 2 ; 9 0 0");
        assertFalse(m.equals(m2));

        m2 = new Matrix("1 2 ; 52 6 ; 9 0");
        assertFalse(m.equals(m2));

        assertFalse(m.equals(null));
    }

    @Test
    public void equalsHighScale() {
        m = new Matrix("1.00123456789 2.987654321  ; 52.5678904321 6.123456789");
        m2 = new Matrix("1.00123456789 2.987654321  ; 52.5678904321 6.123456789");
        assertTrue(m.equals(m2));

        m = new Matrix(new double[][] {{1/3}});
        m2 = new Matrix(new double[][] {{1/3}});
        assertTrue(m.equals(m2));
    }

    @Test
    public void equalsCustomScale() {
        m = new Matrix("125.0012345678956 2.98765432112  ; 52.567890432123 6.12345678945");
        m2 = new Matrix("125.001234567 2.98765432112  ; 52.567890432123 6.12345678945");
        assertFalse(m.equals(m2));
        assertFalse(m.equals(m2, 10));
        assertTrue(m.equals(m2, 8));
    }

    @Test
    public void isSquare() {
        m = new Matrix("-4.567");
        assertTrue(m.isSquare());

        m = new Matrix("1 2 ; 3 4");
        assertTrue(m.isSquare());

        m = new Matrix("-3 4 ; 5 6 ; 1 3");
        assertFalse(m.isSquare());
    }

    @Test
    public void isSymmetrical() {
        m = new Matrix("-0.7");
        assertTrue(m.isSymmetrical());

        m = new Matrix("0 1 2 ; 1 2 3 ; 2 3 4");
        assertTrue(m.isSymmetrical());

        assertTrue(Matrix.Identity(5).isSymmetrical());

        m = new Matrix("0 1 2 ; 1 2 3 ; 2 0 4");
        assertFalse(m.isSymmetrical());
    }

    @Test
    public void getValue() {
        m = new Matrix("1 5 6.3 0 ; 14 3 -6 9 ; 210.5364663 2 13 4");
        assertEquals(m.getValue(0, 1), 5);
        assertEquals(m.getValue(2, 3), 4);
        assertEquals(m.getValue(0, 2), 6.3);
        assertEquals(m.getValue(2, 0), 210.53647);
    }

    @Test
    public void getValueUnhappyPath() {
        m = new Matrix("1 5 6.3 0 ; 14 3 -6 9 ; 210.5364663 2 13 4");

        try {
            m.getValue(-1, 1);
            fail();
        } catch (MatrixOutOfBoundsException e) {}

        try {
            m.getValue(0, -1);
            fail();
        } catch (MatrixOutOfBoundsException e) {}

        try {
            m.getValue(0, 4);
            fail();
        } catch (MatrixOutOfBoundsException e) {}

        try {
            m.getValue(3, 1);
            fail();
        } catch (MatrixOutOfBoundsException e) {}
    }

    @Test
    public void getRow() {
        m = new Matrix(new double[][] {
            {5, 3, 2, 1.5},
            {3, 0.001, 0, -8},
            {4, 5, 6, -123},
            {4, 7, 8, 9},
            {12, -12, -5, 5}
        });
        assertTrue(m.getRow(1).equals(new Matrix("3  0.001  0  -8")));
        assertTrue(m.getRow(4).equals(new Matrix("12 -12 -5 5")));

        m = new Matrix("1 ; 2 ; 3");
        assertTrue(m.getRow(0).equals(new Matrix("1")));
    }

    @Test
    public void getRowUnhappyPath() {
        m = new Matrix(new double[][] {
            {5, 3, 2, 1.5},
            {3, 0.001, 0, -8},
            {4, 5, 6, -123},
            {4, 7, 8, 9},
            {12, -12, -5, 5}
        });

        assertNull(m.getRow(5));
        assertNull(m.getRow(7));
        assertNull(m.getRow(-1));
    }

    @Test
    public void getColumn() {
        m = new Matrix(new double[][] {
            {5, 3, 2, 1.5},
            {3, 0.001, 0, -8},
            {4, 5, 6, -123},
            {4, 7, 8, 9},
            {12, -12, -5, 5}
        });
        assertTrue(m.getColumn(1).equals(new Matrix("3 ; 0.001 ; 5 ; 7; -12")));
        assertTrue(m.getColumn(3).equals(new Matrix("1.5 ; -8 ; -123 ; 9; 5")));

        m = new Matrix("1  2  3");
        assertTrue(m.getColumn(0).equals(new Matrix("1")));
    }

    @Test
    public void getColumnUnhappyPath() {
        m = new Matrix(new double[][] {
            {5, 3, 2, 1.5},
            {3, 0.001, 0, -8},
            {4, 5, 6, -123},
            {4, 7, 8, 9},
            {12, -12, -5, 5}
        });

        assertNull(m.getColumn(4));
        assertNull(m.getColumn(7));
        assertNull(m.getColumn(-1));
    }

    @Test
    public void setValue() {
        m = new Matrix(new double[][] {
            {5, 3, 2, 1.5},
            {3, 0.001, 0, -8},
            {4, 5, 6, -123},
            {4, 7, 8, 9},
            {12, -12, -5, 5}
        });
        assertEquals(m.getValue(0, 0), 5);
        m.setValue(0, 0, 1.5);
        assertEquals(m.getValue(0, 0), 1.5);

        assertEquals(m.getValue(2, 3), -123);
        m.setValue(2, 3, 0);
        assertEquals(m.getValue(2, 3), 0);

        assertTrue(m.equals(new Matrix("1.5 3 2 1.5 ; 3 0.001 0 -8 ; 4 5 6 0 ; 4 7 8 9 ; 12 -12 -5 5")));
    }

    @Test
    public void setValueDifferentTypes() {
        m = new Matrix(new int[][] {
            {4, 5, 6, -123},
            {4, 7, 8, 9},
            {12, -12, -5, 5}
        });
        m.setValue(0, 0, 1.2305);
        assertEquals(m.getValue(0, 0), 1.2305);
    }

    @Test
    public void setValueUnhappyPath() {
        m = new Matrix("1 5 6.3 0 ; 14 3 -6 9 ; 210.5364663 2 13 4");

        try {
            m.setValue(-1, 1, 0);
            fail();
        } catch (MatrixOutOfBoundsException e) {}

        try {
            m.setValue(0, -1, 0);
            fail();
        } catch (MatrixOutOfBoundsException e) {}

        try {
            m.setValue(0, 4, 0);
            fail();
        } catch (MatrixOutOfBoundsException e) {}

        try {
            m.setValue(3, 1, 0);
            fail();
        } catch (MatrixOutOfBoundsException e) {}
    }

    @Test
    public void setRow() {
        m = new Matrix(new double[][] {
            {5, 3, 2, 1},
            {3, 0.001, 0, -8},
            {4, 5, 6, -123},
            {4, 7, 8, 9},
            {12, -12, -5, 5}
        });
        m.setRow(1, new Matrix("0 0 0 0"));
        assertEquals(m.rows(), 5);
        assertEquals(m.cols(), 4);
        assertTrue(m.equals(new Matrix(new int[][] {
            {5, 3, 2, 1},
            {0, 0, 0, 0},
            {4, 5, 6, -123},
            {4, 7, 8, 9},
            {12, -12, -5, 5}
        })));

        m = new Matrix(new int[][] {
            {0, 1, 2, 3},
            {1, 2, 3, 4},
            {2, 3, 4, 5},
            {0, 0, 0, 0}
        });
        assertFalse(m.isSymmetrical());
        m.setRow(3, new Matrix(new int[][] {{3, 4, 5, 6}}));
        assertTrue(m.isSymmetrical());

        m = new Matrix("5");
        assertEquals(m.size(), 1);
        m.setRow(0, new Matrix("6"));
        assertTrue(m.equals(new Matrix("6")));
    }

    @Test
    public void setRowNull() {
        m = new Matrix(new double[][] {
            {5, 3, 2, 1},
            {3, 0.001, 0, -8},
            {4, 5, 6, -123}
        });

        try {
            m.setRow(0, null);
            fail();
        } catch (MatrixNullException e) {}

        try {
            m.setRow(-1, null);
            fail();
        } catch (MatrixNullException e) {}
    }

    @Test
    public void setRowBoundsUnhappyPath() {
        m = new Matrix(new int[][] {
            {5, 3, 2, 1},
            {3, 0, 0, -8},
            {4, 5, 6, -123},
            {1, 7, 77, 777}
        });

        try {
            m.setRow(-1, new Matrix("0  0  0  0"));
            fail();
        } catch (MatrixOutOfBoundsException e) {}

        try {
            m.setRow(4, new Matrix("0 0 0 0"));
            fail();
        } catch (MatrixOutOfBoundsException e) {}

        try {
            m.setRow(6, new Matrix("0 0  0  0"));
            fail();
        } catch (MatrixOutOfBoundsException e) {}
    }

    @Test
    public void setRowDimensionsUnhappyPath() {
        m = new Matrix(new int[][] {
            {5, 3, 2, 1},
            {3, 0, 0, -8},
            {4, 5, 6, -123},
            {1, 7, 77, 777}
        });

        try {
            m.setRow(0, new Matrix("0  0  0  0  0"));
            fail();
        } catch (MatrixDimensionsException e) {}

        try {
            m.setRow (0, new Matrix("0  0 "));
            fail();
        } catch (MatrixDimensionsException e) {}

        try {
            m.setRow(0, new Matrix("0 0  0 0 ; 0 0  0 0"));
            fail();
        } catch (MatrixDimensionsException e) {}
    }

    @Test
    public void setColumn() {
        m = new Matrix(new double[][] {
            {5, 3, 2, 1},
            {3, 0.001, 0, -8},
            {4, 5, 6, -123},
            {4, 7, 8, 9},
            {12, -12, -5, 5}
        });
        m.setColumn(1, new Matrix("0;0;0;0;0"));
        assertEquals(m.rows(), 5);
        assertEquals(m.cols(), 4);
        assertTrue(m.equals(new Matrix(new int[][] {
            {5, 0, 2, 1},
            {3, 0, 0, -8},
            {4, 0, 6, -123},
            {4, 0, 8, 9},
            {12, 0, -5, 5}
        })));

        m = new Matrix(new int[][] {
            {0, 1, 2, 0},
            {1, 2, 3, 0},
            {2, 3, 4, 0},
            {3, 4, 5, 0}
        });
        assertFalse(m.isSymmetrical());
        m.setColumn(3, new Matrix(new int[][] {{3}, {4}, {5}, {6}}));
        assertTrue(m.isSymmetrical());

        m = new Matrix("5");
        assertEquals(m.size(), 1);
        m.setColumn(0, new Matrix("6"));
        assertTrue(m.equals(new Matrix("6")));
    }

    @Test
    public void setColumnNull() {
        m = new Matrix(new double[][] {
            {5, 3, 2, 1},
            {3, 0.001, 0, -8},
            {4, 5, 6, -123}
        });

        try {
            m.setColumn(0, null);
            fail();
        } catch (MatrixNullException e) {}

        try {
            m.setColumn(-1, null);
            fail();
        } catch (MatrixNullException e) {}
    }

    @Test
    public void setColumnBoundsUnhappyPath() {
        m = new Matrix(new int[][] {
            {5, 3, 2, 1},
            {3, 0, 0, -8},
            {4, 5, 6, -123},
            {1, 7, 77, 777}
        });

        try {
            m.setColumn(-1, new Matrix("0 ; 0 ; 0 ; 0"));
            fail();
        } catch (MatrixOutOfBoundsException e) {}

        try {
            m.setColumn(4, new Matrix("0 ; 0 ; 0 ; 0"));
            fail();
        } catch (MatrixOutOfBoundsException e) {}

        try {
            m.setColumn(6, new Matrix("0 ; 0 ; 0 ; 0"));
            fail();
        } catch (MatrixOutOfBoundsException e) {}
    }

    @Test
    public void setColumnDimensionsUnhappyPath() {
        m = new Matrix(new int[][] {
            {5, 3, 2, 1},
            {3, 0, 0, -8},
            {4, 5, 6, -123},
            {1, 7, 77, 777}
        });

        try {
            m.setColumn(0, new Matrix("0 ; 0 ; 0 ; 0 ; 0"));
            fail();
        } catch (MatrixDimensionsException e) {}

        try {
            m.setColumn(0, new Matrix("0 ; 0 "));
            fail();
        } catch (MatrixDimensionsException e) {}

        try {
            m.setColumn(0, new Matrix("0 0 ; 0 0 ; 0 0 ; 0 0"));
            fail();
        } catch (MatrixDimensionsException e) {}
    }
}
