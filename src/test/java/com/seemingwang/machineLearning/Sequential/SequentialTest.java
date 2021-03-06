package com.seemingwang.machineLearning.Sequential;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowNodeBuilder;
import com.seemingwang.machineLearning.OperationFactory.OperationFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class SequentialTest {
    @Test
    public void TestArrange() throws Exception {
        FlowNode a = new FlowNodeBuilder().setShape(new Integer[]{}).build(),b = new FlowNodeBuilder().setShape(new Integer[]{}).build(),d = new FlowNodeBuilder().setShape(new Integer[]{}).build();
        FlowNode c = OperationFactory.add(a,b);
        FlowNode e = OperationFactory.multiply(c,d);
        FlowNode g = OperationFactory.multiply(e,b);
        List<FlowNode> l = Sequential.arrange(true,g);
        List<FlowNode> l1 = Arrays.asList(b,a,d,c,e,g);
        ArrayList<FlowNode> forward = new ArrayList<>(l1);
        Assert.assertEquals(l,forward);
        l = Sequential.arrange(false,g);
        Collections.reverse(forward);
        Assert.assertEquals(l,forward);
    }
}