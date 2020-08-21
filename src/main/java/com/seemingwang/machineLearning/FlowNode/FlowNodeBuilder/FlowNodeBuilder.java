package com.seemingwang.machineLearning.FlowNode.FlowNodeBuilder;

import com.seemingwang.machineLearning.FlowNode.FlowNode;

import java.util.List;

public abstract class FlowNodeBuilder<T extends FlowNode>{
    T data;
    public FlowNodeBuilder<T> setName(String name){
        data.setName(name);
        return this;
    }

    public FlowNodeBuilder<T> setTrainable(boolean trainable){
        data.setTrainable(trainable);
        return this;
    }

    public FlowNodeBuilder<T> setChildren(List l){
        data.setChildren(l);
        return this;
    }


    public FlowNodeBuilder<T> setData(List l){
        data.setData(l);
        return this;
    }

    public T build(){
        return data;
    }

}
