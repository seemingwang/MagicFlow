package com.seemingwang.machineLearning.DerivativeDescriber;

import com.sun.tools.javac.util.Pair;

import java.util.List;

public abstract class  DerivativeDescriber<T> {
    abstract public List<Pair<Integer, Double>> getDevList();
    abstract public T export();
    abstract public DerivativeDescriber combine(DerivativeDescriber a);

}
