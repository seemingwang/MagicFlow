package com.seemingwang.machineLearning.Matrix;

public abstract class Matrix {
    int row,column;

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
    abstract public double get(int x,int y) throws MatrixException;
    abstract public double set(int x,int y,double c) throws MatrixException;
    abstract  public Matrix Transpose();
    abstract public Matrix add(Matrix a) throws MatrixException;
    abstract public Matrix subtract(Matrix a) throws MatrixException;
    abstract public Matrix multiply(Matrix a) throws MatrixException;
    abstract public Matrix multiply(double a) throws MatrixException;

    @Override
    public boolean equals(Object o){
        Matrix m = (Matrix)o;
        if(m.getRow() != getRow() && m.getColumn() != getColumn())
            return false;
        try {
            for(int i = 0;i < getRow();i++){
                for(int j = 0;j < getColumn();j++){
                    if(Math.abs(m.get(i,j) - get(i,j)) > 1e-7)
                        return false;
                    }
                }
        } catch (MatrixException matrixExcepiton) {
            matrixExcepiton.printStackTrace();
            return false;
        }
        return true;
    }
}

