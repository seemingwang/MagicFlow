package com.seemingwang.machineLearning.Regularizer;

import com.seemingwang.machineLearning.FlowNode.ScalaFlowNode;

interface Regularizer {
    ScalaFlowNode makeRegularizationNode(ScalaFlowNode a);
}
