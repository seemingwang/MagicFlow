package com.seemingwang.machineLearning.FlowNode;

public class FlowNodeBuilder {

    FlowNode f;
    public FlowNodeBuilder(){
        f = new FlowNode();
    }

    public FlowNodeBuilder setName(String str){
        f.setName(str);
        return this;
    }

    public FlowNodeBuilder setTrainable(boolean trainable){
        f.setTrainable(trainable);
        return this;
    }

    public FlowNodeBuilder setShape(Integer [] shape){
        f.setShape(shape);
        return this;
    }

    public FlowNode build(){
        return f;
    }
}
