package com.seemingwang.machineLearning.FlowNode;

import java.util.List;

public abstract class FlowNode<T> {
    public boolean isTrainable() {
        return trainable;
    }

    public void setTrainable(boolean trainable) {
        this.trainable = trainable;
    }

    boolean trainable = false;
    public List<FlowNode> getChildren() {
        return children;
    }

    public void setChildren(List<FlowNode> children) {
        this.children = children;
    }

    public List<FlowNode> children;

    public FlowOp getOp() {
        return op;
    }

    public void setOp(FlowOp op) {
        this.op = op;
    }

    FlowOp op;
    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public List<T> data;

    public abstract int [] getShape();

    public abstract void updateDev(T dev, double learningRate);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String name;
}

