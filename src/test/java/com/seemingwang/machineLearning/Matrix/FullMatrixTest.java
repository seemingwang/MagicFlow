package com.seemingwang.machineLearning.Matrix;
import org.junit.Assert;
import org.junit.Test;
public class FullMatrixTest {
    @Test
    public void testMultiply() throws MatrixException {
       FullMatrix a = new FullMatrix(new double[][] {{1,2},{3,4}});
       FullMatrix b = new FullMatrix(new double[][] {{1,2,3},{4,5,6}});
       Matrix c = a.multiply(b);
       Assert.assertEquals(c.equals(new FullMatrix(new double[][]{{9,12,15},{19,26,33}})), true);
    }
}
