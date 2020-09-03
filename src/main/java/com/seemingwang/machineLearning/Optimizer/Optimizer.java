package com.seemingwang.machineLearning.Optimizer;

import com.seemingwang.machineLearning.FlowNode.FlowNode;

public abstract class Optimizer {

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public double learningRate;
    public abstract void run(FlowNode node);
}
