package com.seemingwang.machineLearning.SpeedUp;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowNodeBuilder;
import com.seemingwang.machineLearning.FlowNode.SwitchFlowNode;
import com.seemingwang.machineLearning.Sequential.Sequential;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class BatchNormalizationTest {
    @Test
    public void testBatchNorm() throws Exception {
        FlowNode f = new FlowNodeBuilder().setShape(new Integer[]{3}).build();
        SwitchFlowNode train = new SwitchFlowNode();
        BatchNormalization bn = new BatchNormalization(f,train,0.1,0,0.01);
        FlowNode bnNode = bn.normalize();
        List<FlowNode> l = Sequential.arrange(true,bnNode);
        f.setData(new double[]{9.0,2.0,3.0});
        train.setBinarySwitch(true);
        for(FlowNode x:l){
            if(x.getOp() != null)
                x.getOp().forward(x);
        }
        double res[] = {1.401093,-0.862211,-0.538882};
        for(int i = 0;i < 3;i++)
            Assert.assertEquals(res[i],bnNode.data[i],1e-4);
        List<FlowNode> l1 = Sequential.arrange(false,bnNode);
        bnNode.dev = new double[]{3.0,1.0,1.0};
        for(FlowNode x:l1){
            if(x.getOp() != null)
                x.getOp().backward(x);
        }
        Assert.assertEquals(f.dev[0],0.00796210,1e-7);
        Assert.assertEquals(f.dev[1],0.0448431921,1e-7);
        Assert.assertEquals(f.dev[2],-0.05280529,1e-7);
    }

}