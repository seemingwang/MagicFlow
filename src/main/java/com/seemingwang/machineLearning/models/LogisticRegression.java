package com.seemingwang.machineLearning.models;

import com.seemingwang.machineLearning.DataInitializer.RangeDataInitializer;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LogisticRegression {
    public LogisticRegression(int dimension, WeightDecay w) {
        this.dimension = dimension;
        gm = new GraphManager().setInitializer(new RangeDataInitializer(-3,3)).setOptimizer(new GradientDescentOptimizer(0.1));
        this.input = new FlowNodeBuilder().setShape(new Integer[]{null,dimension}).setName("input").build();
        this.weight = new FlowNodeBuilder().setShape(new Integer[]{dimension,1}).setTrainable(true).setName("weight").build();
        try {
            FlowNode out =  OperationFactory.matMultiply(input,weight);
            this.bias = new FlowNodeBuilder().setShape(new Integer[]{}).setTrainable(true).setName("bias").build();
            this.result = OperationFactory.sigmoid(OperationFactory.add(out,this.bias));
            result.setName("result");
            System.out.println(result.getShapeDesc());
            FlowNode label = new FlowNodeBuilder().setShape(new Integer[]{null,1}).setName("label").build();
            FlowNode diffSquare = OperationFactory.pow(OperationFactory.subtract(result,label),2);
            FlowNode averageSum = OperationFactory.reduceSum(diffSquare,-1,true);
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
        p.put(label,new TwoDArrayDataProvider(dataLabel,dataLabel.length,1));
        try {
            gm.feed(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void train(int step){
        for(int i = 0;i < step;i++){
            gm.run();
//            if(i % 100 == 0){
//                System.out.println("for step " + i + " cost is " + gm.getCost());
//            }
        }
    }

    public Double Predict(double[] in){
        Map<FlowNode,DataProvider> p = new HashMap<>();
        p.put(input,new TwoDArrayDataProvider(in,1,dimension));
        try {
            gm.feed(p);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
        gm.run(result);
        return result.getData()[0];
    }

    public double[] exportWeight(){
        int size = weight.getSize();
        return Arrays.copyOfRange(weight.data,0,size);
    }

    public Double exportBias() {
        return bias.data[0];
    }
}
