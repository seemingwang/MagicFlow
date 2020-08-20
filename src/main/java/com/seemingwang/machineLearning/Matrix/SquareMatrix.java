package com.seemingwang.machineLearning.Matrix;

public abstract class SquareMatrix extends Matrix{
    SquareMatrix(int n){
        this.column = this.row = n;
    }

    abstract SquareMatrix reverse();
}
