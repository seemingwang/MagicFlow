package com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowOpSingleParam;

public class FlowOpReduceSum extends FlowOpSingleParam {
    public FlowOpReduceSum(int index,boolean average) {
            this.index = index;
            this.average = average;
        }

        private int index;
    boolean average;
    @Override
    public void cal(FlowNode input0, FlowNode output) {
        int blockSize = 1,averageSize;
        if(index == -1){
            output.resetDataSize(1);
            int size = input0.getSize();
            double res = 0;
            for(int i = 0;i < size;i++)
                res += input0.data[i];
            output.data[0] = average ? res *1.0/size:res;
            return;

        }
        for(int j = input0.shape.length - 1;j >= index;j--){
            blockSize *= input0.shape[j];
        }
        int indexSize = input0.shape[index];
        averageSize = blockSize/ indexSize;
        int totalSize = input0.getSize();
        output.resetDataSize(totalSize/indexSize);
        int outInd = 0;
        for(int i = 0;i < totalSize;i+=blockSize){
            for(int j = 0;j < averageSize;j++){
                double t = 0;
                for(int k = 0;k < indexSize;k++){
                    t += input0.data[i + k * averageSize + j];
                }
                output.data[outInd++] = average ? t * 1.0/indexSize:t;
            }
        }
    }

    @Override
    public void calDev(FlowNode input0, FlowNode output) {
        if(index == -1){
            int size = input0.getSize();
            input0.resetDevSize(size);
            for(int i = 0;i < size;i++){
                input0.dev[i] += average ? output.dev[0]/size: output.dev[0];
            }
            return;
        }
        int blockSize = 1,averageSize;
        for(int j = input0.shape.length - 1;j >= index;j--){
            blockSize *= input0.shape[j];
        }
        int indexSize = input0.shape[index];
        averageSize = blockSize/ indexSize;
        int totalSize = input0.getSize();
        input0.resetDataSize(totalSize);
        int outInd = 0;
        for(int i = 0;i < totalSize;i+=blockSize){
            for(int j = 0;j < averageSize;j++){
                for(int k = 0;k < indexSize;k++){
                    input0.dev[i + k * averageSize + j] += average? output.dev[outInd]/indexSize:output.dev[outInd];
                }
                outInd++;
            }
        }
    }

    @Override
    public void connect(FlowNode input0, FlowNode output) throws Exception {
        if(index == -1){
            output.setShape(new Integer[]{});
            output.getChildren().add(input0);
            output.setOp(this);
            return;
        }
        Integer []shape = new Integer[input0.shape.length - 1];
        int ind = 0;
        for(int i = 0;i < input0.shape.length;i++){
            if(i != index)
                shape[ind++] = input0.shape[i];
        }
        output.setShape(shape);
        output.getChildren().add(input0);
        output.setOp(this);
    }
}