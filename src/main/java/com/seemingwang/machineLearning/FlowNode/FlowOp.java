package com.seemingwang.machineLearning.FlowNode;


import com.seemingwang.machineLearning.DerivativeDescriber.DerivativeDescriber;

import java.util.List;

public interface FlowOp<T> {
    void forward(FlowNode<T> f);
    List<List<DerivativeDescriber>> backward(FlowNode<T> f, List<T> dev);
}
