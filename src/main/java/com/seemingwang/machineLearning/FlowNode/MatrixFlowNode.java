package com.seemingwang.machineLearning.FlowNode;

import com.seemingwang.machineLearning.Matrix.Matrix;

import java.util.ArrayList;

public abstract class MatrixFlowNode extends FlowNode<Matrix> {

    public int []shape;

    public MatrixFlowNode(int row,int col) {
        this.shape = new int[2];
        shape[0] = row;
        shape[1] = col;
        data = new ArrayList<>();
    }


    @Override
    public int[] getShape() {
        return shape;
    }
}
