package com.seemingwang.machineLearning.FlowNode;

import java.util.ArrayList;
import java.util.List;

public class FlowNode {
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

    public List<FlowNode> children = new ArrayList<>();

    public FlowOp getOp() {
        return op;
    }

    public void setOp(FlowOp op) {
        this.op = op;
    }

    FlowOp op;
    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        this.data = data;
    }

    public double[] data,dev;

    public void setShape(Integer[] shape) {
        if(shape != null) {
            this.shape = new Integer[shape.length];
            for(int i = 0;i < shape.length;i++)
                this.shape[i] = shape[i];
        }
    }

    public String getShapeDesc(){
        String res = "(";
        if(shape != null){
            for(int i = 0;i < shape.length;i++){
                if(i > 0)
                    res += ",";
                res += shape[i] == null?"null":shape[i];
            }
        }
        return res + ")";
    }

    public Integer shape[] = new Integer[]{};
    public int getSize(){
        int size = 1;
        for(int x: shape)
            size *= x;
        return size;
    }
    public Integer [] getShape(){
        return shape;
    }

    public void updateDev(double learningRate){
        if(trainable){
           int size = getSize();
           for(int i = 0;i < size;i++) {
               data[i] -= learningRate * dev[i];
           }
        }
    }

    public void resetDataSize(int size){
        if(data == null || data.length != size) {
            data = new double[size];
        }
    }

    public void resetDevSize(int size){
        if(dev == null || dev.length != size) {
            dev = new double[size];
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String name;
}

