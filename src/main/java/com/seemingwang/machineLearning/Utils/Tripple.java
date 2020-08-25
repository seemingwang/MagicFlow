package com.seemingwang.machineLearning.Utils;

public class Tripple<A,B,C> extends Pair<A,B> {
    public C third;

    public Tripple(A first, B second, C third) {
        super(first, second);
        this.third = third;
    }
}
