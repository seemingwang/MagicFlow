package com.seemingwang.machineLearning.Optimizer;

import com.seemingwang.machineLearning.DerivativeDescriber.DerivativeDescriber;
import com.seemingwang.machineLearning.DerivativeDescriber.DoubleTypeDevDescriber;
import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.ScalaFlowNode;
import com.seemingwang.machineLearning.Sequential.Sequential;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GradientDescentOptimizer extends Optimizer{
    double learningRate;
    public GradientDescentOptimizer(double learningRate) {
        this.learningRate = learningRate;
        seqMap = new HashMap<FlowNode, List<FlowNode>>();
    }

    Map<FlowNode,List<FlowNode>> seqMap;
    @Override
    public void run(ScalaFlowNode node,int size) {
        List<FlowNode> seq = seqMap.get(node);
        if (seq == null || seq.size() == 0 || size == 0){
            try {
                seq = Sequential.arrange(false, node);
                seqMap.put(node,seq);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<DerivativeDescriber> l = new ArrayList<>();
        for(int i = 0;i < size;i++)
            l.add(new DoubleTypeDevDescriber(1.0));
        Map<FlowNode,List<DerivativeDescriber>> m = new HashMap<>();
        m.put(node,l);
        for(FlowNode c: seq){
            List<DerivativeDescriber> dl = m.get(c);
            List l1 = dl.stream().map(d -> d.export()).collect(Collectors.toList());
            if(c.getChildren() != null && c.getChildren().size() > 0){
                List<List<DerivativeDescriber>> l2 = c.getOp().backward(c,l1);
                for(int i = 0;i < c.getChildren().size();i++){
                    FlowNode x = (FlowNode)c.getChildren().get(i);
                    List<DerivativeDescriber> l3 = m.get(x);
                    if(l3 == null){
                        m.put(x,l2.get(i));
                    } else {
                        for(int j = 0;j < size;j++){
                            l3.set(j,l3.get(j).combine(l2.get(i).get(j)));
                        }
                    }

                }
            }
            if(c.isTrainable()){
                DerivativeDescriber d = dl.get(0);
                for(int i = 1; i < size;i++){
                    d = d.combine(dl.get(i));
                }
                c.updateDev(d.export(),learningRate);
            }
        }




    }

    @Override
    public double calCost(ScalaFlowNode node) {
        double cost = 0;
        for(Double d: node.getData())
            cost += d;
        return cost;
    }
}
