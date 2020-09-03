package com.seemingwang.machineLearning.FlowNode;

public abstract class FlowOpSingleParam implements FlowOp {
    public abstract void cal(FlowNode input0, FlowNode output);
    public abstract void calDev(FlowNode input0, FlowNode output);
    public abstract void connect(FlowNode input0, FlowNode output) throws Exception;

    @Override
    public void forward(FlowNode f){
        if(!f.getChildren().isEmpty() && f.getChildren().size() >= 1) {
            cal(f.getChildren().get(0),f);
        }
    }

    @Override
    public void backward(FlowNode f){
        if(!f.getChildren().isEmpty() && f.getChildren().size() >= 1) {
            calDev(f.getChildren().get(0),f);
        }
    }
}
