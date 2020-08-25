package com.seemingwang.machineLearning.FlowNode;

import com.seemingwang.machineLearning.DerivativeDescriber.DerivativeDescriber;

import java.util.ArrayList;
import java.util.List;

public abstract class FlowOpSingle<T,L> implements FlowOp<T> {
    public abstract T cal(L input);
    public abstract List<DerivativeDescriber> calDev(T dev, L input);
    @Override
    public void forward(FlowNode<T> f){
        if(!f.getChildren().isEmpty()) {
            int size = f.getChildren().get(0).getData().size();
            List<T> l = f.getData() == null ? new ArrayList<>():f.getData();
            if(l.size() != size){
                l.clear();
                for(int i =0;i < size;i++)
                    l.add(null);
            }
            for(int i = 0;i < size;i++)
                l.set(i,cal((L) f.getChildren().get(0).getData().get(i)));
            f.setData(l);
        }
    }

    @Override
    public List<List<DerivativeDescriber>> backward(FlowNode<T> f, List<T> dev){
        if(!f.getChildren().isEmpty()){
            List<List<DerivativeDescriber>> l = new ArrayList<>();
            l.add(new ArrayList<>());
            for(int i = 0;i < dev.size();i++){
                l.get(0).add(calDev(dev.get(i),(L)f.getChildren().get(0).getData().get(i)).get(0));
            }
            return l;
        }
        return null;
    }
}
