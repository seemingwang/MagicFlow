package com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass;

import com.seemingwang.machineLearning.DerivativeDescriber.DerivativeDescriber;
import com.seemingwang.machineLearning.DerivativeDescriber.MatrixTypeDevDescriber;
import com.seemingwang.machineLearning.FlowNode.FlowOpSingle;
import com.seemingwang.machineLearning.Matrix.FullMatrix;
import com.seemingwang.machineLearning.Matrix.Matrix;
import com.seemingwang.machineLearning.Matrix.MatrixException;

import java.util.Arrays;
import java.util.List;

public class FlowOpSingleParamSigmoidForMatrixType  extends FlowOpSingle<Matrix,Matrix> {

    public static FlowOpSingleParamSigmoidForMatrixType instance = new FlowOpSingleParamSigmoidForMatrixType();

    public double sigmoid(double a){
        double exp = Math.exp(a);
        return exp / (exp + 1);
    }

    @Override
    public Matrix cal(Matrix input) {
        FullMatrix f = new FullMatrix(input.getRow(),input.getColumn());
        for(int i = 0;i < input.getRow();i++){
            for(int j = 0;j < input.getColumn();j++){
                try {
                    f.set(i,j,sigmoid(input.get(i,j)));
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
                    double sig = sigmoid(input.get(i,j));
                    f.set(i,j,dev.get(i,j)*sig*(1-sig));
                } catch (MatrixException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return Arrays.asList(new MatrixTypeDevDescriber(f));
    }
}
