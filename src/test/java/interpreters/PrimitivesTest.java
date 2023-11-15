package interpreters;

import app.parser.interpreters.primitives.*;
import org.junit.jupiter.api.Test;
import resources.matrix.Matrix;

import static org.junit.jupiter.api.Assertions.*;

public class PrimitivesTest {

    Bool b;
    Err e;
    Mat m;
    Num n;
    Range r;
    Tuple t;

    Mat m2;

    @Test
    public void boolBasic() {
        assertTrue(Bool.of(true).bool());
        assertFalse(Bool.of(false).bool());

        assertTrue(Bool.of(false).not().bool());
        assertFalse(Bool.of(true).not().bool());
    }

    @Test
    public void boolComparison() {
        b = Bool.of(true);
        assertTrue(b.equals(Bool.of(true)));
        assertFalse(b.equals(Bool.of(false)));
        assertFalse(b.equals(Bool.of(true).not()));

        assertFalse(b.equals(new Err("test error")));
        assertFalse(b.equals(new Mat(new Matrix("5 6 7"))));
        assertFalse(b.equals(Null.instance()));
        assertFalse(b.equals(new Num(5)));
    }

    @Test
    public void boolString() {
        assertEquals(Bool.of(true).string(), "true");
        assertEquals(Bool.of(false).string(), "false");
        assertEquals(Bool.of(true).not().string(), "false");
    }

    @Test
    public void errComparison() {
        e = new Err("this is a test error message");
        assertTrue(e.equals(new Err("this is a test error message")));
        assertFalse(e.equals(new Err("different message")));
        assertFalse(e.equals(new Err("This is a test error message")));

        assertFalse(e.equals(new Num(3)));
        assertFalse(e.equals(Null.instance()));
    }

    @Test
    public void errString() {
        e = new Err("this is another test error message");
        assertEquals(e.string(), "Error: this is another test error message");
    }

    @Test
    public void matConstructorUnhappyPath() {
        m = new Mat(null);
        assertTrue(Err.is(m.solve()));

        m = new Mat(new Matrix("5 6 ; 7 8").add(new Matrix("5 ; 3")));
        assertTrue(Err.is(m.solve()));
    }

    @Test
    public void matComparison() {
        m = new Mat(new Matrix("5 6 7 ; 1 2 3"));
        assertTrue(m.equals(new Mat(new Matrix("5.0 6.00 7 ; 1 2 3.0000"))));

        assertFalse(m.equals(new Mat(new Matrix("1 2 3 ; 4 4 4"))));
        assertFalse(m.equals(new Err("error message")));
        assertFalse(m.equals(new Num(5)));
    }

    @Test
    public void matString() {
        m = new Mat(new Matrix(new int[][] {
            {1, 2, 375},
            {50, 0, 3},
            {1, 0, 0}
        }));
        assertEquals(m.string(), "[  1  2  375 ]\n[ 50  0    3 ]\n[  1  0    0 ]");

        m = new Mat(new Matrix(new double[][] {
            {1.00000, 2.33, 37.5},
            {-5.0, -0.01, 3},
            {1, -0, 0}
        }));
        assertEquals(m.string(), "[  1   2.33  37.5 ]\n[ -5  -0.01     3 ]\n[  1      0     0 ]");

        m = new Mat(new Matrix(new double[][] {
            {1.1234567890123456789, 2, 3},
            {-5, 1, 3},
        }));
        assertEquals(m.string(), "[ 1.12346  2  3 ]\n[      -5  1  3 ]");
    }

    @Test
    public void matAdd() {
        m = new Mat(new Matrix(new double[][] {
            {1, 3.5, -3},
            {5.3, 9, 10}
        }));
        m2 = new Mat(new Matrix(new double[][] {
            {-1, 4, 10},
            {1.2, -8, 7}
        }));
        assertTrue(m.add(m2).equals(new Mat(new Matrix("0 7.5 7 ; 6.5 1 17"))));

        m = new Mat(new Matrix("5"));
        m2 = new Mat(new Matrix("-7"));
        assertTrue(m.add(m2).equals(new Mat(new Matrix("-2"))));

        assertTrue(m.add(m2).equals(m2.add(m)));
    }

    @Test
    public void matAddChain() {
        m = new Mat(new Matrix(new double[][] {
            {1, 3.5, -3},
            {5.3, 9, 10}
        }));
        m2 = new Mat(new Matrix(new int[][] {
            {-1, 4, 10},
            {1, -8, 7}
        }));
        assertTrue(Mat.cast(Mat.cast(m.add(m2)).add(m2)).add(m).equals(new Mat(new Matrix("0 15 14 ; 12.6 2 34"))));
    }

    @Test
    public void matAddUnhappyPath() {
        m = new Mat(new Matrix(new double[][] {
            {1, 3.5, -3},
            {5.3, 9, 10}
        }));

        assertTrue(Err.is(m.add(new Num(-3.2)).solve()));
        assertTrue(Err.is(m.add(new Range(5, 8)).solve()));
        assertTrue(Err.is(m.add(new Mat(new Matrix("1 2 3 4 ; 5 6 7 8"))).solve()));
        assertTrue(Err.is(m.add(new Mat(new Matrix("1 2 3 ; 4 5 6 ; 7 8 9"))).solve()));
    }

