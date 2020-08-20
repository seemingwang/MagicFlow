package com.seemingwang.machineLearning.Sequential;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.ScalaFlowNode;
import com.seemingwang.machineLearning.OperationFactory.OperationFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class SequentialTest {
    @Test
    public void TestArrange() throws Exception {
        ScalaFlowNode a = new ScalaFlowNode(),b = new ScalaFlowNode(),d = new ScalaFlowNode();
        ScalaFlowNode c = (ScalaFlowNode)OperationFactory.add(a,b);
        ScalaFlowNode e = (ScalaFlowNode)OperationFactory.Multiply(c,d);
        ScalaFlowNode g = (ScalaFlowNode)OperationFactory.Multiply(e,b);
        List<FlowNode> l = Sequential.arrange(true,g);
        List<FlowNode> l1 = Arrays.asList(b,a,d,c,e,g);
        ArrayList<FlowNode> forward = new ArrayList<>(l1);
        Assert.assertEquals(l,forward);
        l = Sequential.arrange(false,g);
        Collections.reverse(forward);
        Assert.assertEquals(l,forward);
    }
}