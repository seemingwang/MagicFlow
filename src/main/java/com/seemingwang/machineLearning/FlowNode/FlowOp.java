package com.seemingwang.machineLearning.FlowNode;


public interface FlowOp {
    void forward(FlowNode f);
    void backward(FlowNode f);
}