    @Test
    public void matSubtract() {
        m = new Mat(new Matrix(new double[][] {
            {1, 3.5, -3},
            {5.3, 9, 10}
        }));
        m2 = new Mat(new Matrix(new double[][] {
            {-1, 4, 10},
            {1.2, -8, 7}
        }));
        assertTrue(m.subtract(m2).equals(new Mat(new Matrix("2 -0.5 -13 ; 4.1 17 3"))));

        m = new Mat(new Matrix("-5"));
        m2 = new Mat(new Matrix("-7"));
        assertTrue(m.subtract(m2).equals(new Mat(new Matrix("2"))));

        assertFalse(m.subtract(m2).equals(m2.subtract(m)));
    }

    @Test
    public void matSubtractChain() {
        m = new Mat(new Matrix(new double[][] {
            {1, 3.5, -3},
            {5.3, 9, 10}
        }));
        m2 = new Mat(new Matrix(new int[][] {
            {-1, 4, 10},
            {1, -8, 7}
        }));
        assertTrue(Mat.cast(Mat.cast(m.subtract(m2)).subtract(m2)).subtract(m).equals(
            new Mat(new Matrix("2 -8 -20 ; -2 16 -14")))
        );
    }

    @Test
    public void matSubtractUnhappyPath() {
        m = new Mat(new Matrix(new double[][] {
            {1, 3.5, -3},
            {5.3, 9, 10}
        }));

        assertTrue(Err.is(m.subtract(new Num(-3.2)).solve()));
        assertTrue(Err.is(m.subtract(new Range(5, 8)).solve()));
        assertTrue(Err.is(m.subtract(new Mat(new Matrix("1 2 3 4 ; 5 6 7 8"))).solve()));
        assertTrue(Err.is(m.subtract(new Mat(new Matrix("1 2 3 ; 4 5 6 ; 7 8 9"))).solve()));
    }

    @Test
    public void matAddingNegatives() {
        m = new Mat(new Matrix(new double[][] {
            {1, 3.5, -3},
            {5.3, 9, 10}
        }));
        assertTrue(m.add(new Mat(new Matrix("1 2 3 ; 4 5 6"))).solve()
            .equals(m.subtract(new Mat(new Matrix("-1 -2 -3 ; -4 -5 -6"))).solve())
        );
    }

    @Test
    public void matMultiplyScalar() {
        m = new Mat(new Matrix(new int[][] {
            {1, 3, -3},
            {5, 9, 10}
        }));
        assertTrue(m.multiply(new Num(6)).solve().equals(new Mat(new Matrix("6 18 -18 ; 30 54 60")).solve()));
        assertTrue(m.multiply(new Num(0.555)).solve().equals(
            new Mat(new Matrix("0.555 1.665 -1.665 ; 2.775 4.995 5.55")).solve()));
        assertTrue(m.multiply(new Num(-1)).solve().equals(new Mat(new Matrix("-1 -3 3 ; -5 -9 -10")).solve()));
        assertTrue(m.multiply(new Num(0)).solve().equals(new Mat(new Matrix("0 0 0 ; 0 0 0")).solve()));
    }

    @Test
    public void matMultiplyMatrix() {
        m = new Mat(new Matrix(new int[][] {
            {1, 3, -3},
            {5, 9, 10},
            {0 , -5, 9}
        }));
        m2 = new Mat(new Matrix(new int[][] {
            {-8, 32, 2},
            {6, -7, -10},
            {11, 0, 13}
        }));
        assertTrue(m.multiply(m2).equals(new Mat(new Matrix("-23 11 -67 ; 124 97 50 ; 69 35 167"))));

        m = new Mat(new Matrix(new int[][] {
            {1, 3, -3},
            {5, 9, 10}
        }));
        m2 = new Mat(new Matrix(new int[][] {
            {1, 3, 7, 8},
            {5, 9, 10, -4},
            {23, -8, 9, 0}
        }));
        assertTrue(m.multiply(m2).equals(new Mat(new Matrix("-53 54 10 -4 ; 280 16 215 4"))));

        m = new Mat(new Matrix(new double[][] {
            {1.463, 0.33, -3.892215, 4.665, 0.007},
            {5.6748932, 9.91, 4.45, 78.202, 1.005363},
            {0.001, 0.555, 5.58382, 12.99, 5.67890}
        }));
        m2 = new Mat(new Matrix(new double[][] {
            {1.11, 5.6789},
            {5.9901, 9.5737},
            {23, -8.2323},
            {0.001, 45.4545},
            {1.1, 12.12}
        }));
        assertTrue(m.multiply(m2).equals(new Mat(new Matrix(
            "-85.907917 255.6395157445 ; 169.195123752 3657.28659155348 ; 138.0132555 618.633624014"
        ))));
    }

