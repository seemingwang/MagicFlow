package com.seemingwang.machineLearning.Component;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowNodeBuilder.FullMatrixFlowNodeBuilder;
import com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass.FlowOpSingleParamSigmoidForMatrixType;
import com.seemingwang.machineLearning.FlowNode.FlowOpSingle;
import com.seemingwang.machineLearning.FlowNode.FullMatrixFlowNode;
import com.seemingwang.machineLearning.FlowNode.MatrixFlowNode;
import com.seemingwang.machineLearning.OperationFactory.OperationFactory;

public class FullyConnectedWithActivator {

    static MatrixFlowNode makeFullyConnectedWithActivator(int row, int column, FlowOpSingle instance, MatrixFlowNode input) {
        FullMatrixFlowNode weight = new FullMatrixFlowNodeBuilder(row, column).setTrainable(true).build();
        try {
            FlowNode f = OperationFactory.add(OperationFactory.Multiply(input,weight),new FullMatrixFlowNodeBuilder(1,column).setTrainable(true).build());
            FullMatrixFlowNode w = new FullMatrixFlowNodeBuilder(row, column).setTrainable(true).build();
            OperationFactory.singleTypeMatrixFactory.func1(f,w,instance);
            return w;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static FullMatrixFlowNode makeFullyConnectedWithSigmoid(int row, int column, MatrixFlowNode input) {
        FullMatrixFlowNode weight = new FullMatrixFlowNodeBuilder(row, column).setTrainable(true).build();
        try {
            FlowNode f = OperationFactory.add(OperationFactory.Multiply(input,weight),new FullMatrixFlowNodeBuilder(1,column).setTrainable(true).build());
            FullMatrixFlowNode w = new FullMatrixFlowNodeBuilder(row, column).setTrainable(true).build();
            OperationFactory.singleTypeMatrixFactory.func1(f,w, FlowOpSingleParamSigmoidForMatrixType.instance);
            return w;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}