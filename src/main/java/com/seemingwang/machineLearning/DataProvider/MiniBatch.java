package com.seemingwang.machineLearning.DataProvider;

import com.seemingwang.machineLearning.Utils.Pair;

public abstract class MiniBatch {
    public abstract Pair<DataProvider,DataProvider> nextBatch();
}
