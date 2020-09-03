package com.seemingwang.machineLearning.DataProvider;

public class ArrayDataProvider implements DataProvider{

    public ArrayDataProvider(double[] data) {
        this.data = data;
        shape = new Integer[]{data.length};
    }

    double []data;
    Integer []shape;
    @Override
    public Integer[] getShape() {
        return shape;
    }

    @Override
    public double getData(Integer... Pos) {
        return data[Pos[0]];
    }


}
