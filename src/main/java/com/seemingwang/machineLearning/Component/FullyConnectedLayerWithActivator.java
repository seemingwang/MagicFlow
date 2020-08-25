package com.seemingwang.machineLearning.Component;

import com.seemingwang.machineLearning.FlowNode.FlowNodeBuilder.FullMatrixFlowNodeBuilder;
import com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass.FlowOpSingleParamReluForMatrixType;
import com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass.FlowOpSingleParamSigmoidForMatrixType;
import com.seemingwang.machineLearning.FlowNode.FlowOpSingle;
import com.seemingwang.machineLearning.FlowNode.FullMatrixFlowNode;
import com.seemingwang.machineLearning.FlowNode.MatrixFlowNode;
import com.seemingwang.machineLearning.OperationFactory.OperationFactory;
import com.seemingwang.machineLearning.Utils.Structure.Activator;

public class FullyConnectedLayerWithActivator {

    int count;

    public FullyConnectedLayerWithActivator() {
        this.count = 0;
    }

    MatrixFlowNode makeFullyConnectedLayerWithActivator(int row, int column, FlowOpSingle instance, MatrixFlowNode input) {
        count++;
        FullMatrixFlowNode weight = new FullMatrixFlowNodeBuilder(row, column).setTrainable(true).setName("weight" + count).build();
        try {
            FullMatrixFlowNode mul = (FullMatrixFlowNode)OperationFactory.Multiply(input,weight);
            mul.setName("mul" + count);
            FullMatrixFlowNode f = (FullMatrixFlowNode) OperationFactory.add(mul,new FullMatrixFlowNodeBuilder(1,column).setTrainable(true).setName("bias" + count).build());
            f.setName("linearOut" + count);
            if(instance != null) {
                FullMatrixFlowNode w = new FullMatrixFlowNodeBuilder(1, column).build();
                w.setName("activator" + count);
                OperationFactory.singleTypeMatrixFactory.func1(f, w, instance);
                return w;
            } else
                return f;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public FullMatrixFlowNode makeFullyConnectedLayerWithActivator(MatrixFlowNode input, int row, int column, Activator a) {
        FullMatrixFlowNode res;
        if(a == null)
            return (FullMatrixFlowNode) makeFullyConnectedLayerWithActivator(row,column, null,input);
        switch (a){
            case sigmoid: res = (FullMatrixFlowNode) makeFullyConnectedLayerWithActivator(row,column,FlowOpSingleParamSigmoidForMatrixType.instance,input);break;
            case relu:res = (FullMatrixFlowNode) makeFullyConnectedLayerWithActivator(row,column, FlowOpSingleParamReluForMatrixType.instance,input);break;
            default:res = (FullMatrixFlowNode) makeFullyConnectedLayerWithActivator(row,column, null,input);break;
        }
        return res;
    }

}
