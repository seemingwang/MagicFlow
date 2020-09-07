package com.seemingwang.machineLearning.Regularizer;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowNodeBuilder;
import org.junit.Assert;
import org.junit.Test;

public class DropoutNodeTest {

    @Test
    public void testDropout(){
        FlowNode c = new FlowNodeBuilder().setShape(new Integer[]{2,4}).build();
        double t[] = {0,1,2,3,4,5,6,7};
        c.setData(t);
        FlowNode output = DropoutNode.makeDropoutNode(c,0.8,0);
        DropoutNode d = (DropoutNode) output.getChildren().get(1);
        d.setBinarySwitch(true);
        d.setMaskLife(5);
        output.getOp().forward(output);
        d.setMask(new double[] {0.3,0.5,0.7,0.2});
        output.getOp().forward(output);
        for(int i = 0;i < 8;i++){
            Assert.assertEquals(output.data[i],t[i] * d.getMask()[i%4],1e-8);
        }
        output.dev = new double[]{0,1,2,3,4,5,6,7};
        output.getOp().backward(output);
        for(int i = 0;i < 8;i++){
            Assert.assertEquals(output.data[i],c.dev[i],1e-8);
        }
    }
}