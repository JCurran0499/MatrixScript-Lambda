package interpreters;

import app.parser.interpreters.interpreters.arithmetic.Add;
import app.parser.interpreters.primitives.Num;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArithmeticTest {

    Add a;

    @Test
    public void simpleAddition() {
        a = new Add(new Num(5), new Num(7.5));
        assertTrue(a.solve().equals(new Num(12.5)));
    }
}
