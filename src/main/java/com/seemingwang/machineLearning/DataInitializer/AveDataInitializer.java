package com.seemingwang.machineLearning.DataInitializer;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FullMatrixFlowNode;
import com.seemingwang.machineLearning.FlowNode.ScalaFlowNode;
import com.seemingwang.machineLearning.Matrix.FullMatrix;

import java.util.Arrays;
import java.util.Random;

public class AveDataInitializer implements DataInitializer{

    Random r;

    public AveDataInitializer() {
        r = new Random();
    }

    @Override
    public void initData(FlowNode a) {
        if(a == null || !a.isTrainable())
            return;
        a.getData().clear();
        if(a instanceof ScalaFlowNode){
            a.setData(Arrays.asList(r.nextDouble()));
        } else if(a instanceof FullMatrixFlowNode){
            int []shape = ((FullMatrixFlowNode)a).getShape();
            FullMatrix f = new FullMatrix(shape[0],shape[1]);
            for(int i = 0;i < shape[0];i++){
                for(int j = 0;j < shape[1];j++){
                    f.set(i,j, r.nextDouble());
                }
            }
            a.setData(Arrays.asList(f));
        }
    }
}
