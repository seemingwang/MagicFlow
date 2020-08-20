package com.seemingwang.machineLearning.FlowNode;

import com.seemingwang.machineLearning.Matrix.Matrix;
import com.seemingwang.machineLearning.Matrix.MatrixException;

public class FullMatrixFlowNode extends MatrixFlowNode{

    public FullMatrixFlowNode(int row, int col) {
        super(row, col);
    }

    @Override
    public void updateDev(Matrix dev, double learningRate) {
        try {
            this.getData().set(0,this.getData().get(0).subtract(dev.multiply(learningRate)));
        } catch (MatrixException e) {
            e.printStackTrace();
        }
    }

}
