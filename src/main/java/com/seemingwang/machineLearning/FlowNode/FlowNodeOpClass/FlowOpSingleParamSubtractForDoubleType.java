package com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass;

import com.seemingwang.machineLearning.DerivativeDescriber.DerivativeDescriber;
import com.seemingwang.machineLearning.DerivativeDescriber.DoubleTypeDevDescriber;
import com.seemingwang.machineLearning.FlowNode.FlowOpDouble;

import java.util.Arrays;
import java.util.List;

public class FlowOpSingleParamSubtractForDoubleType extends FlowOpDouble<Double,Double,Double> {

    public static FlowOpSingleParamSubtractForDoubleType instance = new FlowOpSingleParamSubtractForDoubleType();
    @Override
    public Double cal(Double input0, Double input1) {
        return input0 - input1;
    }

    @Override
    public List<DerivativeDescriber> calDev(Double dev, Double input0, Double input1) {
        return Arrays.asList(new DoubleTypeDevDescriber(dev), new DoubleTypeDevDescriber(-dev));
    }
}