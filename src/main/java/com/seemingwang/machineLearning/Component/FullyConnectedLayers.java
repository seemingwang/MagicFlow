package com.seemingwang.machineLearning.Component;

import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.Utils.Structure.Activator;
import com.seemingwang.machineLearning.Utils.Tripple;

public class FullyConnectedLayers {
    FullyConnectedLayerWithActivator f;

    public FullyConnectedLayers() {
        f = new FullyConnectedLayerWithActivator();
    }

    public FlowNode makeFullyConnectedLayers(FlowNode input, Tripple<Integer,Integer,Activator> ... T){
        for(Tripple<Integer,Integer,Activator> t: T){
            input = f.makeFullyConnectedLayerWithActivator(input,t.first,t.second,t.third);
        }
        return input;
    }
}
