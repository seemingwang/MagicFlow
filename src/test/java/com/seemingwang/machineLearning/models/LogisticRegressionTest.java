package com.seemingwang.machineLearning.models;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LogisticRegressionTest {
    public double getDouble(double range){
        return new Random().nextDouble() * range - range/2;
    }

    public double sigmoid(double x){
        return Math.exp(x)/(1 + Math.exp(x));
    }
    @Test
    public void TestExample(){
        int dimension = 50;
        LogisticRegression lr = new LogisticRegression(dimension, null);
        List<Double> w = new ArrayList<>(),label = new ArrayList<>();
        int size = 200;
        List<List<Double>> input = new ArrayList<>();
        for(int i = 0;i < dimension;i++){
            w.add(getDouble(1.0));
        }
        Double bias = getDouble(1.0);
        for(int i = 0;i < size;i++){
            List<Double> in = new ArrayList<>();
            double tot = 0;
            for(int j = 0;j < dimension;j++) {
                in.add(getDouble(10));
                tot += in.get(j) * w.get(j);
            }
            tot += bias;
            input.add(in);
            label.add(sigmoid(tot));
        }
        lr.prepare(input,label);
        lr.train();
        List<Double> trainedWeight = lr.exportWeight();
        Double trainBias = lr.exportBias();
        Assert.assertEquals(trainBias,bias,0.05);
        for(int i = 0;i < dimension;i++)
            Assert.assertEquals(w.get(i),trainedWeight.get(i),0.05);
//        double t = 0, t1 = 0;
//        for(int i = 0;i < dimension;i++){
//            t += w.get(i) * input.get(0).get(i);
//            t1 += trainedWeight.get(i) * input.get(0).get(i);
//        }
//        t+= bias;
//        t1+= trainBias;
    }

}