package com.seemingwang.machineLearning.Matrix;

public class FullMatrix extends Matrix {
    double [][]mat;

    public FullMatrix(int row, int column){
        this.row = row;
        this.column = column;
        mat = new double[row][column];
    }

    public FullMatrix(double [][]mat){
        this.mat = mat;
        row = mat.length;
        column = mat[0].length;
    }

    public double get(int x, int y) {
        return mat[x][y];
    }

    public double set(int x, int y, double c) {
        return mat[x][y] = c;
    }

    public Matrix Transpose() {
        FullMatrix trans = new FullMatrix(column, row);
        for(int i = 0;i < column;i++)
            for(int j = 0;j < row;j++)
                trans.set(i, j, get(j, i));
        return trans;
    }

    public Matrix add(Matrix a) throws MatrixException{
        if(a.getColumn() != getColumn() || a.getRow() != getRow()){
            throw new MatrixException("in matrix add operation, shapes don't match(" + getRow() + "," + getColumn() + ")(" + a.getRow() + "," + a.getColumn());
        }
        FullMatrix res = new FullMatrix(mat);
        for(int i = 0;i < a.getRow();i++) {
            for (int j = 0; j < a.getColumn(); j++) {
                res.set(i, j, res.get(i, j) + a.get(i, j));
            }
        }
        return res;
    }

    @Override
    public Matrix subtract(Matrix a) throws MatrixException {
        if(a.getColumn() != getColumn() || a.getRow() != getRow()){
            throw new MatrixException("in matrix subtract operation, shapes don't match(" + getRow() + "," + getColumn() + ")(" + a.getRow() + "," + a.getColumn());
        }
        FullMatrix res = new FullMatrix(mat);
        for(int i = 0;i < a.getRow();i++) {
            for (int j = 0; j < a.getColumn(); j++) {
                res.set(i, j, res.get(i, j) - a.get(i, j));
            }
        }
        return res;
    }

    public Matrix multiply(Matrix a) throws MatrixException {
        if(a.getRow() != getColumn()){
            throw new MatrixException("in matrix add operation, shapes don't match(" + getRow() + "," + getColumn() + ")(" + a.getRow() + "," + a.getColumn());
        }
        FullMatrix mat = new FullMatrix(getRow(),a.getColumn());
        for(int i = 0;i < getRow();i++){
            for(int j = 0;j < a.getColumn();j++){
                double tot = 0;
                for(int k = 0;k < a.getRow();k++)
                    tot += get(i,k) * a.get(k,j);
                mat.set(i, j, tot);
            }
        }
        return mat;
    }

    @Override
    public Matrix multiply(double a) throws MatrixException {
        FullMatrix res = new FullMatrix(mat);
        for(int i = 0;i < res.getRow();i++){
            for(int j = 0;j < res.getColumn();j++){
                res.set(i,j,a*res.get(i,j));
            }
        }
        return res;
    }
}