    @Test
    public void matMultiplyMatrixUnhappyPath() {
        m = new Mat(new Matrix(new int[][] {
            {1, 3, -3, 5},
            {5, 9, 10, 6},
            {0 , -5, 9, 7}
        }));
        assertTrue(Err.is(m.multiply(new Range(0, 1)).solve()));
        assertTrue(Err.is(m.multiply(m).solve()));
        assertTrue(Err.is(m.multiply(new Mat(new Matrix("0 ; 1 ; 2 ; 3 ; 4"))).solve()));
        assertTrue(Err.is(m.multiply(new Mat(new Matrix("0 1 2 3 "))).solve()));
    }

    @Test
    public void matDivide() {
        m = new Mat(new Matrix("20"));
        assertTrue(m.divide(new Num(4)).equals(new Mat(new Matrix("5"))));

        m = new Mat(new Matrix(new int[][] {
            {9, 3, -3, 6},
            {15, 39, 30, 36},
            {300 , -15, 9, 27}
        }));
        assertTrue(m.divide(new Num(3)).equals(new Mat(new Matrix("3 1 -1 2 ; 5 13 10 12 ; 100 -5 3 9"))));

        m = new Mat(new Matrix(new int[][] {
            {11, 13, -13, 15},
            {115, 29, 110, 46},
            {90 , -75, 19, 37}
        }));
        assertTrue(m.divide(new Num(4)).equals(new Mat(new Matrix(
            "2.75 3.25 -3.25 3.75 ; 28.75 7.25 27.5 11.5 ; 22.5 -18.75 4.75 9.25"
        ))));
    }

    @Test
    public void matDivideByZeroUnhappyPath() {
        m = new Mat(new Matrix(new int[][] {
            {9, 3, -3, 6},
            {15, 39, 30, 36},
            {300 , -15, 9, 27}
        }));
        assertTrue(Err.is(m.divide(new Num(0)).solve()));
    }

    @Test
    public void matToPower() {
        m = new Mat(new Matrix("5"));
        assertTrue(m.power(new Num(3)).equals(new Mat(new Matrix("125"))));

        m = new Mat(new Matrix(new double[][] {
            {9.3, 3, -3.07},
            {15, 39.2, 30},
            {0, -5.5, 9}
        }));
        assertTrue(m.power(new Num(0)).equals(new Mat(new Matrix("1 0 0 ; 0 1 0 ; 0 0 1"))));
        assertTrue(m.power(new Num(0)).equals(new Mat(Matrix.Identity(3))));
        assertTrue(m.power(new Num(1)).equals(m));

        assertTrue(m.power(new Num(2)).equals(new Mat(new Matrix(
            "131.49 162.385 33.819 ; 727.5 1416.64 1399.95 ; -82.5 -265.1 -84"
        ))));
        assertTrue(m.power(new Num(3)).equals(new Mat(new Matrix(
            "3658.632 6573.9575 4772.2467 ; 28015.35 50015.063 52865.325 ; -4743.75 -10177.42 -8455.725"
        ))));
    }

    @Test
    public void matToPowerUnhappyPath() {
        m = new Mat(new Matrix(new int[][] {
            {1, 2, 3, 4},
            {0, 1, 2, 3},
            {4, 3, 2, 1},
        }));
        assertTrue(Err.is(m.power(new Num(2)).solve()));

        m = new Mat(new Matrix(new int[][] {
            {1, 2, 3},
            {0, 1, 2},
            {4, 3, 2},
            {4, 3, 2},
        }));
        assertTrue(Err.is(m.power(new Num(2)).solve()));

        m = new Mat(new Matrix("1 2 ; 3 4"));
        assertFalse(Err.is(m.power(new Num(2)).solve()));
        assertTrue(Err.is(m.power(new Num(-1)).solve()));
    }

    @Test
    public void matToPowerMultiplicationChain() {
        m = new Mat(new Matrix(new int[][] {
            {1, 2, 3, 4},
            {0, 1, 2, 3},
            {4, 3, 2, 1},
            {0, 2, 4, 2}
        }));
        assertTrue(m.power(new Num(5)).equals(
            Mat.cast(Mat.cast(Mat.cast(m.multiply(m)).multiply(m)).multiply(m)).multiply(m)
        ));
    }

    @Test
    public void matGetBasic() {
        m = new Mat(new Matrix(new double[][] {
            {1, 2.3, 3, 4},
            {0, 1, 2, 3.1234},
            {4, 3.08, 2, 1},
        }));

    assertTrue(m.get(
        new Tuple(new Num(0), new Num(1))
    ).equals(new Num(2.3)));
    }



    @Test
    public void solveReflexive() {
        b = Bool.of(true);
        assertEquals(b.solve(), b);
        b = Bool.of(false);
        assertEquals(b.solve(), b);

        e = new Err("test error message");
        assertEquals(e.solve(), e);

        m = new Mat(new Matrix("1 2 3 ; 4 5 6"));
        assertEquals(m.solve(), m);

        n = new Num(5);
        assertEquals(n.solve(), n);

        assertEquals(Null.instance().solve(), Null.instance());

        r = new Range(0, 5);
        assertEquals(r.solve(), r);

        t = new Tuple(b, m, n, r);
        assertEquals(t.solve(), t);
    }
}
