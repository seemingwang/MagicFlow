package com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowOpSingleParam;

public class FlowOpRelu extends FlowOpSingleParam {

    public static FlowOpRelu instance = new FlowOpRelu();
    @Override
    public void cal(FlowNode input0, FlowNode output) {
        int size = input0.getSize();
        output.resetDataSize(size);
        for(int i = 0;i < size;i++)
            output.data[i] = Math.max(input0.data[i],0);
        //Math.max(0,input0)
    }

    @Override
    public void calDev(FlowNode input0, FlowNode output) {
        int size = input0.getSize();
        input0.resetDevSize(size);
        for(int i = 0;i < size;i++) {
            if(output.data[i] >= 0){
                input0.dev[i] += output.dev[i];
            }
        }
//        if(input0 >= 0)
//            return Arrays.asList(new DoubleTypeDevDescriber(dev));
//        return Arrays.asList(new DoubleTypeDevDescriber(0.0));
    }

    @Override
    public void connect(FlowNode input0, FlowNode output) throws Exception {
        output.setShape(input0.shape);
        output.getChildren().add(input0);
    }
}
