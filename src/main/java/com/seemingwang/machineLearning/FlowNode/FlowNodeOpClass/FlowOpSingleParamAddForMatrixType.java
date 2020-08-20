package com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass;

import com.seemingwang.machineLearning.DerivativeDescriber.DerivativeDescriber;
import com.seemingwang.machineLearning.DerivativeDescriber.MatrixTypeDevDescriber;
import com.seemingwang.machineLearning.FlowNode.FlowOpDouble;
import com.seemingwang.machineLearning.Matrix.Matrix;
import com.seemingwang.machineLearning.Matrix.MatrixException;

import java.util.Arrays;
import java.util.List;

public class FlowOpSingleParamAddForMatrixType extends FlowOpDouble<Matrix,Matrix,Matrix> {
    public static FlowOpSingleParamAddForMatrixType instance = new FlowOpSingleParamAddForMatrixType();
    @Override
    public Matrix cal(Matrix input0, Matrix input1) {
        try {
            return input0.add(input1);
        } catch (MatrixException matrixException) {
            matrixException.printStackTrace();
            return null;
        }
    }

    @Override
    public List<DerivativeDescriber> calDev(Matrix dev, Matrix input0, Matrix input1) {
        return Arrays.asList(new MatrixTypeDevDescriber(dev),
                new MatrixTypeDevDescriber(dev)
        );
    }
}
