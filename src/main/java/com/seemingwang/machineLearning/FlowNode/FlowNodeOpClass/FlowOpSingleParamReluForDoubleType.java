package com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass;

import com.seemingwang.machineLearning.DerivativeDescriber.DerivativeDescriber;
import com.seemingwang.machineLearning.DerivativeDescriber.DoubleTypeDevDescriber;
import com.seemingwang.machineLearning.FlowNode.FlowOpSingle;

import java.util.Arrays;
import java.util.List;

public class FlowOpSingleParamReluForDoubleType extends FlowOpSingle<Double,Double> {

    public static FlowOpSingleParamReluForDoubleType instance = new FlowOpSingleParamReluForDoubleType();

    @Override
    public Double cal(Double input0) {
        return Math.max(0,input0);
    }

    @Override
    public List<DerivativeDescriber> calDev(Double dev, Double input0) {
        if(input0 >= 0)
            return Arrays.asList(new DoubleTypeDevDescriber(dev));
        return Arrays.asList(new DoubleTypeDevDescriber(0.0));
    }
}
