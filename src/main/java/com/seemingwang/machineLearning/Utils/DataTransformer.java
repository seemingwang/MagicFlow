package com.seemingwang.machineLearning.Utils;

public class DataTransformer {
    static public double[] MatrixToArr(double[][] l){
        int row = l.length,col = l[0].length;
        double [] arr = new double[row * col];
        for(int i = 0;i < row;i++){
            for(int j = 0;j < col;j++)
                arr[i * col + j] = l[i][j];
        }
        return arr;
    }
}

