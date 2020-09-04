package com.seemingwang.machineLearning.OperationFactory;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass.*;

public class OperationFactory {
    static public FlowNode add(FlowNode a,FlowNode b) throws Exception {
        FlowNode f = new FlowNode();
        FlowOpAdd.instance.connect(a,b,f);
        return f;
    }

    static public FlowNode multiply(FlowNode a,FlowNode b) throws Exception {
        FlowNode f = new FlowNode();
        FlowOpMultiply.instance.connect(a,b,f);
        return f;
    }

    static public FlowNode matMultiply(FlowNode a,FlowNode b) throws Exception {
        FlowNode f = new FlowNode();
        FlowOpMatMultiply.instance.connect(a,b,f);
        return f;
    }


    static public FlowNode subtract(FlowNode a,FlowNode b) throws Exception {
        FlowNode f = new FlowNode();
        FlowOpSubtract.instance.connect(a,b,f);
        return f;
    }

    static public FlowNode sigmoid(FlowNode a){
        FlowNode f = new FlowNode();
        try {
            FlowOpSigmoid.instance.connect(a,f);
            return f;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static public FlowNode pow(FlowNode a, double s){
        FlowNode f = new FlowNode();
        try {
            new FlowOpPow(s).connect(a,f);
            return f;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static public FlowNode reduceSum(FlowNode a,int index, boolean average){
        FlowNode f = new FlowNode();
        try {
            new FlowOpReduceSum(index,average).connect(a,f);
            return f;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
