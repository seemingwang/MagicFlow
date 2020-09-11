package com.seemingwang.machineLearning.models;

import com.seemingwang.machineLearning.DataProvider.TwoDArrayDataProvider;
import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.Optimizer.GradientDescentOptimizer;
import com.seemingwang.machineLearning.Utils.Pair;
import com.seemingwang.machineLearning.Utils.Structure.Activator;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class FullyConnectedNetworkWithScalaOutputTest {

    int belongTo(double a){
        if(a < 0.5)
            return 0;
        else if(a >= 0.5 && a < 1.5)
            return 1;
        else if(a >= 1.5 && a < 2.5)
            return 2;
        else if(a >= 2.5 && a < 3.5)
            return 3;
        return 4;
    }
    public static double[][] ListToMatrix(List<List<Double>> l){
        int r = l.size(), c = l.get(0).size();
        double w[][] = new double[r][c];
        for(int i = 0;i < r;i++){
            for(int j = 0;j < c;j++){
                w[i][j] = l.get(i).get(j);
            }
        }
        return w;
    }

    public static double[] ListToArray(List<Double> l){
        int r = l.size();
        double w[] = new double[r];
        for(int i = 0;i < r;i++){
            w[i] = l.get(i);
        }
        return w;
    }

    public static double[][] ArraySplit(double []a,int r,int c){
        double [][] res = new double[r][c];
        for(int i = 0;i < r;i++){
            for(int j = 0;j < c;j++){
                res[i][j] = a[i * c + j];
            }
        }
        return res;
    }

    public Pair<List<List<Double>>,List<Double>> makeTestExample(){
        List<List<Double>> data = new ArrayList<>();
        List<Double> label = new ArrayList<>();
        for(int i = 0;i < 30;i++){
            for(int j = 0;j < 30;j++){
                if((i >= 3 && i <= 12 || i >= 18 && i <= 27) && (j >= 3 && j <= 12 || j >= 18 && j <= 27)){
                    double t = 0;
                    if((i >= 3 && i <= 12 ) && j >= 3 && j <= 12)
                        t = 1.0;
                    else if((i >= 18 && i <= 27) && (j >= 3 && j <= 12 ))
                        t = 2.0;
                    else if((i >= 3 && i <= 12) && (j >= 18 && j <= 27))
                        t = 3.0;
                    else
                        t = 4.0;
                    label.add(t);
                } else
                    label.add(0.0);
                data.add(Arrays.asList(i/30.0,j/30.0));
            }
        }
        return new Pair<>(data,label);
    }

    @Test
    public void TestFullyConnectedNetwork1(){
        Pair<List<List<Double>>,List<Double>> t = makeTestExample();
        FullyConnectedNetworkWithScalaOutput f = new FullyConnectedNetworkWithScalaOutput(2, new int[]{2, 32, 32,16,16,1}, Activator.sigmoid,new GradientDescentOptimizer(0.001));
        try {
            f.gm.initData();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        double [][] data = ListToMatrix(t.first);
        double []label = ListToArray(t.second);
        f.prepare(data,label,30);
        Map<FlowNode,Object>m = new HashMap<>();
        m.put(f.input,new TwoDArrayDataProvider(data));
        m.put(f.label,new TwoDArrayDataProvider(label,label.length,1));
        for(int i = 0;i < 1000000;i++) {
            f.train(10000);
            try {
                f.gm.feed(m);
                f.gm.run(f.output);
                for(int j = 0;j < 30;j++){
                    for(int k = 0;k < 30;k++){
                        double x= f.output.data[j * 30 + k];
                        System.out.print(belongTo(x) + " ");
                    }
                    System.out.println();
                }
                System.out.println(f.gm.getCost());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void TestFullyConnectedNetwork(){
        Pair<List<List<Double>>,List<Double>> t = makeTestExample();
        FullyConnectedNetworkWithScalaOutput f = new FullyConnectedNetworkWithScalaOutput(2, new int[]{2, 16, 16, 8,8,1}, Activator.sigmoid,new GradientDescentOptimizer(0.01));
        FullyConnectedTestUnit a = new FullyConnectedTestUnit(2,16), c = new FullyConnectedTestUnit(16,8), e = new FullyConnectedTestUnit(8,1);
        SigmoidTestUnit b = new SigmoidTestUnit(),d = new SigmoidTestUnit();
        NetworkUnit.makePair(a,b);
        NetworkUnit.makePair(b,c);
        NetworkUnit.makePair(c,d);
        NetworkUnit.makePair(d,e);
        SquareError q = new SquareError(ListToArray(t.second));
        double[][] unitInput = ListToMatrix(t.first);
        NetworkUnit.makePair(e,q);
        int batchSize = 900;
        f.prepare(unitInput,ListToArray(t.second),batchSize);
        try {
            f.gm.initData();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        FlowNode weight1 = null;
        for(FlowNode x:f.gm.exeSeq){
            if(x.getName().equals("weight1")){
                a.setWeight(ArraySplit(x.data,x.getShape()[0],x.getShape()[1]));
                weight1 = x;
            } else if(x.getName().equals("bias1")){
                a.setBias(x.data);
            } else  if(x.getName().equals("weight2")){
                c.setWeight(ArraySplit(x.data,x.getShape()[0],x.getShape()[1]));
            } else if(x.getName().equals("bias2")){
                c.setBias(x.data);
            } else if(x.getName().equals("weight3")){
                e.setWeight(ArraySplit(x.data,x.getShape()[0],x.getShape()[1]));
            } else if(x.getName().equals("bias3")){
                e.setBias(x.data);
            }
        }
        double backdev [][] = new double [batchSize][1];
        for(int i  = 0;i < batchSize;i++){
            backdev[i][0] = 1.0/batchSize;
        }
        f.train(100);
        for(int i = 0;i < 100;i++) {
            a.input = unitInput;
            a.forward();
            q.backward(backdev, 0.01);
        }
        int row = a.weight.length, col = a.weight[0].length;
        double[][] f2 = ArraySplit(weight1.data,weight1.getShape()[0],weight1.getShape()[1]);
        for(int i = 0;i < row;i++){
            for(int j = 0;j < col;j++){
                Assert.assertEquals(a.weight[i][j],f2[i][j],0.00001);
            }
        }

    }

}