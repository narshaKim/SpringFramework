import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class CalcSumTest {

    Calculator calculator;
    String numFilepath;

    @Before
    public void setUp() {
        calculator = new Calculator();
        numFilepath = getClass().getResource("numbers.txt").getPath();
    }

    @Test
    public void sumOfNumbers() throws IOException {
        int sum = calculator.calcSum(numFilepath);
        Assert.assertThat(sum, CoreMatchers.is(10));
    }

    @Test
    public void multiplyOfNumbers() throws IOException {
        int multiply = calculator.calcMultiply(numFilepath);
        Assert.assertThat(multiply, CoreMatchers.is(24));
    }

    @Test
    public void concatenateOfNumbers() throws IOException {
        String concat = calculator.concatenate(numFilepath);
        Assert.assertThat(concat, CoreMatchers.is("1234"));
    }

}
