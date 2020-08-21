package com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass;

import com.seemingwang.machineLearning.DerivativeDescriber.DerivativeDescriber;
import com.seemingwang.machineLearning.DerivativeDescriber.MatrixTypeDevDescriber;
import com.seemingwang.machineLearning.FlowNode.FlowOpSingle;
import com.seemingwang.machineLearning.Matrix.FullMatrix;
import com.seemingwang.machineLearning.Matrix.Matrix;
import com.seemingwang.machineLearning.Matrix.MatrixException;

import java.util.Arrays;
import java.util.List;

public class FlowOpSingleParamReluForMatrixType extends FlowOpSingle<Matrix,Matrix> {

    public static FlowOpSingleParamReluForMatrixType instance = new FlowOpSingleParamReluForMatrixType();

    @Override
    public Matrix cal(Matrix input) {
        FullMatrix f = new FullMatrix(input.getRow(),input.getColumn());
        for(int i = 0;i < input.getRow();i++){
            for(int j = 0;j < input.getColumn();j++){
                try {
                    f.set(i,j,Math.max(input.get(i,j),0));
                } catch (MatrixException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return f;
    }

    @Override
    public List<DerivativeDescriber> calDev(Matrix dev, Matrix input) {
        FullMatrix f = new FullMatrix(input.getRow(),input.getColumn());
        for(int i = 0;i < input.getRow();i++){
            for(int j = 0;j < input.getColumn();j++){
                try {
                    f.set(i,j,input.get(i,j) >= 0?dev.get(i,j):0);
                } catch (MatrixException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return Arrays.asList(new MatrixTypeDevDescriber(f));
    }
}
