package com.seemingwang.machineLearning.Regularizer;

import com.seemingwang.machineLearning.DataInitializer.RangeDataInitializer;
import com.seemingwang.machineLearning.DataProvider.DataProvider;
import com.seemingwang.machineLearning.DataProvider.TwoDArrayDataProvider;
import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowNodeBuilder;
import com.seemingwang.machineLearning.GraphManager.GraphManager;
import com.seemingwang.machineLearning.OperationFactory.OperationFactory;
import com.seemingwang.machineLearning.Optimizer.GradientDescentOptimizer;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class L2WeightDecayNodeTest {

    @Test
    public void testMakeRegularizationNode(){
        FlowNode x = new FlowNodeBuilder().setShape(new Integer[]{}).setTrainable(true).setName("x").build();
        FlowNode const2 = new FlowNodeBuilder().setShape(new Integer[]{null,1}).setName("const2").build(),const3 = new FlowNodeBuilder().setName("const3").setShape(new Integer[]{null,1}).build();
        try {
            FlowNode squareError = OperationFactory.pow(OperationFactory.subtract(OperationFactory.multiply(const2,x),const3),2);
            squareError.setName("squareError");
            FlowNode out = OperationFactory.reduceSum(squareError,-1,true);
            FlowNode decay = L2WeightDecayNode.makeRegularizationNode(out,1.0);
            FlowNode sum = OperationFactory.add(out,decay);
            GraphManager gm = new GraphManager().setOptimizer(new GradientDescentOptimizer(0.01)).setInitializer(new RangeDataInitializer(0,2));
            gm.setOptimizeNode(sum);
            Map<FlowNode,Object> m = new HashMap<>();
            m.put(const2,new TwoDArrayDataProvider(new double[][]{{4.0},{2.0},{3.0}}));
            m.put(const3,new TwoDArrayDataProvider(new double[][]{{4.0},{5.0},{8.0}}));
            gm.feed(m);
            out.setName("out");
            decay.setName("decay");
            sum.setName("sum");
            for(int i = 0;i < 1000;i++) {
                gm.run();
                if(i % 10 == 0){
                    System.out.println("step " + i + " " + gm.getCost());
                }
            }
            Assert.assertEquals(x.getData()[0],100.0/61,0.01);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}