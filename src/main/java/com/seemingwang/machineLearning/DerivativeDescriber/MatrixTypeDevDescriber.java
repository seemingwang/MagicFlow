package com.seemingwang.machineLearning.DerivativeDescriber;

import com.seemingwang.machineLearning.Matrix.Matrix;
import com.seemingwang.machineLearning.Matrix.MatrixException;
import com.sun.tools.javac.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class MatrixTypeDevDescriber extends recoverableDevDescriber<Matrix>{

    public MatrixTypeDevDescriber(Matrix devData) {
        super(devData);
    }

    @Override
    public List<Pair<Integer, Double>> getDevList() {
        int pos = 0;
        List<Pair<Integer, Double>> list = new ArrayList<>();
        for(int i = 0;i < devData.getRow();i++){
            for(int j = 0;j < devData.getColumn();j++){
                try {
                    list.add(new Pair<>(pos,devData.get(i,j)));
                    pos++;
                } catch (MatrixException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    @Override
    public DerivativeDescriber combine(DerivativeDescriber a) {
        try {
            DerivativeDescriber l =  new MatrixTypeDevDescriber(this.getDevData().add(((MatrixTypeDevDescriber)a).getDevData()));
            return l;
        } catch (MatrixException e) {
            e.printStackTrace();
            return null;
        }
    }
}
