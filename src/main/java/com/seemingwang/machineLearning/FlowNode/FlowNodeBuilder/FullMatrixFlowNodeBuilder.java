package com.seemingwang.machineLearning.FlowNode.FlowNodeBuilder;

import com.seemingwang.machineLearning.FlowNode.FullMatrixFlowNode;

public class FullMatrixFlowNodeBuilder extends FlowNodeBuilder<FullMatrixFlowNode> {

    public FullMatrixFlowNodeBuilder(int r,int c) {
        data = new FullMatrixFlowNode(r,c);
    }
}
