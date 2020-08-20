package com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass;

import com.seemingwang.machineLearning.DerivativeDescriber.DerivativeDescriber;
import com.seemingwang.machineLearning.DerivativeDescriber.DoubleTypeDevDescriber;
import com.seemingwang.machineLearning.FlowNode.FlowOpSingle;

import java.util.Arrays;
import java.util.List;

public class FlowOpSingleParamSigmoidForDoubleType extends FlowOpSingle<Double,Double> {

    public static FlowOpSingleParamSigmoidForDoubleType instance = new FlowOpSingleParamSigmoidForDoubleType();

    public double sigmoid(double a){
        double exp = Math.exp(a);
        return exp / (exp + 1);
    }
    @Override
    public Double cal(Double input0) {
        return sigmoid(input0);
    }

    @Override
    public List<DerivativeDescriber> calDev(Double dev, Double input0) {
        double sig = sigmoid(input0);
        return Arrays.asList(new DoubleTypeDevDescriber(dev * sig * (1 - sig)));
    }
}

