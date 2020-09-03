package com.seemingwang.machineLearning.models;

import com.seemingwang.machineLearning.Component.FullyConnectedLayers;
import com.seemingwang.machineLearning.DataInitializer.RangeDataInitializer;
import com.seemingwang.machineLearning.DataProvider.ArrayDataProvider;
import com.seemingwang.machineLearning.DataProvider.DataProvider;
import com.seemingwang.machineLearning.DataProvider.TwoDArrayDataProvider;
import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowNodeBuilder;
import com.seemingwang.machineLearning.GraphManager.GraphManager;
import com.seemingwang.machineLearning.OperationFactory.OperationFactory;
import com.seemingwang.machineLearning.Optimizer.Optimizer;
import com.seemingwang.machineLearning.Utils.Structure.Activator;
import com.seemingwang.machineLearning.Utils.Tripple;

import java.util.HashMap;
import java.util.Map;

public class FullyConnectedNetworkWithScalaOutput {
    FlowNode input,output,label;
    GraphManager gm;

    public FullyConnectedNetworkWithScalaOutput(int inputDimension, int [] weightDimension, Activator a, Optimizer op) {
        gm = new GraphManager().setInitializer(new RangeDataInitializer(0,3)).setOptimizer(op);
        label = new FlowNodeBuilder().setName("label").build();
        try {
            input = new FlowNodeBuilder().setShape(new Integer[]{null,inputDimension}).setName("input").build();
            FlowNode temp = input;
            FullyConnectedLayers maker = new FullyConnectedLayers();
            for(int i = 0;i < weightDimension.length;i+=2){
                temp = maker.makeFullyConnectedLayers(temp,new Tripple<>(weightDimension[i],weightDimension[i+1],i == weightDimension.length - 2 ?null:a));
            }
            output = temp;
            input.setName("input");
            output.setName("output");
            FlowNode diff =  OperationFactory.subtract(output,label);
            diff.setName("diff");
            FlowNode squareError = OperationFactory.pow(diff,2);
            squareError.setName("squareError");
            FlowNode averageSum = OperationFactory.reduceSum(squareError,-1);
            averageSum.setName("averageSum");
            gm.setOptimizeNode(averageSum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public void train(int step){
        for(int i = 0;i < step;i++){
            gm.run();
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
        gm.run(output);
        return output.getData()[0];
    }
}
