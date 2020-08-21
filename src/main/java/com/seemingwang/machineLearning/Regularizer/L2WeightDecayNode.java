package com.seemingwang.machineLearning.Regularizer;

import com.seemingwang.machineLearning.DerivativeDescriber.DerivativeDescriber;
import com.seemingwang.machineLearning.DerivativeDescriber.DoubleTypeDevDescriber;
import com.seemingwang.machineLearning.DerivativeDescriber.MatrixTypeDevDescriber;
import com.seemingwang.machineLearning.FlowNode.*;
import com.seemingwang.machineLearning.Matrix.FullMatrix;
import com.seemingwang.machineLearning.Matrix.MatrixException;

import java.util.*;

public class L2WeightDecayNode extends ScalaFlowNode{

    public int count;
    public double lamda;
    L2WeightDecayNode(double lamda){
        this.lamda = lamda;
    }
    public static L2WeightDecayNode makeRegularizationNode(ScalaFlowNode a,double lamda) {
        Queue<FlowNode> q = new LinkedList<>();
        L2WeightDecayNode optimize = new L2WeightDecayNode(lamda);
        optimize.setChildren(new ArrayList<>());
        q.add(a);
        HashSet<FlowNode> s = new HashSet<>();
        s.add(a);
        while(q.size() > 0){
            FlowNode c = q.poll();
            if(c.isTrainable() && ((c instanceof ScalaFlowNode) || (c instanceof FullMatrixFlowNode))){
                optimize.getChildren().add(c);
            }
            if(c.getChildren() == null)
                continue;
            for(FlowNode x: (List<FlowNode>)c.getChildren()){
                if(!s.contains(x)){
                    s.add(x);
                    q.add(x);
                }
            }
        }
        optimize.setOp(new FlowOp() {
            @Override
            public void forward(FlowNode f) {
                double res = 0,t;
                for(FlowNode c: (List<FlowNode>) f.getChildren()){
                    if(c instanceof ScalaFlowNode){
                        t =  ((Double)c.getData().get(0));
                        res += t * t;
                    } else if(c instanceof FullMatrixFlowNode){
                        FullMatrix matrix = (FullMatrix) c.getData().get(0);
                        for(int i = 0;i < c.getShape()[0];i++){
                            for(int j = 0;j < c.getShape()[1];j++){
                                t = matrix.get(i,j);
                                res += t * t;
                            }
                        }
                    }
                }
                f.setData(Arrays.asList(res * ((L2WeightDecayNode)f).lamda/2));
            }

            @Override
            public List<List<DerivativeDescriber>> backward(FlowNode f, List dev) {
                List<List<DerivativeDescriber>> res = new ArrayList<>();
                double derivative = (double) dev.get(0) * ((L2WeightDecayNode)f).lamda;
                double t;
                for(FlowNode c: (List<FlowNode>) f.getChildren()){
                    if(c instanceof ScalaFlowNode){
                        t =  ((Double)c.getData().get(0));
                        res.add(Arrays.asList(new DoubleTypeDevDescriber( t * derivative)));
                    } else if(c instanceof FullMatrixFlowNode){
                        FullMatrix matrix = (FullMatrix) c.getData().get(0);
                        try {
                            res.add(Arrays.asList(new MatrixTypeDevDescriber(matrix.multiply(derivative))));
                        } catch (MatrixException e) {
                            e.printStackTrace();
                        }

                    } else {
                        res.add(null);
                    }
                }
                return res;
            }
        });
        return optimize;


    }
}
