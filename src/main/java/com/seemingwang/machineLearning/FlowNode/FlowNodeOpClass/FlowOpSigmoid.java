package com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowOpSingleParam;

public class FlowOpSigmoid extends FlowOpSingleParam {

    public static FlowOpSigmoid instance = new FlowOpSigmoid();

    public double sigmoid(double a){
        double exp = Math.exp(a);
        return exp / (exp + 1);
    }

    @Override
    public void cal(FlowNode input0, FlowNode output) {
        int size = input0.getSize();
        output.resetDataSize(size);
        for(int i = 0;i < size;i++)
            output.data[i] = sigmoid(input0.data[i]);
        //Math.max(0,input0)
    }

    @Override
    public void calDev(FlowNode input0, FlowNode output) {
        int size = input0.getSize();
        input0.resetDevSize(size);
        for(int i = 0;i < size;i++) {
            double sig = sigmoid(input0.data[i]);
            input0.dev[i] += output.dev[i] * sig * (1 - sig);
        }
//        double sig = sigmoid(input0);
//        return Arrays.asList(new DoubleTypeDevDescriber(dev * sig * (1 - sig)));
    }

    @Override
    public void connect(FlowNode input0, FlowNode output) throws Exception {
        output.setShape(input0.shape);
        output.getChildren().add(input0);
        output.setOp(this);
    }
}
