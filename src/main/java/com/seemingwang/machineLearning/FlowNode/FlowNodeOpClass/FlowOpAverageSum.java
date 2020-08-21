package com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass;

import com.seemingwang.machineLearning.DerivativeDescriber.DerivativeDescriber;
import com.seemingwang.machineLearning.DerivativeDescriber.DoubleTypeDevDescriber;
import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.FlowOp;
import com.seemingwang.machineLearning.FlowNode.ScalaFlowNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlowOpAverageSum implements FlowOp<Double> {
    public static FlowOpAverageSum instance = new FlowOpAverageSum();
    @Override
    public void forward(FlowNode<Double> f, int size) {
        if(f.getData().size() == 0){
            f.getData().add(0.0);
        }
        double x = 0;
        ScalaFlowNode c = (ScalaFlowNode)f.getChildren().get(0);
        for(Double num:c.getData())
            x += num;
        x /= c.getData().size();
        f.setData(Arrays.asList(x));

    }

    @Override
    public List<List<DerivativeDescriber>> backward(FlowNode<Double> f, List<Double> dev) {
        List<List<DerivativeDescriber>> res = new ArrayList<>();
        List<DerivativeDescriber> l = new ArrayList<>();
        int size = f.getChildren().get(0).getData().size();
        double ave = 1.0 / size * dev.get(0);
        for(int i = 0;i < size;i++){
            l.add(new DoubleTypeDevDescriber(ave));
        }
        res.add(l);
        return res;
    }
}
