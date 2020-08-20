package com.seemingwang.machineLearning.DerivativeDescriber;

import com.sun.tools.javac.util.Pair;

import java.util.Arrays;
import java.util.List;

public class DoubleTypeDevDescriber extends recoverableDevDescriber<Double>{

    public DoubleTypeDevDescriber(Double devData) {
        super(devData);
    }

    @Override
    public List<Pair<Integer, Double>> getDevList() {
        return Arrays.asList(new Pair<Integer,Double>(0,devData));
    }

    @Override
    public DerivativeDescriber combine(DerivativeDescriber a) {
//        if(a instanceof MatrixTypeDevDescriber){
//            try {
//                return new DoubleTypeDevDescriber(this.devData + ((MatrixTypeDevDescriber)a).devData.get(0,0));
//            } catch (MatrixException e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
        return new DoubleTypeDevDescriber(this.devData + ((DoubleTypeDevDescriber)a).devData);
    }


}
