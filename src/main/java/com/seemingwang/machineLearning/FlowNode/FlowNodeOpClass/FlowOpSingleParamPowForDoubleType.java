package com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass;

import com.seemingwang.machineLearning.DerivativeDescriber.DerivativeDescriber;
import com.seemingwang.machineLearning.DerivativeDescriber.DoubleTypeDevDescriber;
import com.seemingwang.machineLearning.FlowNode.FlowOpSingle;

import java.util.Arrays;
import java.util.List;

public class FlowOpSingleParamPowForDoubleType extends FlowOpSingle<Double,Double> {

    public static FlowOpSingleParamPowForDoubleType square = new FlowOpSingleParamPowForDoubleType(2);
    public double param;

    public FlowOpSingleParamPowForDoubleType(int param) {
        this.param = param;
    }

    @Override
    public Double cal(Double input0) {
        return Math.pow(input0,param);
    }

    @Override
    public List<DerivativeDescriber> calDev(Double dev, Double input0) {
        return Arrays.asList(new DoubleTypeDevDescriber(dev * param * Math.pow(input0,param - 1)));
    }
}

