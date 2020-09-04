package com.seemingwang.machineLearning.GraphManager;

import com.seemingwang.machineLearning.DataInitializer.DataInitializer;
import com.seemingwang.machineLearning.DataProvider.DataProvider;
import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.Optimizer.Optimizer;
import com.seemingwang.machineLearning.Sequential.Sequential;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class GraphManager {

    void feedFlowNodeData(FlowNode f, DataProvider dp) throws Exception {
        if(!initDone){
            initData();
            initDone = true;
        }
        Integer[] shape = dp.getShape();
        Integer[] nodeShape = f.getShape();
        if(nodeShape.length == 0){
            f.resetDevSize(1);
            f.data[0] = dp.getData(0);
            return;
        }
        if(nodeShape.length != shape.length){
            throw new Exception("flowNode f's fed data doesn't have the right shape, expected shape is " + f.getShapeDesc());
        }
        int size = 1;
        Integer []pos = new Integer[shape.length];
        for(int i = 0;i < shape.length;i++){
            if(f.getShape()[i] == 0){
                setBatchSize(shape[i]);
            } else if(!shape[i].equals(f.getShape()[i])){
                throw new Exception("flowNode f's expected shape is " + f.getShapeDesc() + " which the input's shape doesn't match");
            }
            size *= shape[i];
            pos[i] = 0;
        }
        pos[pos.length - 1] = -1;
        f.resetDataSize(size);
        for(int i = 0;i < size;i++){
            pos[shape.length - 1]++;
            int k = shape.length - 1;
            while(k > 0 && pos[k] == shape[k]){
                pos[k] = 0;
                pos[k-1]++;
                k--;
            }
            f.data[i] = dp.getData(pos);
        }

    }
    public List<FlowNode> exeSeq;

    public int getBatchSize() {
        return batchSize;
    }

    private Integer batchSize;

    private void setBatchSize(int b){
        try {
            Field f  = Integer.class.getDeclaredField("value");
            f.setAccessible(true);
            f.set(batchSize,b);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public Optimizer getOptimizer() {
        return optimizer;
    }


    public FlowNode getOptimizeNode() {
        return optimizeNode;
    }

    public void setOptimizeNode(FlowNode optimizeNode) {
        this.optimizeNode = optimizeNode;
    }

    public FlowNode optimizeNode;

    public GraphManager setOptimizer(Optimizer optimizer) {
        this.optimizer = optimizer;
        return this;
    }

    public Optimizer optimizer;

    public DataInitializer getInitializer() {
        return initializer;
    }

    public GraphManager setInitializer(DataInitializer initializer) {
        this.initializer = initializer;
        return this;
    }

    public DataInitializer initializer;
    public boolean initDone;
    public void feed(Map<FlowNode,DataProvider> m) throws Exception {
        for(FlowNode c:m.keySet()){
            DataProvider dp = m.get(c);
            feedFlowNodeData(c,dp);
        }
    }
    public void initData() throws Exception {
        batchSize = new Integer(0);
        exeSeq = Sequential.arrange(true, optimizeNode);
        for(FlowNode c:exeSeq){
            if(c.isTrainable()){
                initializer.initData(c);
            }
            for(int i = 0;i < c.shape.length;i++){
                if(c.shape[i] == null)
                    c.shape[i] = batchSize;
            }
        }
        initDone = true;
    }

    public void run(){
        if(!initDone){
            try {
                initData();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        for(FlowNode c:exeSeq){
            if(c.getOp() != null)
                c.getOp().forward(c);
        }
        optimizer.run(optimizeNode);
    }

    public void run(FlowNode x){
        for(FlowNode c:exeSeq){
            if(c.getOp() != null)
                c.getOp().forward(c);
            if(c == x)
                return;
        }
    }

    public double getCost(){
        return optimizeNode.data[0];
    }

}
