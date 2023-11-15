package matrix;

import static org.junit.jupiter.api.Assertions.*;

import resources.matrix.Matrix;
import org.junit.jupiter.api.Test;


public class MatrixArithmeticTest {

    Matrix m;
    Matrix m2;

    @Test
    public void add() {
        m = new Matrix(new double[][] {
            {1, 3.5, -3},
            {5.3, 9, 10}
        });
        m2 = new Matrix(new double[][] {
            {-1, 4, 10},
            {1.2, -8, 7}
        });
        assertTrue(m.add(m2).equals(new Matrix("0 7.5 7 ; 6.5 1 17")));

        m = new Matrix("5");
        m2 = new Matrix("-7");
        assertTrue(m.add(m2).equals(new Matrix("-2")));

        assertTrue(m.add(m2).equals(m2.add(m)));
    }

    @Test
    public void addChain() {
        m = new Matrix(new double[][] {
            {1, 3.5, -3},
            {5.3, 9, 10}
        });
        m2 = new Matrix(new int[][] {
            {-1, 4, 10},
            {1, -8, 7}
        });
        assertTrue(m.add(m2).add(m2).add(m).equals(new Matrix("0 15 14 ; 12.6 2 34")));
    }

    @Test
    public void addUnhappyPath() {
        m = new Matrix(new double[][] {
            {1, 3.5, -3},
            {5.3, 9, 10}
        });

        assertNull(m.add(null));
        assertNull(m.add(new Matrix("1 2 3 4 ; 5 6 7 8")));
        assertNull(m.add(new Matrix("1 2 3 ; 4 5 6 ; 7 8 9")));
    }

    @Test
    public void subtract() {
        m = new Matrix(new double[][] {
            {1, 3.5, -3},
            {5.3, 9, 10}
        });
        m2 = new Matrix(new double[][] {
            {-1, 4, 10},
            {1.2, -8, 7}
        });
        assertTrue(m.subtract(m2).equals(new Matrix("2 -0.5 -13 ; 4.1 17 3")));

        m = new Matrix("-5");
        m2 = new Matrix("-7");
        assertTrue(m.subtract(m2).equals(new Matrix("2")));

        assertFalse(m.subtract(m2).equals(m2.subtract(m)));
    }

    @Test
    public void subtractChain() {
        m = new Matrix(new double[][] {
            {1, 3.5, -3},
            {5.3, 9, 10}
        });
        m2 = new Matrix(new int[][] {
            {-1, 4, 10},
            {1, -8, 7}
        });
        assertTrue(m.subtract(m2).subtract(m2).subtract(m).equals(new Matrix("2 -8 -20 ; -2 16 -14")));
    }

    @Test
    public void subtractUnhappyPath() {
        m = new Matrix(new double[][] {
            {1, 3.5, -3},
            {5.3, 9, 10}
        });

        assertNull(m.subtract(null));
        assertNull(m.subtract(new Matrix("1 2 3 4 ; 5 6 7 8")));
        assertNull(m.subtract(new Matrix("1 2 3 ; 4 5 6 ; 7 8 9")));
    }

    @Test
    public void addingNegatives() {
        m = new Matrix(new double[][] {
            {1, 3.5, -3},
            {5.3, 9, 10}
        });
        assertTrue(m.add(new Matrix("1 2 3 ; 4 5 6"))
            .equals(m.subtract(new Matrix("-1 -2 -3 ; -4 -5 -6")))
        );
    }

    @Test
    public void multiplyScalar() {
        m = new Matrix(new int[][] {
            {1, 3, -3},
            {5, 9, 10}
        });
        assertTrue(m.multiply(6).equals(new Matrix("6 18 -18 ; 30 54 60")));
        assertTrue(m.multiply(0.555).equals(new Matrix("0.555 1.665 -1.665 ; 2.775 4.995 5.55")));
        assertTrue(m.multiply(-1).equals(new Matrix("-1 -3 3 ; -5 -9 -10")));
        assertTrue(m.multiply(0).equals(new Matrix("0 0 0 ; 0 0 0")));
    }

    @Test
    public void multiplyMatrix() {
        m = new Matrix(new int[][] {
            {1, 3, -3},
            {5, 9, 10},
            {0 , -5, 9}
        });
        m2 = new Matrix(new int[][] {
            {-8, 32, 2},
            {6, -7, -10},
            {11, 0, 13}
        });
        assertTrue(m.multiply(m2).equals(new Matrix("-23 11 -67 ; 124 97 50 ; 69 35 167")));

        m = new Matrix(new int[][] {
            {1, 3, -3},
            {5, 9, 10}
        });
        m2 = new Matrix(new int[][] {
            {1, 3, 7, 8},
            {5, 9, 10, -4},
            {23, -8, 9, 0}
        });
        assertTrue(m.multiply(m2).equals(new Matrix("-53 54 10 -4 ; 280 16 215 4")));

        m = new Matrix(new double[][] {
            {1.463, 0.33, -3.892215, 4.665, 0.007},
            {5.6748932, 9.91, 4.45, 78.202, 1.005363},
            {0.001, 0.555, 5.58382, 12.99, 5.67890}
        });
        m2 = new Matrix(new double[][] {
            {1.11, 5.6789},
            {5.9901, 9.5737},
            {23, -8.2323},
            {0.001, 45.4545},
            {1.1, 12.12}
        });
        assertTrue(m.multiply(m2).equals(new Matrix(
            "-85.907917 255.6395157445 ; 169.195123752 3657.28659155348 ; 138.0132555 618.633624014"
        )));
    }

