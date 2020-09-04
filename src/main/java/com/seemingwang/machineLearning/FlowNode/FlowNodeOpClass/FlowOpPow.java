package com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowOpSingleParam;

public class FlowOpPow extends FlowOpSingleParam{
    @Override
    public void cal(FlowNode input0, FlowNode output) {
        int size = input0.getSize();
        output.resetDataSize(size);
        for(int i = 0;i < size;i++)
            output.data[i] = Math.pow(input0.data[i],param);
    }
    public double param;

    public FlowOpPow(double param) {
        this.param = param;
    }

    @Override
    public void calDev(FlowNode input0, FlowNode output) {
        int size = input0.getSize();
        input0.resetDevSize(size);
        for(int i = 0;i < size;i++) {
            input0.dev[i] += output.dev[i] * param * Math.pow(input0.data[i], param - 1);
        }

    }

    @Override
    public void connect(FlowNode input0, FlowNode output) throws Exception {
        output.setShape(input0.shape);
        output.getChildren().add(input0);
        output.setOp(this);
    }
}
