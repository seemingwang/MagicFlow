package com.seemingwang.machineLearning.models;

import com.seemingwang.machineLearning.Component.FullyConnectedLayers;
import com.seemingwang.machineLearning.DataInitializer.AveDataInitializer;
import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FullMatrixFlowNode;
import com.seemingwang.machineLearning.FlowNode.ScalaFlowNode;
import com.seemingwang.machineLearning.GraphManager.GraphManager;
import com.seemingwang.machineLearning.Matrix.FullMatrix;
import com.seemingwang.machineLearning.OperationFactory.OperationFactory;
import com.seemingwang.machineLearning.Optimizer.Optimizer;
import com.seemingwang.machineLearning.Utils.Structure.Activator;
import com.seemingwang.machineLearning.Utils.Tripple;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FullyConnectedNetworkWithScalaOutput {
    FlowNode input,output,label;
    GraphManager gm;

    public FullyConnectedNetworkWithScalaOutput(int inputDimension, int [] weightDimension, Activator a, Optimizer op) {
        gm = new GraphManager().setInitializer(new AveDataInitializer(-10,10)).setOptimizer(op);
        label = new ScalaFlowNode();
        label.setName("label");
        try {
            input = new FullMatrixFlowNode(1,inputDimension);
            FlowNode temp = input;
            FullyConnectedLayers maker = new FullyConnectedLayers();
            for(int i = 0;i < weightDimension.length;i+=2){
                temp = maker.makeFullyConnectedLayers((FullMatrixFlowNode)temp,new Tripple<>(weightDimension[i],weightDimension[i+1],i == weightDimension.length - 2 ?null:a));
            }
            output = temp;
            input.setName("input");
            output.setName("output");
            FlowNode diff =  OperationFactory.subtract(output,label);
            diff.setName("diff");
            FlowNode squareError = OperationFactory.pow(diff,2);
            squareError.setName("squareError");
            FlowNode averageSum = OperationFactory.averageSum(squareError);
            averageSum.setName("averageSum");
            gm.setOptimizeNode((ScalaFlowNode)averageSum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public void train(int step){
        for(int i = 0;i < step;i++){
            gm.run();
        }
    }

    public Double Predict(List<Double> in){
        input.setData(gm.changeToMatrixList(Arrays.asList(in)));
        gm.run(output);
        return ((FullMatrix)output.getData().get(0)).get(0,0);
    }
}
