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
    public void run(ScalaFlowNode node) {
        List<FlowNode> seq = seqMap.get(node);
        if (seq == null || seq.size() == 0){
            try {
                seq = Sequential.arrange(false, node);
                seqMap.put(node,seq);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<DerivativeDescriber> l = new ArrayList<>();
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
                    List<DerivativeDescriber> l2copy = l2.get(i);
                    if(x.isTrainable() && l2copy.size() > 1){
                        DerivativeDescriber one = l2copy.get(0);
                        for(int k = 1; k < l2copy.size();k++){
                            one = one.combine(l2copy.get(k));
                        }
                        l2copy.clear();
                        l2copy.add(one);
                    }
                    if(l3 == null){
                        m.put(x,l2copy);
                    } else {
                        for(int j = 0;j < l2copy.size();j++){
                            l3.set(j,l3.get(j).combine(l2copy.get(j)));
                        }
                    }

                }
            }
            if(c.isTrainable()){
                c.updateDev(dl.get(0).export(),learningRate);
            }
        }




    }

}
