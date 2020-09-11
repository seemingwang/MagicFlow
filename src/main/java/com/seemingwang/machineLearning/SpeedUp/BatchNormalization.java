package com.seemingwang.machineLearning.SpeedUp;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowNodeBuilder;
import com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass.FlowOpReduceFlowingSum;
import com.seemingwang.machineLearning.FlowNode.FlowOp;
import com.seemingwang.machineLearning.FlowNode.SwitchFlowNode;
import com.seemingwang.machineLearning.OperationFactory.OperationFactory;

import java.util.Arrays;

public class BatchNormalization{
    public BatchNormalization(FlowNode input, SwitchFlowNode isTraining, double decay,int lastIndex,double eps) {
        this.isTraining = isTraining;
        this.decay = decay;
        this.input = input;
        this.lastIndex = lastIndex;
        this.eps = eps;
    }

    public SwitchFlowNode getIsTraining() {
        return isTraining;
    }

    public void setIsTraining(SwitchFlowNode isTraining) {
        this.isTraining = isTraining;
    }

    public static FlowNode makeBatchNormalizationNode(FlowNode input, SwitchFlowNode isTraining, double decay,int lastIndex,double eps){
        BatchNormalization bn = new BatchNormalization(input,isTraining,decay,lastIndex,eps);
        try {
            return bn.makeOutput(bn.normalize());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    SwitchFlowNode isTraining;
    FlowNode input;
    int lastIndex;
    final double decay,eps;
    FlowNode makeOutput(FlowNode norm){
        try{
            int blockSize = 1;
            for(int j = lastIndex + 1;j < input.shape.length;j++){
                blockSize *= input.shape[j];
            }
            FlowNode gamma,beta;
            gamma = new FlowNodeBuilder().setShape(new Integer[blockSize]).setTrainable(true).build();
            beta = new FlowNodeBuilder().setShape(new Integer[blockSize]).setTrainable(true).build();
            return OperationFactory.add(OperationFactory.multiply(norm,gamma),beta);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    FlowNode normalize() throws Exception {
        FlowNode mean = new FlowNode();
        FlowOpReduceFlowingSum opMean = new FlowOpReduceFlowingSum(lastIndex,true);
        opMean.connect(input,mean);
        mean.getChildren().add(isTraining);
        mean.setOp(new FlowOp() {
            @Override
            public void forward(FlowNode f) {
                SwitchFlowNode train = (SwitchFlowNode)f.getChildren().get(1);
                if(train.getBinarySwitch()){
                    opMean.forward(f);
                }
            }

            @Override
            public void backward(FlowNode f) {
                    opMean.backward(f);
            }
        });
        FlowNode movingMean = new FlowNodeBuilder().setShape(mean.shape.clone()).build();
        movingMean.setChildren(Arrays.asList(mean,isTraining));
        movingMean.setOp(new FlowOp() {
            @Override
            public void forward(FlowNode f) {
                SwitchFlowNode isTraining = (SwitchFlowNode)f.getChildren().get(1);
                if(isTraining.getBinarySwitch()) {
                    int size = f.getSize();
                    f.resetDataSize(size);
                    FlowNode c = f.getChildren().get(0);
                    for (int i = 0; i < size; i++) {
                        f.data[i] = f.data[i] * decay + c.data[i] * (1.0 - decay);
                    }
                }
            }

            @Override
            public void backward(FlowNode f) {

            }
        });
        FlowNode var = new FlowNodeBuilder().setShape(mean.shape.clone()).build();
        var.setChildren(Arrays.asList(input,mean,isTraining));
        var.setOp(new FlowOp() {
            @Override
            public void forward(FlowNode f) {
                FlowNode input0 = f.getChildren().get(0),mean = f.getChildren().get(1);
                SwitchFlowNode train = (SwitchFlowNode)f.getChildren().get(2);
                if(train.getBinarySwitch()) {
                    int totalSize = input0.getSize();
                    int blockSize = mean.getSize();
                    f.resetDataSize(blockSize);
                    for (int i = 0; i < blockSize; i++)
                        f.data[i] = 0;
                    int groupSize = totalSize / blockSize;
                    for (int i = 0; i < totalSize; i += blockSize) {
                        for (int j = 0; j < blockSize; j++) {
                            f.data[j] += (input0.data[i + j] - mean.data[j]) * (input0.data[i + j] - mean.data[j]);
                        }
                    }
                    for (int i = 0; i < blockSize; i++)
                        f.data[i] = f.data[i]/groupSize;
                }
            }

            @Override
            public void backward(FlowNode f) {
                FlowNode input0 = f.getChildren().get(0),mean = f.getChildren().get(1);
                int totalSize = input0.getSize();
                int blockSize = mean.getSize();
                mean.resetDevSize(blockSize);
                input0.resetDevSize(totalSize);
                int groupSize = totalSize / blockSize;
                for (int i = 0; i < totalSize; i += blockSize) {
                    for (int j = 0; j < blockSize; j++) {
                        double p = 2 * (input0.data[i + j] - mean.data[j])/groupSize * f.dev[j];
                        input0.dev[i+j] += p;
                        mean.dev[j] += -p;
                    }
                }
            }
        });
        FlowNode movingVar = new FlowNodeBuilder().setShape(var.shape.clone()).build();
        movingVar.setChildren(Arrays.asList(var,isTraining));
        movingVar.setOp(new FlowOp() {
            @Override
            public void forward(FlowNode f) {
                SwitchFlowNode isTraining = (SwitchFlowNode)f.getChildren().get(1);
                if(isTraining.getBinarySwitch()) {
                    int size = f.getSize();
                    f.resetDataSize(size);
                    FlowNode c = f.getChildren().get(0);
                    for (int i = 0; i < size; i++) {
                        f.data[i] = f.data[i] * decay + c.data[i] * (1.0 - decay);
                    }
                }
            }

            @Override
            public void backward(FlowNode f) {

            }
        });
        FlowNode norm = new FlowNodeBuilder().setShape(input.shape).build();
        norm.setChildren(Arrays.asList(input,mean,movingMean,var,movingVar,isTraining));
        norm.setOp(new FlowOp() {
            @Override
            public void forward(FlowNode f) {
                SwitchFlowNode train = (SwitchFlowNode)f.getChildren().get(5);
                FlowNode input = f.getChildren().get(0),mean,var;
                if(train.getBinarySwitch()){
                    mean = f.getChildren().get(1);
                    var = f.getChildren().get(3);
                } else {
                    mean = f.getChildren().get(2);
                    var = f.getChildren().get(4);
                }
                int size = input.getSize();
                int blockSize = mean.getSize();
                f.resetDataSize(size);
                for(int i = 0,j = 0;i < size;i++,j++) {
                    if(j == blockSize)
                        j = 0;
                    f.data[i] = (input.data[i] - mean.data[j]) / Math.sqrt(var.data[j] + eps);
                }
            }

            @Override
            public void backward(FlowNode f) {
                FlowNode input = norm.getChildren().get(0),mean =f .getChildren().get(1),var = f.getChildren().get(3);
                int size = input.getSize();
                int blockSize = mean.getSize();
                input.resetDevSize(size);
                mean.resetDevSize(blockSize);
                var.resetDevSize(blockSize);
                for(int i = 0,j = 0;i < size;i++,j++) {
                    if(j == blockSize)
                        j = 0;
                    double temp = Math.sqrt(var.data[j] + eps);
                    input.dev[i] += f.dev[i] * temp;
                    mean.dev[j]+= -f.dev[i]*temp;
                    var.dev[j] += -f.dev[i] * f.data[i] * 0.5 /(var.data[j] + eps);
                }
            }
        });
        return norm;
    }

}
