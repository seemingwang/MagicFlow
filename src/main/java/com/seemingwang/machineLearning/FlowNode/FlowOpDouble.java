package com.seemingwang.machineLearning.FlowNode;

import com.seemingwang.machineLearning.DerivativeDescriber.DerivativeDescriber;

import java.util.ArrayList;
import java.util.List;

public abstract class FlowOpDouble<T,L,R> implements FlowOp<T> {
    public abstract T cal( L input0, R input1);
    public abstract List<DerivativeDescriber> calDev(T dev,L input0, R input1);

    @Override
    public void forward(FlowNode<T> f,int size){
        if(!f.getChildren().isEmpty() && f.getChildren().size() >= 2) {
            List<T> l = f.getData() == null ? new ArrayList<>():f.getData();
            if(l.size() == 0){
                for(int i =0;i < size;i++)
                    l.add(null);
            }
            boolean leftTrain = f.getChildren().get(0).isTrainable(), rightTrain = f.getChildren().get(1).isTrainable();
            int leftPos = 0, rightPos = 0;
            for(int i = 0;i < size;i++){
                l.set(i,cal((L) f.getChildren().get(0).getData().get(leftPos), (R) f.getChildren().get(1).getData().get(rightPos)));
                if(!leftTrain)
                    leftPos++;
                if(!rightTrain)
                    rightPos++;
            }
            f.setData(l);
        }
    }

    @Override
    public List<List<DerivativeDescriber>> backward(FlowNode<T> f, List<T> dev){
        List<List<DerivativeDescriber>> l = new ArrayList<>();
        if(!f.getChildren().isEmpty() && f.getChildren().size() >= 2){
            int size = dev.size();
            l.add(new ArrayList<>());
            l.add(new ArrayList<>());
            boolean leftTrain = f.getChildren().get(0).isTrainable(), rightTrain = f.getChildren().get(1).isTrainable();
            int leftPos = 0, rightPos = 0;
            for(int i = 0;i < size;i++){
                List<DerivativeDescriber> tmp = calDev(dev.get(i),(L)f.getChildren().get(0).getData().get(leftPos),(R)f.getChildren().get(1).getData().get(rightPos));
                l.get(0).add(tmp.get(0));
                l.get(1).add(tmp.get(1));
                if(!leftTrain)
                    leftPos++;
                if(!rightTrain)
                    rightPos++;
            }
            return l;
        }
        return null;
    }
}
