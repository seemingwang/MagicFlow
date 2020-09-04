package com.seemingwang.machineLearning.DataProvider;

public class TwoDArrayDataProvider implements DataProvider{

    public TwoDArrayDataProvider(double[][] data) {
        this.data = data;
        shape = new Integer[]{data.length,data[0].length};
    }

    public TwoDArrayDataProvider(double[] data,int r,int c) {
        this.data = new double[r][c];
        for(int i = 0;i < r;i++){
            for(int j = 0;j < c;j++)
                this.data[i][j] = data[i *c + j];
        }
        shape = new Integer[]{r,c};
    }

    double [][]data;
    Integer []shape;
    @Override
    public Integer[] getShape() {
        return shape;
    }

    @Override
    public double getData(Integer... Pos) {
        return data[Pos[0]][Pos[1]];
    }


}

