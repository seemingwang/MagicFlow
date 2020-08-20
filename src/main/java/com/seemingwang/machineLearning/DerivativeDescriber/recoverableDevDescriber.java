package com.seemingwang.machineLearning.DerivativeDescriber;

public abstract class recoverableDevDescriber<T> extends DerivativeDescriber<T> {
    public T getDevData() {
        return devData;
    }

    public void setDevData(T devData) {
        this.devData = devData;
    }


    public recoverableDevDescriber(T devData) {
        this.devData = devData;
    }

    public T devData;

    @Override
    public T export(){
        return getDevData();
    }
}
