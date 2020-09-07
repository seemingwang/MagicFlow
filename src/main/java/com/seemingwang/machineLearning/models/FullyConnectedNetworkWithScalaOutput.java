package com.seemingwang.machineLearning.models;

import com.seemingwang.machineLearning.Component.FullyConnectedLayers;
import com.seemingwang.machineLearning.DataInitializer.NormalDataInitializer;
import com.seemingwang.machineLearning.DataProvider.DataProvider;
import com.seemingwang.machineLearning.DataProvider.MiniBatch;
import com.seemingwang.machineLearning.DataProvider.RandomShuffleBatch;
import com.seemingwang.machineLearning.DataProvider.TwoDArrayDataProvider;
import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowNodeBuilder;
import com.seemingwang.machineLearning.GraphManager.GraphManager;
import com.seemingwang.machineLearning.OperationFactory.OperationFactory;
import com.seemingwang.machineLearning.Optimizer.Optimizer;
import com.seemingwang.machineLearning.Utils.Pair;
import com.seemingwang.machineLearning.Utils.Structure.Activator;
import com.seemingwang.machineLearning.Utils.Tripple;

import java.util.HashMap;
import java.util.Map;

public class FullyConnectedNetworkWithScalaOutput {
    FlowNode input,output,label;
    GraphManager gm;
    int dimension;
    MiniBatch batch;

    public FullyConnectedNetworkWithScalaOutput(int inputDimension, int [] weightDimension, Activator a, Optimizer op) {
        dimension = inputDimension;
        gm = new GraphManager().setInitializer(new NormalDataInitializer(0,5)).setOptimizer(op);
        label = new FlowNodeBuilder().setName("label").setShape(new Integer[]{null,1}).build();
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
            FlowNode averageSum = OperationFactory.reduceSum(squareError,-1,true);
            averageSum.setName("averageSum");
            gm.setOptimizeNode(averageSum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void prepare(double[][] data, double[] dataLabel,int batchSize){
        batch = new RandomShuffleBatch(new TwoDArrayDataProvider(data),new TwoDArrayDataProvider(dataLabel,dataLabel.length,1),batchSize);
    }

    public void train(int step){
        Map<FlowNode,Object> m = new HashMap<>();
        for(int i = 0;i < step;i++){
            Pair<DataProvider,DataProvider> p = batch.nextBatch();
            m.put(input,p.first);
            m.put(label,p.second);
            try {
                gm.feed(m);
                gm.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Double predict(double[] in){
        Map<FlowNode,Object> p = new HashMap<>();
        p.put(input,new TwoDArrayDataProvider(in,1, dimension));
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
