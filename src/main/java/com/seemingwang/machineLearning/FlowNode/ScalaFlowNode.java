package com.seemingwang.machineLearning.FlowNode;

import java.util.ArrayList;

public class ScalaFlowNode extends FlowNode<Double> {

    public ScalaFlowNode() {
        data = new ArrayList<>();
    }

    @Override
    public int[] getShape() {
        int [] shape = new int[1];
        shape[0] = 1;
        return shape;
    }

    @Override
    public void updateDev(Double dev, double learningRate) {
        this.data.set(0,this.data.get(0) - dev * learningRate);
    }
}
