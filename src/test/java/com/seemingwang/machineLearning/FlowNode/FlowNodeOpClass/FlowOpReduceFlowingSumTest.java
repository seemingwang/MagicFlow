package com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowNodeBuilder;
import org.junit.Assert;
import org.junit.Test;

public class FlowOpReduceFlowingSumTest {

    @Test
    public void testReduceSum() throws Exception {
        FlowNode input0 = new FlowNodeBuilder().setShape(new Integer[]{3,2}).build();
        FlowNode output = new FlowNode();
        FlowOpReduceFlowingSum op = new FlowOpReduceFlowingSum(0,true);
        op.connect(input0,output);
        input0.setData(new double[]{0,1,2,3,4,5});
        output.getOp().forward(output);
        double [] ave = {2,3};
        for(int i = 0;i < 2;i++)
            Assert.assertEquals(ave[i], output.data[i],1e-6);
        output.dev = new double[]{1.5,3};
        output.getOp().backward(output);
        for(int i = 0;i < 6;i++){
            Assert.assertEquals(input0.dev[i], i % 2 == 0?0.5:1.0,1e-6);
        }
    }
}