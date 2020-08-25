package com.seemingwang.machineLearning.models;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FullMatrixFlowNode;
import com.seemingwang.machineLearning.FlowNode.MatrixFlowNode;
import com.seemingwang.machineLearning.Matrix.FullMatrix;
import com.seemingwang.machineLearning.Matrix.Matrix;
import com.seemingwang.machineLearning.Matrix.MatrixException;
import com.seemingwang.machineLearning.Optimizer.GradientDescentOptimizer;
import com.seemingwang.machineLearning.Utils.Pair;
import com.seemingwang.machineLearning.Utils.Structure.Activator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
abstract class NetwordUnit {
    int shape[];
    public static void makePair(NetwordUnit a,NetwordUnit b){
        a.next = b;
        b.previous = a;
    }
    double [][] output,input,weight,wdev;;
    NetwordUnit previous, next;
    public static void reset(double [][] mat){
        for(int i = 0;i < mat.length;i++){
            for(int j = 0;j < mat[0].length;j++){
                mat[i][j] = 0;
            }
        }
    }
    abstract void cal();
    abstract void exportWeight();
    abstract double[][] calDev(double [][]dev,double learningRate);
    public void forward(){
        cal();
        if(next != null){
            next.input = output;
            next.forward();
        }
    }

    public void backward(double [][]dev,double learningRate) {
        double[][] dev2 = calDev(dev,learningRate);
        if (previous != null) {
            previous.backward(dev2,learningRate);
        }
    }

}
class SquareError extends NetwordUnit{

    public SquareError(double[] label) {
        this.label = label;
    }

    public double label[];

    @Override
    void cal() {
        output = new double [input.length][1];
        for(int i = 0; i < input.length;i++){
            output[i][0] = (input[i][0] - label[i]) * (input[i][0] - label[i]);
        }
    }

    @Override
    void exportWeight() {

    }

    @Override
    double[][] calDev(double[][] dev, double learningRate) {
        double [][] w = new double[dev.length][1];
        for(int i = 0;i < dev.length;i++){
            w[i][0] = 2.0 * (input[i][0] - label[i]) * dev[i][0];
        }
        return w;
    }
}

class FullyConnectedTestUnit extends NetwordUnit{

    public void setWeight(double[][] weight) {
        this.weight = weight;
    }

    public void setBias(double []bias){
        this.bias = bias;
    }
    FullyConnectedTestUnit(int r,int c){
        shape = new int[]{r,c};
        weight = new double [r][c];
    }

    public double bias[],wbias[];

    @Override
    void cal() {
        int r1 = input.length,c = shape[1],r = shape[0];
        output = new double [r1][c];
        reset(output);
        for(int i = 0;i < r1;i++){
            for(int j = 0;j < c;j++){
                for(int k = 0;k < r;k++){
                    output[i][j] += input[i][k] * weight[k][j];
                }
                output[i][j] += bias[j];
            }
        }
    }

