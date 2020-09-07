package com.seemingwang.machineLearning.Regularizer;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowNodeBuilder;
import com.seemingwang.machineLearning.FlowNode.FlowOp;
import com.seemingwang.machineLearning.Utils.FetchValue;

import java.util.Arrays;
import java.util.Random;

public class DropoutNode extends FlowNode {
    public void setBinarySwitch(Boolean binarySwitch) {
        this.binarySwitch = binarySwitch;
    }

    @FetchValue
    Boolean binarySwitch;
    double keepingRate;

    public void setMask(double[] mask) {
        this.mask = mask;
    }

    public double[] getMask() {
        return mask;
    }

    double mask[];

    public void setMaskLife(int maskLife) {
        this.maskLife = maskLife;
    }

    int maskLife = 1;
    int maskCount = 0;
    public static Random r = new Random();

    public void initMask(){
        if(maskCount == 0) {
            int size = getSize();
            if (mask == null || mask.length < size)
                mask = new double[size];
            for (int i = 0; i < size; i++) {
                if (DropoutNode.r.nextDouble() < keepingRate) {
                    mask[i] = 1.0 / keepingRate;
                } else {
                    mask[i] = 0.0;
                }
            }
        }
        ++maskCount;
        if(maskCount == maskLife)
            maskCount = 0;
    }

    @Override
    public void setShape(Integer[] shape){
        if(shape != null){
            super.setShape(shape);
            mask = new double[getSize()];
        }
    }

    public DropoutNode(double keepingRate) {
        this.keepingRate = keepingRate;
    }

    public static FlowNode makeDropoutNode(FlowNode a,double keepingRate,int dropIndex) {
        Integer []shape;
        if(dropIndex != -1){
            shape = new Integer[a.getShape().length - 1];
            int ind = 0;
            for(int i = 0;i < a.getShape().length;i++){
                if(i == dropIndex)
                    continue;
                shape[ind++] = a.getShape()[i];
            }
        } else {
            shape = a.getShape().clone();
        }
        DropoutNode drop = new DropoutNode(keepingRate);
        drop.setShape(shape);
        FlowNode output = new FlowNodeBuilder().setShape(a.getShape()).build();
        output.setChildren(Arrays.asList(a,drop));
        output.setOp(new FlowOp() {
            @Override
            public void forward(FlowNode f) {
                DropoutNode drop = (DropoutNode)f.getChildren().get(1);
                FlowNode c = f.getChildren().get(0);
                int size = drop.getSize();
                int inputSize = c.getSize();
                output.resetDataSize(inputSize);
                double[] data = output.getData();
                drop.initMask();
                double []mask = drop.mask;
                if(drop.binarySwitch) {
                    for (int i = 0; i < inputSize; i++) {
                        data[i] = mask[i % size] * c.data[i];
                    }
                } else {
                    for(int i = 0;i < inputSize;i++)
                        data[i] = c.data[i];
                }

            }

            @Override
            public void backward(FlowNode f) {
                DropoutNode drop = (DropoutNode)f.getChildren().get(1);
                FlowNode c = f.getChildren().get(0);
                int size = drop.getSize();
                int inputSize = c.getSize();
                c.resetDevSize(inputSize);
                for (int i = 0; i < inputSize; i++) {
                    if(drop.mask[i % size] < 1e-8)
                        c.dev[i] = 0.0;
                    else
                        c.dev[i] = f.dev[i] * drop.mask[i % size];
                }

            }
        });
        return output;
    }
}
