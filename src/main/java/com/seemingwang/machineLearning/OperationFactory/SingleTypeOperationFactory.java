package com.seemingwang.machineLearning.OperationFactory;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowOpDouble;
import com.seemingwang.machineLearning.FlowNode.FlowOpSingle;

import java.util.Arrays;

public class SingleTypeOperationFactory<T> {
    public void func2(FlowNode a, FlowNode b, FlowNode f ,FlowOpDouble<T,T,T> fp) {
        f.setChildren(Arrays.asList(a,b));
        f.setOp(fp);
    }

    public void func1(FlowNode a, FlowNode f, FlowOpSingle<T,T> fp){
        f.setChildren(Arrays.asList(a));
        f.setOp(fp);
    }
}
