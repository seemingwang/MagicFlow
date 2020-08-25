package com.seemingwang.machineLearning.models;

import com.seemingwang.machineLearning.DataInitializer.AveDataInitializer;
import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowNodeBuilder.FullMatrixFlowNodeBuilder;
import com.seemingwang.machineLearning.FlowNode.FlowNodeBuilder.ScalaFlowNodeBuilder;
import com.seemingwang.machineLearning.FlowNode.ScalaFlowNode;
import com.seemingwang.machineLearning.GraphManager.GraphManager;
import com.seemingwang.machineLearning.Matrix.FullMatrix;
import com.seemingwang.machineLearning.OperationFactory.OperationFactory;
import com.seemingwang.machineLearning.Optimizer.GradientDescentOptimizer;
import com.seemingwang.machineLearning.Regularizer.L1WeightDecayNode;
import com.seemingwang.machineLearning.Regularizer.L2WeightDecayNode;
import com.seemingwang.machineLearning.Utils.Structure.WeightDecay;

import java.util.*;

public class LogisticRegression {
    public LogisticRegression(int dimension, WeightDecay w) {
        this.dimension = dimension;
        gm = new GraphManager().setInitializer(new AveDataInitializer(-3,3)).setOptimizer(new GradientDescentOptimizer(0.1));
        this.input = new FullMatrixFlowNodeBuilder(1,dimension).setName("input").build();
        this.weight = new FullMatrixFlowNodeBuilder(dimension,1).setTrainable(true).setName("weight").build();
        try {
            FlowNode out =  OperationFactory.Multiply(input,weight);
            this.bias = new ScalaFlowNodeBuilder().setTrainable(true).setName("bias").build();
            this.result = OperationFactory.sigmoid(OperationFactory.add(out,this.bias));
            result.setName("result");
            FlowNode label = new ScalaFlowNodeBuilder().setName("label").build();
            FlowNode diffSquare = OperationFactory.pow(OperationFactory.subtract(result,label),2);
            FlowNode averageSum = OperationFactory.averageSum(diffSquare);
            diffSquare.setName("diffSquare");
            averageSum.setName("averageSum");
            if(w == null) {
                gm.setOptimizeNode((ScalaFlowNode) averageSum);
            } else {
                FlowNode decay;
                if(w.equals(WeightDecay.L2)){
                    decay = L2WeightDecayNode.makeRegularizationNode((ScalaFlowNode) averageSum,0.001);
                } else {
                    decay = L1WeightDecayNode.makeRegularizationNode((ScalaFlowNode) averageSum,0.001);
                }
                decay.setName("decay");
                gm.setOptimizeNode((ScalaFlowNode) OperationFactory.add(decay ,averageSum));
            }
            this.label = label;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    FlowNode input,label,weight,bias,result;
    public int dimension;
    GraphManager gm;
    public void prepare(List<List<Double>> data, List<Double> dataLabel){
        Map<FlowNode,List> p = new HashMap<>();
        p.put(input,gm.changeToMatrixList(data));
        p.put(label,dataLabel);
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

    public List<Double> exportWeight(){
        List<Double> l = new ArrayList<>();

        for(int i = 0;i < dimension;i++)
            l.add(((FullMatrix)weight.getData().get(0)).get(i,0));
        return l;
    }

    public Double exportBias(){
        return (Double)bias.getData().get(0);
    }

    public Double Predict(List<Double> in){
        input.setData(gm.changeToMatrixList(Arrays.asList(in)));
        gm.run(result);
        return (Double)result.getData().get(0);
    }
}
