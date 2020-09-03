package com.seemingwang.machineLearning.Optimizer;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.Sequential.Sequential;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradientDescentOptimizer extends Optimizer{
    public GradientDescentOptimizer(double learningRate) {
        this.learningRate = learningRate;
        seqMap = new HashMap<FlowNode, List<FlowNode>>();
    }

    Map<FlowNode,List<FlowNode>> seqMap;
    @Override
    public void run(FlowNode node) {
        List<FlowNode> seq = seqMap.get(node);
        if (seq == null || seq.size() == 0){
            try {
                seq = Sequential.arrange(false, node);
                seqMap.put(node,seq);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        node.resetDevSize(1);
        node.dev[0] = 1.0;
        for(FlowNode c: seq){
            if(c.getChildren() != null && c.getChildren().size() > 0){
                c.getOp().backward(c);
            }
            if(c.isTrainable()){
                c.updateDev(learningRate);
            }
        }

    }

}
