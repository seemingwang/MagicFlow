package com.seemingwang.machineLearning.models;

import com.seemingwang.machineLearning.DataInitializer.RangeDataInitializer;
import com.seemingwang.machineLearning.DataProvider.ArrayDataProvider;
import com.seemingwang.machineLearning.DataProvider.DataProvider;
import com.seemingwang.machineLearning.DataProvider.TwoDArrayDataProvider;
import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowNodeBuilder;
import com.seemingwang.machineLearning.GraphManager.GraphManager;
import com.seemingwang.machineLearning.OperationFactory.OperationFactory;
import com.seemingwang.machineLearning.Optimizer.GradientDescentOptimizer;
import com.seemingwang.machineLearning.Regularizer.L1WeightDecayNode;
import com.seemingwang.machineLearning.Regularizer.L2WeightDecayNode;
import com.seemingwang.machineLearning.Utils.Structure.WeightDecay;

import java.util.HashMap;
import java.util.Map;

public class LogisticRegression {
    public LogisticRegression(int dimension, WeightDecay w) {
        this.dimension = dimension;
        gm = new GraphManager().setInitializer(new RangeDataInitializer(0,3)).setOptimizer(new GradientDescentOptimizer(0.1));
        this.input = new FlowNodeBuilder().setShape(new Integer[]{null,dimension}).setName("input").build();
        this.weight = new FlowNodeBuilder().setShape(new Integer[]{dimension,1}).setTrainable(true).setName("weight").build();
        try {
            FlowNode out =  OperationFactory.matMultiply(input,weight);
            this.bias = new FlowNodeBuilder().setShape(new Integer[]{}).setTrainable(true).setName("bias").build();
            this.result = OperationFactory.sigmoid(OperationFactory.add(out,this.bias));
            result.setName("result");
            FlowNode label = new FlowNodeBuilder().setName("label").build();
            FlowNode diffSquare = OperationFactory.pow(OperationFactory.subtract(result,label),2);
            FlowNode averageSum = OperationFactory.reduceSum(diffSquare,-1);
            diffSquare.setName("diffSquare");
            averageSum.setName("averageSum");
            if(w == null) {
                gm.setOptimizeNode(averageSum);
            } else {
                FlowNode decay;
                if(w.equals(WeightDecay.L2)){
                    decay = L2WeightDecayNode.makeRegularizationNode(averageSum,0.001);
                } else {
                    decay = L1WeightDecayNode.makeRegularizationNode( averageSum,0.001);
                }
                decay.setName("decay");
                gm.setOptimizeNode(OperationFactory.add(decay ,averageSum));
            }
            this.label = label;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    FlowNode input,label,weight,bias,result;
    public int dimension;
    GraphManager gm;
    public void prepare(double[][] data, double[] dataLabel){
        Map<FlowNode,DataProvider> p = new HashMap<>();
        p.put(input,new TwoDArrayDataProvider(data));
        p.put(label,new ArrayDataProvider(dataLabel));
        try {
            gm.feed(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void train(){
        for(int i = 0;i < 50000;i++){
            gm.run();
            if(i % 100 == 0){
                System.out.println("for step " + i + " cost is " + gm.getCost());
            }
        }
    }

    public Double Predict(double[] in){
        Map<FlowNode,DataProvider> p = new HashMap<>();
        p.put(input,new TwoDArrayDataProvider(in));
        try {
            gm.feed(p);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
        gm.run(result);
        return result.getData()[0];
    }
}
