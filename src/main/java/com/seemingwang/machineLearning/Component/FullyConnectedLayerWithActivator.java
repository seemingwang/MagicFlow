package com.seemingwang.machineLearning.Component;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowNodeBuilder;
import com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass.FlowOpRelu;
import com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass.FlowOpSigmoid;
import com.seemingwang.machineLearning.FlowNode.FlowOpSingleParam;
import com.seemingwang.machineLearning.OperationFactory.OperationFactory;
import com.seemingwang.machineLearning.Utils.Structure.Activator;

public class FullyConnectedLayerWithActivator {

    int count;

    public FullyConnectedLayerWithActivator() {
        this.count = 0;
    }

    FlowNode makeFullyConnectedLayerWithActivator(int row, int column, FlowOpSingleParam instance, FlowNode input) {
        count++;
        FlowNode weight = new FlowNodeBuilder().setShape(new Integer[]{row, column}).setTrainable(true).setName("weight" + count).build();
        try {
            FlowNode mul = OperationFactory.matMultiply(input,weight);
            mul.setName("mul" + count);
            FlowNode f = OperationFactory.add(mul,new FlowNodeBuilder().setShape(new Integer[]{1,column}).setTrainable(true).setName("bias" + count).build());
            f.setName("linearOut" + count);
            if(instance != null) {
                FlowNode w = new FlowNodeBuilder().setShape(new Integer[]{1,column}).build();
                w.setName("activator" + count);
                instance.connect(f,w);
                return w;
            } else
                return f;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public FlowNode makeFullyConnectedLayerWithActivator(FlowNode input, int row, int column, Activator a) {
        FlowNode res;
        if(a == null)
            return  makeFullyConnectedLayerWithActivator(row,column, null,input);
        switch (a){
            case sigmoid: res = makeFullyConnectedLayerWithActivator(row,column, FlowOpSigmoid.instance,input);break;
            case relu:res =  makeFullyConnectedLayerWithActivator(row,column, FlowOpRelu.instance,input);break;
            default:res =  makeFullyConnectedLayerWithActivator(row,column, null,input);break;
        }
        return res;
    }

}
