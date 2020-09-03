package com.seemingwang.machineLearning.Regularizer;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowOp;

import java.util.*;

public class L1WeightDecayNode extends FlowNode{

    public int count;
    public double lamda;
    L1WeightDecayNode(double lamda){
        this.lamda = lamda;
    }
    public static L1WeightDecayNode makeRegularizationNode(FlowNode a,double lamda) {
        Queue<FlowNode> q = new LinkedList<>();
        L1WeightDecayNode optimize = new L1WeightDecayNode(lamda);
        optimize.setChildren(new ArrayList<>());
        q.add(a);
        HashSet<FlowNode> s = new HashSet<>();
        s.add(a);
        while(q.size() > 0){
            FlowNode c = q.poll();
            if(c.isTrainable()){
                optimize.getChildren().add(c);
            }
            for(FlowNode x: (List<FlowNode>)c.getChildren()){
                if(!s.contains(x)){
                    s.add(x);
                    q.add(x);
                }
            }
        }
        optimize.setOp(new FlowOp() {
            @Override
            public void forward(FlowNode f) {
                double res = 0,t;
                for(FlowNode c: f.getChildren()){
                    int size = c.getSize();
                    for(int i = 0;i < c.data.length;i++)
                        res += c.data[i] >= 0?c.data[i]:-c.data[i];
                }
                f.resetDataSize(1);
                f.data[0] = res * ((L2WeightDecayNode)f).lamda/2;
            }

            @Override
            public void backward(FlowNode f) {
                double derivative = f.dev[0] * ((L1WeightDecayNode)f).lamda;
                for(FlowNode c: f.getChildren()){
                    int size =  c.getSize();
                    c.resetDevSize(size);
                    for(int i = 0;i < size;i++)
                        c.dev[i] += c.data[i] >= 0? derivative:-derivative;
                }

            }
        });
        return optimize;


    }
}
