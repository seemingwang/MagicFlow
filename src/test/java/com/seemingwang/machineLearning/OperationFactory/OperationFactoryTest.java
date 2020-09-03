package com.seemingwang.machineLearning.OperationFactory;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowNodeBuilder;
import com.seemingwang.machineLearning.Utils.DataTransformer;
import org.junit.Assert;
import org.junit.Test;

public class OperationFactoryTest {

    @Test
    public void TestAddFunctionForMatrix() throws Exception {
        double []arr  = new double[6],arr1 = new double[6],ans = new double[6];
        int ind = 0;
        double v = 1.0;
        for(int i = 0;i < 2;i++){
            for(int j = 0;j < 3;j++){
                arr[ind] = arr1[ind] = v;
                ans[ind++] = v + v;
                ind++;
                v+=1.0;

            }
        }
        FlowNode fn = new FlowNodeBuilder().setShape(new Integer[]{2,3}).build(),fn1 = new FlowNodeBuilder().setShape(new Integer[]{2,3}).build();
        fn.setData(arr);
        fn1.setData(arr1);
        FlowNode e = OperationFactory.add(fn, fn1);
        e.getOp().forward(e);
        double data[] = e.getData();
        Assert.assertEquals(data,ans);
    }

    @Test
    public void TestAddBetweenScalaAndMatrix() throws Exception {
        FlowNode fn = new FlowNodeBuilder().setShape(new Integer[]{2,3}).build(),fn1 = new FlowNodeBuilder().setShape(new Integer[]{}).build();
        double [] x = new double[]{1,2,3},y = new double[]{1.7,2.7,3.7};
        fn1.resetDataSize(1);
        fn1.data[0] = 0.7;
        fn.setData(x);
        FlowNode add = OperationFactory.add(fn,fn1);
        add.getOp().forward(add);
        Assert.assertEquals(add.data,y);
    }

    @Test
    public void TestMultiply() throws Exception {
        double [][] mat1 = {{1,2},{3,4}},mat2 = {{2,5,1},{6,2,1}};
        FlowNode f1 = new FlowNodeBuilder().setShape(new Integer[]{2,2}).build(),f2 = new FlowNodeBuilder().setShape(new Integer[]{2,3}).build();
        f1.setData(DataTransformer.MatrixToArr(mat1));
        f2.setData(DataTransformer.MatrixToArr(mat2));
        FlowNode f3 = OperationFactory.matMultiply(f1,f2);
        f3.getOp().forward(f3);
        double [][] dev = {{1,2,2},{3,1,1}},dev1 = {{14,12},{12,21}},dev2 = {{10,5,5},{14,8,8}};
        f3.dev = DataTransformer.MatrixToArr(dev);
        f3.getOp().backward(f3);
        Assert.assertEquals(f1.dev,DataTransformer.MatrixToArr(dev1));
        Assert.assertEquals(f2.dev,DataTransformer.MatrixToArr(dev2));
    }
}