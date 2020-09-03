package com.seemingwang.machineLearning.DataProvider;

public class TwoDArrayDataProvider implements DataProvider{

    public TwoDArrayDataProvider(double[][] data) {
        this.data = data;
        shape = new Integer[]{data.length,data[0].length};
    }

    public TwoDArrayDataProvider(double[] data) {
        this.data = new double[1][data.length];
        this.data[0] = data;
        shape = new Integer[]{1,data.length};
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

