package com.seemingwang.machineLearning.DataInitializer;

import com.seemingwang.machineLearning.FlowNode.FlowNode;

import java.util.Random;

public class RangeDataInitializer implements DataInitializer{

    Random r;

    double up,down;
    public RangeDataInitializer(double up,double down) {
        r = new Random();
        this.up = up;
        this.down = down;
    }

    @Override
    public void initData(FlowNode a) {
        if(a == null || !a.isTrainable())
            return;
        int size = a.getSize();
        a.resetDataSize(size);
        for(int i = 0;i < size;i++)
            a.data[i] = r.nextDouble() * (up - down) + down;
    }
}
