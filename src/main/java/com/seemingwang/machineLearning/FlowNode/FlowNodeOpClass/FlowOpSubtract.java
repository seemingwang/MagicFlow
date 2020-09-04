package com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowOpDoubleParams;

public class FlowOpSubtract extends FlowOpDoubleParams {

    public static FlowOpSubtract instance = new FlowOpSubtract();

    @Override
    public void cal(FlowNode input0, FlowNode input1, FlowNode output) {
        int size0 = input0.getSize(),size1 = input1.getSize();
        output.resetDataSize(size0);
        for(int i = 0,j = 0;i < size0;i++){
            output.data[i] = input0.data[i] - input1.data[j];
            if(++j == size1)
                j = 0;
        }
    }

    @Override
    public void calDev(FlowNode input0, FlowNode input1, FlowNode output) {
        int size0 = input0.getSize(),size1 = input1.getSize();
        input0.resetDevSize(size0);
        input1.resetDevSize(size1);
        for(int i = 0,j = 0;i < size0;i++){
            input0.dev[i] += output.dev[i];
            input1.dev[j] += -1 * output.dev[i];
            if(++j == size1)
                j = 0;
        }
    }

    @Override
    public void connect(FlowNode input0, FlowNode input1, FlowNode output) {
        output.setShape(input0.shape);
        output.getChildren().add(input0);
        output.getChildren().add(input1);
        output.setOp(this);
    }
}
