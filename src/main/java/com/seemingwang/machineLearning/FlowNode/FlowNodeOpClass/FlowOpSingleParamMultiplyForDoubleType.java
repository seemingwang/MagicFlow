package com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass;

import com.seemingwang.machineLearning.DerivativeDescriber.DerivativeDescriber;
import com.seemingwang.machineLearning.DerivativeDescriber.DoubleTypeDevDescriber;
import com.seemingwang.machineLearning.FlowNode.FlowOpDouble;

import java.util.Arrays;
import java.util.List;

public class FlowOpSingleParamMultiplyForDoubleType extends FlowOpDouble<Double,Double,Double> {

    public static FlowOpSingleParamMultiplyForDoubleType instance = new FlowOpSingleParamMultiplyForDoubleType();
    @Override
    public Double cal(Double input0, Double input1) {
        return input0 * input1;
    }

    @Override
    public List<DerivativeDescriber> calDev(Double dev, Double input0, Double input1) {
        return Arrays.asList(new DoubleTypeDevDescriber(input1 * dev),new DoubleTypeDevDescriber(input0 * dev));
    }
}

