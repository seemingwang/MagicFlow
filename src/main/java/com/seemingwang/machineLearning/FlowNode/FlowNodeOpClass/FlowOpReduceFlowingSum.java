package com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowOpSingleParam;

public class FlowOpReduceFlowingSum extends FlowOpSingleParam {
    public FlowOpReduceFlowingSum(int index,boolean average) {
        this.index = index;
        this.average = average;
    }

    private int index;
    boolean average;
    int blockSize;
    @Override
    public void cal(FlowNode input0, FlowNode output) {
        int totalSize = input0.getSize();
        output.resetDataSize(blockSize);
        for(int i = 0;i < blockSize;i++)
            output.data[i] = 0;
        int groupSize = totalSize/blockSize;
        for(int i = 0;i < totalSize;i+=blockSize){
            for(int j = 0;j < blockSize;j++){
                output.data[j] += input0.data[i + j];
            }
        }
        if(average) {
            for (int i = 0; i < blockSize; i++)
                output.data[i] /= groupSize;
        }
    }

    @Override
    public void calDev(FlowNode input0, FlowNode output) {
        int totalSize = input0.getSize();
        input0.resetDevSize(totalSize);
        int groupSize = totalSize/blockSize;
        for(int i = 0;i < totalSize;i+=blockSize){
            for(int j = 0;j < blockSize;j++){
                input0.dev[i + j] += average?(output.dev[j] / groupSize):output.dev[j];
            }
        }
    }

    @Override
    public void connect(FlowNode input0, FlowNode output) throws Exception {
        Integer []shape = new Integer[input0.shape.length - 1 - index];
        blockSize = 1;
        for(int j = index + 1;j < input0.shape.length;j++){
            blockSize *= input0.shape[j];
            shape[j - index - 1] = input0.shape[j];
        }
        output.resetDataSize(blockSize);
        output.setShape(shape);
        output.getChildren().add(input0);
        output.setOp(this);
    }
}
