package com.seemingwang.machineLearning.Optimizer;

import com.seemingwang.machineLearning.FlowNode.ScalaFlowNode;

public abstract class Optimizer {

    public abstract void run(ScalaFlowNode node,int size);
}