    @Override
    void exportWeight() {
        System.out.println("weight matrix");
        for(int i = 0;i < weight.length;i++){
            for(int j = 0;j < weight[0].length;j++){
                System.out.print(weight[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("wbias");
        for(int i = 0;i < bias.length;i++){
            System.out.print(bias[i] + " ");
        }
        System.out.println();
    }

    @Override
    double[][] calDev(double[][] dev,double learningRate) {
        int r1 = input.length,c = shape[1],r = shape[0];
        double dev2[][] = new double[r1][r];
        wdev= new double[r][c];
        wbias = new double [c];
        reset(dev2);
        reset(wdev);
        for(int i = 0;i < r1;i++){
            for(int j = 0;j < c;j++){
                for(int k = 0;k < r;k++){
                    //output[i][j] += input[i][k] * weight[k][j];
                    dev2[i][k] += dev[i][j] * weight[k][j];
                    wdev[k][j] += dev[i][j] * input[i][k];

                }
                wbias[j] += dev[i][j];
            }
        }
        for(int i = 0;i < r;i++){
            for(int j = 0;j < c;j++){
                weight[i][j] -= wdev[i][j] * learningRate;
            }
        }
        for(int i = 0;i < c;i++)
            bias[i] -= wbias[i] * learningRate;
        return dev2;
    }
}

class SigmoidTestUnit extends NetwordUnit{

    double sigmoid(double x){
        return Math.exp(x)/(1.0 + Math.exp(x));
    }
    @Override
    void cal() {
        int r = input.length, c = input[0].length;
        output = new double[r][c];
        for(int i = 0;i < r;i++){
            for(int j = 0;j < c;j++){
                output[i][j] = sigmoid(input[i][j]);
            }
        }
    }

    @Override
    void exportWeight() {

    }

    @Override
    double[][] calDev(double[][] dev,double l) {
        int r = dev.length, c = dev[0].length;
        double [][] dev2 = new double[r][c];
        for(int i = 0;i < r;i++){
            for(int j =0;j < c;j++){
                dev2[i][j] = sigmoid(input[i][j]) * (1.0 - sigmoid(input[i][j])) * dev[i][j];
            }
        }
        return dev2;
    }
}
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

    public double[][] MatrixWeightToArray(MatrixFlowNode a){
        Matrix t = a.getData().get(0);
        int r = t.getRow(), c = t.getColumn();
            double[][] data = new double[r][c];
            for (int i = 0; i < r; i++) {
                for (int j = 0; j < c; j++) {
                    try {
                        data[i][j] = t.get(i, j);
                    } catch (MatrixException e) {
                        e.printStackTrace();
                    }
                }
            }
            return data;
        }

    public double[] MatrixBiasToArray(MatrixFlowNode a){
        Matrix t = a.getData().get(0);
        int  c = t.getColumn();
        double[] data = new double[c];
            for (int j = 0; j < c; j++) {
                try {
                    data[j] = t.get(0, j);
                } catch (MatrixException e) {
                    e.printStackTrace();
                }
            }

        return data;
    }

    @Test
    public void TestFullyConnectedNetwork1(){
        Pair<List<List<Double>>,List<Double>> t = makeTestExample();
        FullyConnectedNetworkWithScalaOutput f = new FullyConnectedNetworkWithScalaOutput(2, new int[]{2, 32, 32,1}, Activator.sigmoid,new GradientDescentOptimizer(0.01));
        f.prepare(t.first,t.second);
        try {
            f.gm.initData();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        for(int i = 0;i < 10000;i++) {
            f.train(100);
            f.gm.run(f.output);
            for(int j = 0;j < 30;j++){
                for(int k = 0;k < 30;k++){
                    double x= ((FullMatrix)f.output.getData().get(j * 30 + k)).get(0,0);
                    System.out.print(belongTo(x) + " ");
                }
                System.out.println();
            }
            System.out.println(f.gm.getCost());
        }
    }
    @Test
    public void TestFullyConnectedNetwork(){
        Pair<List<List<Double>>,List<Double>> t = makeTestExample();
        FullyConnectedNetworkWithScalaOutput f = new FullyConnectedNetworkWithScalaOutput(2, new int[]{2, 16, 16, 8,8,1}, Activator.sigmoid,new GradientDescentOptimizer(0.01));
        FullyConnectedTestUnit a = new FullyConnectedTestUnit(2,16), c = new FullyConnectedTestUnit(16,8), e = new FullyConnectedTestUnit(8,1);
        SigmoidTestUnit b = new SigmoidTestUnit(),d = new SigmoidTestUnit();
        NetwordUnit.makePair(a,b);
        NetwordUnit.makePair(b,c);
        NetwordUnit.makePair(c,d);
        NetwordUnit.makePair(d,e);
        SquareError q = new SquareError(ListToArray(t.second));
        double[][] unitInput = ListToMatrix(t.first);
        NetwordUnit.makePair(e,q);
        int batchSize = 900;
        f.prepare(t.first,t.second);
        try {
            f.gm.initData();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        FullMatrixFlowNode weight1 = null;
        for(FlowNode x:f.gm.exeSeq){
            if(x.getName().equals("weight1")){
                a.setWeight(MatrixWeightToArray((MatrixFlowNode) x));
                weight1 = (FullMatrixFlowNode) x;
            } else if(x.getName().equals("bias1")){
                a.setBias(MatrixBiasToArray((MatrixFlowNode) x));
            } else  if(x.getName().equals("weight2")){
                c.setWeight(MatrixWeightToArray((MatrixFlowNode) x));
            } else if(x.getName().equals("bias2")){
                c.setBias(MatrixBiasToArray((MatrixFlowNode) x));
            } else if(x.getName().equals("weight3")){
                e.setWeight(MatrixWeightToArray((MatrixFlowNode) x));
            } else if(x.getName().equals("bias3")){
                e.setBias(MatrixBiasToArray((MatrixFlowNode) x));
            }
        }
        double backdev [][] = new double [900][1];
        for(int i  = 0;i < 900;i++){
            backdev[i][0] = 1.0/batchSize;
        }
        f.train(10);
        for(int i = 0;i < 10;i++) {
            a.input = unitInput;
            a.forward();
            q.backward(backdev, 0.01);
        }
        int row = a.weight.length, col = a.weight[0].length;
        FullMatrix f2 = (FullMatrix)weight1.getData().get(0);
        for(int i = 0;i < row;i++){
            for(int j = 0;j < col;j++){
                Assert.assertEquals(a.weight[i][j],f2.get(i,j),0.00001);
            }
        }



    }

}