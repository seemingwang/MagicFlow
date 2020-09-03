package com.seemingwang.machineLearning.DataInitializer;

import com.seemingwang.machineLearning.FlowNode.FlowNode;

import java.util.Random;

public class RangeDataInitializer implements DataInitializer{

    Random r;

    double mean,dev,sqrtDev;
    public RangeDataInitializer(double mean,double dev) {
        r = new Random();
        this.mean = mean;
        this.dev = dev;
        this.sqrtDev = Math.sqrt(dev);
    }

    @Override
    public void initData(FlowNode a) {
        if(a == null || !a.isTrainable())
            return;
        int size = a.getSize();
        a.resetDataSize(size);
        for(int i = 0;i < size;i++)
            a.data[i] = r.nextGaussian()*sqrtDev + mean;
    }
}
