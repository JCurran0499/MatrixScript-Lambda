package matrix;

import static org.junit.jupiter.api.Assertions.*;

import resources.matrix.Matrix;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class MatrixAdvancedTest {

    Matrix m;
    Matrix m2;

    @Test
    public void transpose() {
        m = new Matrix(new int[][] {
            {1, 0, 4, 5, 6},
            {12, 13, 5, 0, 7},
            {0, 0, 65, 56, 9}
        });
        assertTrue(m.transpose().equals(new Matrix("1 12 0 ; 0 13 0 ; 4 5 65 ; 5 0 56 ; 6 7 9")));

        m = new Matrix("5");
        assertTrue(m.transpose().equals(new Matrix("5")));

        m = new Matrix(new int[][] {{1, 2, 3, 4, 5}});
        assertTrue(m.transpose().equals(new Matrix("1.0; 2.0; 3.0; 4.0; 5.0")));
    }

    @Test
    public void transposeReflexive() {
        m = Matrix.Identity(6);
       assertTrue(m.transpose().equals(m));

        m = new Matrix("5 6 7 ; 0 1 2 ; 4 5 0 ; 0 0 5 ; 11 12 13 ; 0 1 0");
        assertTrue(m.transpose().transpose().equals(m));
    }


    private boolean arraysEqual(Object[][] a1, Object[][] a2) {
        if (a1.length != a2.length)
            return false;

        for (int i = 0; i < a1.length; i++) {
            if (!Arrays.equals(a1[i], a2[i]))
                return false;
        }

        return true;
    }

    @Test
    public void mapToArray() {
        m = new Matrix("1 0 4 5.6 ; 12 13.05 5 0.1");

        assertTrue(arraysEqual(m.mapToArray(0, (d) -> (int) d), new Integer[][] {
            {1, 0, 4, 5},
            {12, 13, 5, 0},
        }));
        assertTrue(arraysEqual(m.mapToArray(0.0, (d) -> d), new Double[][] {
            {1.0, 0.0, 4.0, 5.6},
            {12.0, 13.05, 5.0, 0.1},
        }));
        assertTrue(arraysEqual(m.mapToArray("", (d) -> Double.valueOf(d).toString()), new String[][] {
            {"1.0", "0.0", "4.0", "5.6"},
            {"12.0", "13.05", "5.0", "0.1"},
        }));
        assertTrue(arraysEqual(m.mapToArray("", (d) ->

            (d % 1 == 0)
                ? Integer.valueOf((int) d).toString()
                : Double.valueOf(d).toString()

        ), new String[][] {
            {"1", "0", "4", "5.6"},
            {"12", "13.05", "5", "0.1"},
        }));
    }

    @Test
    public void augment() {
        m = new Matrix(new int[][] {
            {1, 0, 4, 5},
            {12, 13, 5, 0},
        });
        m2 = new Matrix(new int[][] {
            {2, 3},
            {4, 1},
        });

        assertTrue(m.augment(m2).equals(new Matrix("1 0 4 5 2 3 ; 12 13 5 0 4 1")));
        assertTrue(m2.augment(m).equals(new Matrix("2 3 1 0 4 5 ; 4 1 12 13 5 0")));
    }

    @Test
    public void augmentUnhappyPath() {
        m = new Matrix(new int[][] {
            {1, 0, 4, 5},
            {12, 13, 5, 0},
        });
        m2 = new Matrix(new int[][] {
            {2, 3},
            {4, 1},
            {0, 5}
        });
        assertNull(m.augment(null));
        assertNull(m.augment(m2));
        assertNull(m2.augment(m));
    }

    @Test
    public void append() {
        m = new Matrix(new int[][] {
            {1, 0},
            {12, 13},
        });
        m2 = new Matrix(new int[][] {
            {2, 3},
            {4, 1},
            {4, 5}
        });

        assertTrue(m.append(m2).equals(new Matrix("1 0 ; 12 13 ; 2 3 ; 4 1 ; 4 5")));
        assertTrue(m2.append(m).equals(new Matrix("2 3 ; 4 1 ; 4 5 ; 1 0 ; 12 13")));
    }

    @Test
    public void appendUnhappyPath() {
        m = new Matrix(new int[][] {
            {1, 0, 4, 5},
            {12, 13, 5, 0},
        });
        m2 = new Matrix(new int[][] {
            {2, 3, 4},
            {4, 1, 5},
        });
        assertNull(m.append(null));
        assertNull(m.append(m2));
        assertNull(m2.append(m));
    }
}
