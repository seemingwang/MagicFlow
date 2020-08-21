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

    FullMatrix changeToMatrix(List<Double> t){
        int size = t.size();
        FullMatrix f = new FullMatrix(1,size);
        for(int i = 0;i < size;i++)
            f.set(0,i,t.get(i));
        return f;
    }
    List<FullMatrix> changeToMatrixList(List<List<Double>> l){
        List<FullMatrix> res = new ArrayList<>();
        for(List<Double> c:l){
            res.add(changeToMatrix(c));
        }
        return res;
    }
    public List<FlowNode> exeSeq;
    public int size;

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
    public void feed(Map<String,Map<FlowNode,List>> m) throws Exception {
        size = 0;
        Map<FlowNode,List> placeHolder = m.get("placeholder"), label = m.get("label");

        for(FlowNode c:placeHolder.keySet()){
            c.setData(changeToMatrixList(placeHolder.get(c)));
            if(size == 0)
                size = c.getData().size();
            else if(size != c.getData().size()){
                throw new Exception("data size varies");
            }
        }
        for(FlowNode c:label.keySet()){
            c.setData(label.get(c));
            if(size == 0)
                size = c.getData().size();
            else if(size != c.getData().size()){
                throw new Exception("data size varies");
            }
        }
    }
    void initData() throws Exception {
        exeSeq = Sequential.arrange(true, optimizeNode);
        for(FlowNode c:exeSeq){
            if(c.isTrainable()){
                initializer.initData(c);
            }
            if((c.getChildren() == null || c.getChildren().size() == 0) && (c.getData()== null || c.getData().size() == 0)){
                    throw new Exception("node " + c + "'s data is not initialized");
            }
        }
    }

    public void run(){
        if(!initDone){
            try {
                initData();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            initDone = true;
        }
        for(FlowNode c:exeSeq){
            if(c.getOp() != null)
                c.getOp().forward(c,size);
        }
        optimizer.run(optimizeNode,size);
    }

    public double getCost(){
        return optimizeNode.getData().get(0);
    }

}