    @Test
    public void multiplyMatrixUnhappyPath() {
        m = new Matrix(new int[][] {
            {1, 3, -3, 5},
            {5, 9, 10, 6},
            {0 , -5, 9, 7}
        });
        assertNull(m.multiply(null));
        assertNull(m.multiply(m));
        assertNull(m.multiply(new Matrix("0 ; 1 ; 2 ; 3 ; 4")));
        assertNull(m.multiply(new Matrix("0 1 2 3 ")));
    }

    @Test
    public void divide() {
        m = new Matrix("20");
        assertTrue(m.divide(4).equals(new Matrix("5")));

        m = new Matrix(new int[][] {
            {9, 3, -3, 6},
            {15, 39, 30, 36},
            {300 , -15, 9, 27}
        });
        assertTrue(m.divide(3).equals(new Matrix("3 1 -1 2 ; 5 13 10 12 ; 100 -5 3 9")));

        m = new Matrix(new int[][] {
            {11, 13, -13, 15},
            {115, 29, 110, 46},
            {90 , -75, 19, 37}
        });
        assertTrue(m.divide(4).equals(new Matrix(
            "2.75 3.25 -3.25 3.75 ; 28.75 7.25 27.5 11.5 ; 22.5 -18.75 4.75 9.25"
        )));

        m = new Matrix(new double[][] {
            {10.3, 11.5},
            {115, 22.9},
        });
        assertTrue(m.divide(5.7464873).equals(
            new Matrix("1.79239933 2.00122256 ; 20.01222556 3.98504318"), 8)
        );
    }

    @Test
    public void divideByZeroUnhappyPath() {
        m = new Matrix(new int[][] {
            {9, 3, -3, 6},
            {15, 39, 30, 36},
            {300 , -15, 9, 27}
        });
        assertNull(m.divide(0));
    }

    @Test
    public void toPower() {
        m = new Matrix("5");
        assertTrue(m.toPower(3).equals(new Matrix("125")));

        m = new Matrix(new double[][] {
            {9.3, 3, -3.07},
            {15, 39.2, 30},
            {0, -5.5, 9}
        });
        assertTrue(m.toPower(0).equals(new Matrix("1 0 0 ; 0 1 0 ; 0 0 1")));
        assertTrue(m.toPower(0).equals(Matrix.Identity(3)));
        assertTrue(m.toPower(1).equals(m));

        assertTrue(m.toPower(2).equals(new Matrix(
            "131.49 162.385 33.819 ; 727.5 1416.64 1399.95 ; -82.5 -265.1 -84"
        )));
        assertTrue(m.toPower(3).equals(new Matrix(
            "3658.632 6573.9575 4772.2467 ; 28015.35 50015.063 52865.325 ; -4743.75 -10177.42 -8455.725"
        )));
        m2 = m.toPower(8);
        assertTrue(m.toPower(8).equals(new Matrix(
            "217580457549.32486751 373396170040.64435134 404621688507.62622173 ; " +
                "1534870613436.2303025 2628013832361.9775771 2853186076928.4525558 ; " +
                "-324537690651.78318 -556295137780.24878065 -603572755441.4520075"
        ), 3));
    }

    @Test
    public void toPowerUnhappyPath() {
        m = new Matrix(new int[][] {
            {1, 2, 3, 4},
            {0, 1, 2, 3},
            {4, 3, 2, 1},
        });
        assertNull(m.toPower(2));

        m = new Matrix(new int[][] {
            {1, 2, 3},
            {0, 1, 2},
            {4, 3, 2},
            {4, 3, 2},
        });
        assertNull(m.toPower(2));

        m = new Matrix("1 2 ; 3 4");
        assertNotNull(m.toPower(2));
        assertNull(m.toPower(-1));
    }

    @Test
    public void toPowerMultiplicationChain() {
        m = new Matrix(new int[][] {
            {1, 2, 3, 4},
            {0, 1, 2, 3},
            {4, 3, 2, 1},
            {0, 2, 4, 2}
        });
        assertTrue(m.toPower(5).equals(
           m.multiply(m).multiply(m).multiply(m).multiply(m)
        ));
    }
}
