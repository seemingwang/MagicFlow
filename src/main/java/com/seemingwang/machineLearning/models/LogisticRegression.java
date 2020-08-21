package com.seemingwang.machineLearning.models;

import com.seemingwang.machineLearning.DataInitializer.AveDataInitializer;
import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowNodeBuilder.FullMatrixFlowNodeBuilder;
import com.seemingwang.machineLearning.FlowNode.FlowNodeBuilder.ScalaFlowNodeBuilder;
import com.seemingwang.machineLearning.FlowNode.FullMatrixFlowNode;
import com.seemingwang.machineLearning.FlowNode.ScalaFlowNode;
import com.seemingwang.machineLearning.GraphManager.GraphManager;
import com.seemingwang.machineLearning.Matrix.FullMatrix;
import com.seemingwang.machineLearning.OperationFactory.OperationFactory;
import com.seemingwang.machineLearning.Optimizer.GradientDescentOptimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogisticRegression {
    public LogisticRegression(int dimension) {
        this.dimension = dimension;
        gm = new GraphManager().setInitializer(new AveDataInitializer()).setOptimizer(new GradientDescentOptimizer(0.1));
        this.input = new FullMatrixFlowNode(1,dimension);
        this.weight = new FullMatrixFlowNodeBuilder(dimension,1).setTrainable(true).setName("weight").build();
        try {
            FlowNode out =  OperationFactory.Multiply(input,weight);
            this.bias = new ScalaFlowNodeBuilder().setTrainable(true).setName("bias").build();
            FlowNode result = OperationFactory.sigmoid(OperationFactory.add(out,this.bias));
            result.setName("result");
            FlowNode label = new ScalaFlowNodeBuilder().setName("label").build();
            FlowNode diffSquare = OperationFactory.pow(OperationFactory.subtract(result,label),2);
            FlowNode averageSum = OperationFactory.averageSum(diffSquare);
            diffSquare.setName("diffSquare");
            averageSum.setName("averageSum");
            gm.setOptimizeNode((ScalaFlowNode)averageSum);
            this.label = label;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    FlowNode input,label,weight,bias;
    public int dimension;
    GraphManager gm;
    public void prepare(List<List<Double>> data, List<Double> dataLabel){
        Map<String,Map<FlowNode,List>> m = new HashMap<>();
        Map<FlowNode,List> p = new HashMap<>(), l = new HashMap<>();
        p.put(input,data);
        l.put(label,dataLabel);
        m.put("placeholder",p);
        m.put("label",l);
        try {
            gm.feed(m);
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

}
