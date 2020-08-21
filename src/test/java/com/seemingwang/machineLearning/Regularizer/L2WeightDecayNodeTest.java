package com.seemingwang.machineLearning.Regularizer;

import com.seemingwang.machineLearning.DataInitializer.AveDataInitializer;
import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowNodeBuilder.ScalaFlowNodeBuilder;
import com.seemingwang.machineLearning.FlowNode.ScalaFlowNode;
import com.seemingwang.machineLearning.GraphManager.GraphManager;
import com.seemingwang.machineLearning.OperationFactory.OperationFactory;
import com.seemingwang.machineLearning.Optimizer.GradientDescentOptimizer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class L2WeightDecayNodeTest {

    @Test
    public void testMakeRegularizationNode(){
        ScalaFlowNode x = new ScalaFlowNodeBuilder().setTrainable(true).setName("x").build();
        ScalaFlowNode const2 = new ScalaFlowNodeBuilder().setName("const2").build(),const3 = new ScalaFlowNodeBuilder().setName("const3").build();
        try {
            FlowNode out = OperationFactory.averageSum(OperationFactory.pow(OperationFactory.subtract(OperationFactory.Multiply(const2,x),const3),2));
//            FlowNode mul = OperationFactory.Multiply(const2,x);
//            mul.setName("mul");
//            FlowNode sub = OperationFactory.subtract(mul,const3);
//            sub.setName("sub");
//            FlowNode pow = OperationFactory.pow(sub,2);
//            pow.setName("pow");
//            FlowNode out = OperationFactory.averageSum(pow);
//            out.setName("out");
            FlowNode decay = L2WeightDecayNode.makeRegularizationNode((ScalaFlowNode)out,1.0);
            FlowNode sum = OperationFactory.add(out,decay);
            GraphManager gm = new GraphManager().setOptimizer(new GradientDescentOptimizer(0.01)).setInitializer(new AveDataInitializer());
            gm.setOptimizeNode((ScalaFlowNode) sum);
            const2.setData(Arrays.asList(4.0,2.0,3.0));
            const3.setData(Arrays.asList(4.0,5.0,8.0));
            out.setName("out");
            decay.setName("decay");
            sum.setName("sum");
            for(int i = 0;i < 1000;i++) {
                gm.run();
                if(i % 10 == 0){
                    System.out.println("step " + i + " " + gm.getCost());
                }
            }
            Assert.assertEquals(x.getData().get(0),100.0/61,0.01);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}