package com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass;

import com.seemingwang.machineLearning.DerivativeDescriber.DerivativeDescriber;
import com.seemingwang.machineLearning.DerivativeDescriber.MatrixTypeDevDescriber;
import com.seemingwang.machineLearning.FlowNode.FlowOpDouble;
import com.seemingwang.machineLearning.Matrix.Matrix;
import com.seemingwang.machineLearning.Matrix.MatrixException;

import java.util.Arrays;
import java.util.List;

public class FlowOpSingleParamMultiplyForMatrixType extends FlowOpDouble<Matrix,Matrix,Matrix> {

    public static FlowOpSingleParamMultiplyForMatrixType instance = new FlowOpSingleParamMultiplyForMatrixType();
    @Override
    public Matrix cal(Matrix input0, Matrix input1) {
        try {
            return input0.multiply(input1);
        } catch (MatrixException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<DerivativeDescriber> calDev(Matrix dev, Matrix input0, Matrix input1) {
        List l = null;
        try {
            l = Arrays.asList(new MatrixTypeDevDescriber(dev.multiply(input1.Transpose())),new MatrixTypeDevDescriber(input0.Transpose().multiply(dev)));
        } catch (MatrixException e) {
            e.printStackTrace();
        }
        return l;

    }
}
