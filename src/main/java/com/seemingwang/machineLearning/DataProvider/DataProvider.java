package com.seemingwang.machineLearning.DataProvider;

public interface DataProvider {
    Integer[] getShape();
    double getData(Integer ... Pos);
}
