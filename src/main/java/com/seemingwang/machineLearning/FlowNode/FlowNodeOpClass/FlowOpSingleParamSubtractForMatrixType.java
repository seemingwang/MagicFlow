package com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass;

import com.seemingwang.machineLearning.DerivativeDescriber.DerivativeDescriber;
import com.seemingwang.machineLearning.DerivativeDescriber.MatrixTypeDevDescriber;
import com.seemingwang.machineLearning.FlowNode.FlowOpDouble;
import com.seemingwang.machineLearning.Matrix.Matrix;
import com.seemingwang.machineLearning.Matrix.MatrixException;

import java.util.Arrays;
import java.util.List;

public class FlowOpSingleParamSubtractForMatrixType extends FlowOpDouble<Matrix,Matrix,Matrix> {

    public static FlowOpSingleParamSubtractForMatrixType instance = new FlowOpSingleParamSubtractForMatrixType();


    @Override
    public Matrix cal(Matrix input0, Matrix input1) {
        try {
            return input0.subtract(input1);
        } catch (MatrixException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<DerivativeDescriber> calDev(Matrix dev, Matrix input0, Matrix input1) {
        try {
            return Arrays.asList(new MatrixTypeDevDescriber(dev),new MatrixTypeDevDescriber(dev.multiply(-1)));
        } catch (MatrixException e) {
            e.printStackTrace();
            return null;
        }
    }
}
