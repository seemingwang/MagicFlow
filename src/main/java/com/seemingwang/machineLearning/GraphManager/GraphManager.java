package com.seemingwang.machineLearning.GraphManager;

import com.seemingwang.machineLearning.DataInitializer.DataInitializer;
import com.seemingwang.machineLearning.FlowNode.FlowNode;
import com.seemingwang.machineLearning.FlowNode.ScalaFlowNode;
import com.seemingwang.machineLearning.Matrix.FullMatrix;
import com.seemingwang.machineLearning.Optimizer.Optimizer;
import com.seemingwang.machineLearning.Sequential.Sequential;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphManager {

    static public FullMatrix changeToMatrix(List<Double> t){
        int size = t.size();
        FullMatrix f = new FullMatrix(1,size);
        for(int i = 0;i < size;i++)
            f.set(0,i,t.get(i));
        return f;
    }
    static public List<FullMatrix> changeToMatrixList(List<List<Double>> l){
        List<FullMatrix> res = new ArrayList<>();
        for(List<Double> c:l){
            res.add(changeToMatrix(c));
        }
        return res;
    }
    public List<FlowNode> exeSeq;

    public int getBatchSize() {
        return batchSize;
    }

    public GraphManager setBatchSize(int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public int batchSize;

    public Optimizer getOptimizer() {
        return optimizer;
    }


    public ScalaFlowNode getOptimizeNode() {
        return optimizeNode;
    }

    public void setOptimizeNode(ScalaFlowNode optimizeNode) {
        this.optimizeNode = optimizeNode;
    }

    public ScalaFlowNode optimizeNode;

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
    public void feed(Map<FlowNode,List> m) throws Exception {
        batchSize = 0;

        for(FlowNode c:m.keySet()){
            List l = m.get(c);
            c.setData(l);
            if(batchSize == 0)
                batchSize = c.getData().size();
            else if(batchSize != c.getData().size()){
                throw new Exception("data size varies");
            }
        }
    }
    public void initData() throws Exception {
        exeSeq = Sequential.arrange(true, optimizeNode);
        for(FlowNode c:exeSeq){
            if(c.isTrainable()){
                initializer.initData(c);
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
        return optimizeNode.getData().get(0);
    }

}
