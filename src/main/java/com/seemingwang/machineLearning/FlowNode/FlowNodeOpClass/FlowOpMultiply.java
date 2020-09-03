package com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowOpDoubleParams;

public class FlowOpMultiply extends FlowOpDoubleParams {

    public static FlowOpMultiply instance = new FlowOpMultiply();

    @Override
    public void cal(FlowNode input0, FlowNode input1, FlowNode output) {
        int size0 = input0.getSize(),size1 = input1.getSize();
        output.resetDataSize(size0);
        for(int i = 0,j = 0;i < size0;i++){
            output.data[i] = input0.data[i] * input1.data[j];
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
            input0.dev[i] += output.dev[i] * input1.data[j];
            input1.dev[j] += output.dev[i] * input0.data[i];
            if(++j == size1)
                j = 0;
        }
    }

    @Override
    public void connect(FlowNode input0, FlowNode input1, FlowNode output) {
        Integer size0 = 1,size1 = 1;
        for(Integer s: input0.getShape()){
            if(s == null)
                continue;
            size0 *= s;
        }
        for(Integer s: input1.getShape()){
            if(s == null)
                continue;
            size1 *= s;
        }
        if(size0 >= size1){
            output.setShape(input0.shape);
            output.getChildren().add(input0);
            output.getChildren().add(input1);
        } else {
            output.setShape(input1.shape);
            output.getChildren().add(input1);
            output.getChildren().add(input0);
        }
    }
}

