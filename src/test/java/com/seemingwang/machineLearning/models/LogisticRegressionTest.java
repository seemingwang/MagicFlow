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
        int dimension = 20;
        LogisticRegression lr = new LogisticRegression(dimension, null);
        List<Double> w = new ArrayList<>();
        double [][] wi = new double[dimension][1];
        int size = 200;
        for(int i = 0;i < dimension;i++){
            w.add(getDouble(1.0));
        }
        Double bias = getDouble(1.0);
        double [][] in = new double[size][dimension];
        double []label = new double[size];
        for(int i = 0;i < size;i++){
            double tot = 0;
            for(int j = 0;j < dimension;j++) {
                in[i][j] = getDouble(10);
                tot += in[i][j] * w.get(j);
            }
            tot += bias;
            label[i] = sigmoid(tot);
        }
        lr.prepare(in,label);
        //lr.train(1);
        FullyConnectedTestUnit a = new FullyConnectedTestUnit(dimension,1);
        SigmoidTestUnit b = new SigmoidTestUnit();
        NetworkUnit.makePair(a,b);
        double []biasx = lr.bias.data.clone();
        a.setBias(biasx);
        for(int i = 0;i < dimension;i++)
            wi[i][0] = lr.weight.data[i];
        a.setWeight(wi);
        SquareError q = new SquareError(label);
        NetworkUnit.makePair(b,q);
        int batchSize = 200;
        double backdev [][] = new double [200][1];
        for(int i  = 0;i < 200;i++){
            backdev[i][0] = 1.0/batchSize;
        }
        int trainStep = 10000;
        for(int i = 0;i < trainStep;i++) {
            a.input = in;
            a.forward();
            q.backward(backdev, 0.1);
        }
        lr.train(trainStep);
        double tot = 0;
        for(int j = 0;j < batchSize;j++){
            tot += q.output[j][0];
        }
        Assert.assertEquals(tot * 1.0/batchSize,lr.gm.getCost(),1e-6);
//        lr.train(50000);
//        double[] trainedWeight = lr.exportWeight();
//        Double trainBias = lr.exportBias();
//        Assert.assertEquals(trainBias,bias,0.05);
//        for(int i = 0;i < dimension;i++)
//            Assert.assertEquals(w.get(i),trainedWeight[i],0.05);

//        for(int i = 0;i < in.length;i++){
//            System.out.println("label " + label[i]);
//            System.out.println(lr.Predict(in[i]));
//        }
//        double t = 0, t1 = 0;
//        for(int i = 0;i < dimension;i++){
//            t += w.get(i) * input.get(0).get(i);
//            t1 += trainedWeight.get(i) * input.get(0).get(i);
//        }
//        t+= bias;
//        t1+= trainBias;
    }

}