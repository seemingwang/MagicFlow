package com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowOpDoubleParams;

public class FlowOpMatMultiply extends FlowOpDoubleParams {

    public static FlowOpMatMultiply instance = new FlowOpMatMultiply();
    @Override
    public void cal(FlowNode input0, FlowNode input1, FlowNode output) {
        int row = input0.shape[0],col = input1.shape[1],mid = input0.shape[1];
        output.resetDataSize(row*col);
        for(int i = 0;i < row;i++){
            for(int j = 0;j < col;j++){
                double tot = 0;
                for(int k = 0;k < mid;k++)
                    tot += input0.data[i*mid+k] * input1.data[k*col+j];
                output.data[i * col + j] = tot;
            }
        }
    }

    @Override
    public void calDev(FlowNode input0, FlowNode input1, FlowNode output) {
        int row = input0.shape[0],col = input1.shape[1],mid = input0.shape[1];
        input0.resetDevSize(row * mid);
        input1.resetDevSize(mid * col);
        for(int i = 0;i < row;i++){
            for(int j = 0;j < col;j++){
                double dev = output.dev[i * col + j];
                for(int k = 0;k < mid;k++){
                    input0.dev[i*mid + k] += dev * input1.data[k*col+j];
                    input1.dev[k*col+ j] += dev * input0.data[i*mid+k];
                }
            }
        }
    }

    @Override
    public void connect(FlowNode input0, FlowNode input1, FlowNode output) throws Exception {
        if(input0.shape == null || input1.shape == null || input0.shape.length != 2 || input1.shape.length != 2){
            throw new Exception("input0 " + input0.getShapeDesc() + " and input1 " + input1.getShapeDesc() + " doesn't form a valid pair for matrix multiplication");
        }
        output.setShape(new Integer[]{input0.getShape()[0],input1.getShape()[1]});
        output.getChildren().add(input0);
        output.getChildren().add(input1);
        output.setOp(this);
    }
}
