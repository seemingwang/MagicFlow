package com.seemingwang.machineLearning.OperationFactory;

import com.seemingwang.machineLearning.DerivativeDescriber.MatrixTypeDevDescriber;
import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.Matrix.FullMatrix;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class OperationFactoryTest {

    @Test
    public void TestAddFunctionForMatrix() throws Exception {
        FullMatrix f = new FullMatrix(2,3),f1 = new FullMatrix(2,3);
        FullMatrix ans = new FullMatrix(2,3);
        double v = 1.0;
        for(int i = 0;i < 2;i++){
            for(int j = 0;j < 3;j++){
                f.set(i,j,v);
                f1.set(i,j,v);
                ans.set(i,j,v + v);
                v+=1.0;

            }
        }
        FullMatrixFlowNode fn = new FullMatrixFlowNode(2,3),fn1 = new FullMatrixFlowNode(2,3);
        fn.setData(Arrays.asList(f));
        fn1.setData(Arrays.asList(f1));
        FullMatrixFlowNode e = (FullMatrixFlowNode)OperationFactory.add(fn, fn1);
        e.getOp().forward(e);
        Assert.assertEquals(e.getData().get(0),ans);
    }

    @Test
    public void TestAddBetweenScalaAndMatrix() throws Exception {
        ScalaFlowNode a = new ScalaFlowNode();
        a.setData(Arrays.asList(1.0));
        FullMatrixFlowNode b = new FullMatrixFlowNode(1,1);
        FullMatrix f = new FullMatrix(1,1);
        f.set(0,0,1.2);
        b.setData(Arrays.asList(f));
        FlowNode c = OperationFactory.add(a, b);
        c.getOp().forward(c);
        Assert.assertEquals(c.getData().get(0),2.2);
        FlowNode d = OperationFactory.add(b,a);
        d.getOp().forward(d);
        Assert.assertEquals(d.getData().get(0),2.2);
    }

    @Test
    public void TestMultiply() throws Exception {
        double [][] mat1 = {{1,2},{3,4}},mat2 = {{2,5,1},{6,2,1}};
        FullMatrixFlowNode f1 = new FullMatrixFlowNode(2,2),f2 = new FullMatrixFlowNode(3,2);
        f1.setData(Arrays.asList(new FullMatrix(mat1)));
        f2.setData(Arrays.asList(new FullMatrix(mat2)));
        FullMatrixFlowNode f3 = (FullMatrixFlowNode)OperationFactory.Multiply(f1,f2);
        f3.getOp().forward(f3);
        double [][] dev = {{1,2,2},{3,1,1}},dev1 = {{14,12},{12,21}},dev2 = {{10,5,5},{14,8,8}};
        FullMatrix devM = new FullMatrix(dev),dev1M = new FullMatrix(dev1), dev2M = new FullMatrix(dev2);
        List<List<MatrixTypeDevDescriber>> l = f3.getOp().backward(f3,Arrays.asList(devM));
        Assert.assertEquals(l.get(0).get(0).devData,dev1M);
        Assert.assertEquals(l.get(1).get(0).devData,dev2M);
    }
}