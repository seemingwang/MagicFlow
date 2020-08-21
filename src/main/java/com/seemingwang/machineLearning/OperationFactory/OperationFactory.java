package com.seemingwang.machineLearning.OperationFactory;

import com.seemingwang.machineLearning.DerivativeDescriber.DerivativeDescriber;
import com.seemingwang.machineLearning.DerivativeDescriber.DoubleTypeDevDescriber;
import com.seemingwang.machineLearning.DerivativeDescriber.MatrixTypeDevDescriber;
import com.seemingwang.machineLearning.FlowNode.*;
import com.seemingwang.machineLearning.FlowNode.FlowNodeOpClass.*;
import com.seemingwang.machineLearning.Matrix.FullMatrix;
import com.seemingwang.machineLearning.Matrix.Matrix;
import com.seemingwang.machineLearning.Matrix.MatrixException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OperationFactory {
    static public SingleTypeOperationFactory<Double> singleTypeScalaFactory = new SingleTypeOperationFactory<>();
    static public SingleTypeOperationFactory<Matrix> singleTypeMatrixFactory = new SingleTypeOperationFactory<>();
    static public FlowNode add(FlowNode a,FlowNode b) throws Exception {
        if((a instanceof  MatrixFlowNode) && (b instanceof ScalaFlowNode)) {
            FlowNode temp = a;
            a = b;
            b = temp;
        }

        if(a instanceof ScalaFlowNode) {
            if(b instanceof MatrixFlowNode){
                if(b.getShape()[0] != 1 || b.getShape()[1] != 1){
                    throw new Exception("in add operation,when added with a scala, a matrix needs to have shape(1,1)");
                }
                ScalaFlowNode c = new ScalaFlowNode();
                c.setChildren(Arrays.asList(a,b));
                c.setOp(new FlowOp<Double>(){

                    @Override
                    public void forward(FlowNode<Double> f) {
                        try {
                            int size = Math.max(f.getChildren().get(0).getData().size(),f.getChildren().get(1).getData().size());
                            List<Double> l = f.getData() == null ? new ArrayList<>():f.getData();
                            if(l.size() == 0){
                                for(int i =0;i < size;i++)
                                    l.add(null);
                            }
                            boolean leftTrain = f.getChildren().get(0).isTrainable(), rightTrain = f.getChildren().get(1).isTrainable();
                            int leftPos = 0, rightPos = 0;
                            for(int i = 0;i < size;i++){
                                l.set(i,(Double)f.getChildren().get(0).getData().get(leftPos) + ((Matrix)f.getChildren().get(1).getData().get(rightPos)).get(0,0));
                                if(!leftTrain)
                                    leftPos++;
                                if(!rightTrain)
                                    rightPos++;
                            }
                            f.setData(l);
                        } catch (MatrixException matrixException) {
                            matrixException.printStackTrace();
                        }
                    }

                    @Override
                    public List<List<DerivativeDescriber>> backward(FlowNode<Double> f, List<Double> dev) {
                        List<List<DerivativeDescriber>> l = new ArrayList<>();
                        l.add(new ArrayList<>());
                        l.add(new ArrayList<>());
                        for(int i = 0;i < dev.size();i++) {
                            FullMatrix f1 = new FullMatrix(1, 1);
                            f1.set(0, 0, dev.get(i));
                            l.get(0).add(new DoubleTypeDevDescriber(dev.get(i)));
                            l.get(1).add(new MatrixTypeDevDescriber(f1));
                        }
                        return l;

                    }
                });
                return c;
            }
            if (!(b instanceof ScalaFlowNode)) {
                throw new Exception("in add operation,the second parameter is expected to be scala");
            }
            ScalaFlowNode c = new ScalaFlowNode();
            singleTypeScalaFactory.func2(a, b, c, FlowOpSingleParamAddForDoubleType.instance);
            return c;
        } else if(a instanceof MatrixFlowNode){


            if (!(b instanceof MatrixFlowNode)) {
                throw new Exception("in add operation,the second parameter is expected to be Matrix");
            }
            MatrixFlowNode c = new FullMatrixFlowNode(a.getShape()[0],a.getShape()[1]);
            singleTypeMatrixFactory.func2(a, b, c, FlowOpSingleParamAddForMatrixType.instance);
            return c;

        }
        return null;
    }

    static public FlowNode Multiply(FlowNode a,FlowNode b) throws Exception {
        if((a instanceof  MatrixFlowNode) && (b instanceof ScalaFlowNode)) {
            FlowNode temp = a;
            a = b;
            b = temp;
        }
        if(a instanceof ScalaFlowNode){
            if(b instanceof MatrixFlowNode){
                if(b.getShape()[0] != 1 || b.getShape()[1] != 1){
                    throw new Exception("in multiply operation,when added with a scala, a matrix needs to have shape(1,1)");
                }
                ScalaFlowNode c = new ScalaFlowNode();
                c.setChildren(Arrays.asList(a,b));
                c.setOp(new FlowOp<Double>(){

                    @Override
                    public void forward(FlowNode<Double> f) {
                        int size = Math.max(f.getChildren().get(0).getData().size(),f.getChildren().get(1).getData().size());
                        try {
                            List<Double> l = f.getData() == null ? new ArrayList<>():f.getData();
                            if(l.size() == 0){
                                for(int i =0;i < size;i++)
                                    l.add(null);
                            }
                            boolean leftTrain = f.getChildren().get(0).isTrainable(), rightTrain = f.getChildren().get(1).isTrainable();
                            int leftPos = 0, rightPos = 0;
                            for(int i = 0;i < size;i++){
                                l.set(i,(Double)f.getChildren().get(0).getData().get(leftPos) * ((Matrix)f.getChildren().get(1).getData().get(rightPos)).get(0,0));
                                if(!leftTrain)
                                    leftPos++;
                                if(!rightTrain)
                                    rightPos++;
                            }
                            f.setData(l);
                        } catch (MatrixException matrixException) {
                            matrixException.printStackTrace();
                        }
                    }

                    @Override
                    public List<List<DerivativeDescriber>> backward(FlowNode<Double> f, List<Double> dev) {
                        try {
                            List<List<DerivativeDescriber>> l = new ArrayList<>();
                            l.add(new ArrayList<>());
                            l.add(new ArrayList<>());
                            boolean leftTrain = f.getChildren().get(0).isTrainable(), rightTrain = f.getChildren().get(1).isTrainable();
                            int leftPos = 0, rightPos = 0;
                            for(int i = 0;i < dev.size();i++) {
                                FullMatrix f1 = new FullMatrix(1, 1);
                                Double num1 = (Double) f.getChildren().get(0).getData().get(leftPos), num2 = ((Matrix) f.getChildren().get(1).getData().get(rightPos)).get(0, 0);
                                f1.set(0, 0, dev.get(i) * num1);
                                l.get(0).add(new DoubleTypeDevDescriber(dev.get(i) * num2));
                                l.get(1).add(new MatrixTypeDevDescriber(f1));
                                //l.add( Arrays.asList(new DoubleTypeDevDescriber(dev.get(i) * num2), new MatrixTypeDevDescriber(f1)));
                                if(!leftTrain)
                                    leftPos++;
                                if(!rightTrain)
                                    rightPos++;
                            }
                            return l;
                        } catch (MatrixException e) {
                            e.printStackTrace();
                            return null;
                        }

                    }
                });
                return c;
            }
            if (!(b instanceof ScalaFlowNode)) {
                throw new Exception("in add operation,the second parameter is expected to be scala");
            }
            ScalaFlowNode c = new ScalaFlowNode();
            singleTypeScalaFactory.func2(a, b, c, FlowOpSingleParamMultiplyForDoubleType.instance);
            return c;
        }
        if((a instanceof MatrixFlowNode) && (b instanceof MatrixFlowNode)){
            FullMatrixFlowNode f = new FullMatrixFlowNode(a.getShape()[0],b.getShape()[1]);
            singleTypeMatrixFactory.func2(a,b,f, FlowOpSingleParamMultiplyForMatrixType.instance);
            return f;
        }
        return null;
    }

    static public FlowNode subtract(FlowNode a,FlowNode b) throws Exception {

        if(a instanceof ScalaFlowNode) {
            if(b instanceof MatrixFlowNode){
                if(b.getShape()[0] != 1 || b.getShape()[1] != 1){
                    throw new Exception("in subtract operation,when working with a scala, a matrix needs to have shape(1,1)");
                }
                ScalaFlowNode c = new ScalaFlowNode();
                c.setChildren(Arrays.asList(a,b));
                c.setOp(new FlowOp<Double>(){

                    @Override
                    public void forward(FlowNode<Double> f) {
                        int size = Math.max(f.getChildren().get(0).getData().size(),f.getChildren().get(1).getData().size());
                        try {
                            List<Double> l = f.getData() == null ? new ArrayList<>():f.getData();
                            if(l.size() == 0){
                                for(int i =0;i < size;i++)
                                    l.add(null);
                            }
                            boolean leftTrain = f.getChildren().get(0).isTrainable(), rightTrain = f.getChildren().get(1).isTrainable();
                            int leftPos = 0, rightPos = 0;
                            for(int i = 0;i < size;i++){
                                l.set(i,(Double)f.getChildren().get(0).getData().get(leftPos) - ((Matrix)f.getChildren().get(1).getData().get(rightPos)).get(0,0));
                                if(!leftTrain)
                                    leftPos++;
                                if(!rightTrain)
                                    rightPos++;
                            }
                            f.setData(l);
                        } catch (MatrixException matrixException) {
                            matrixException.printStackTrace();
                        }
                    }

                    @Override
                    public List<List<DerivativeDescriber>> backward(FlowNode<Double> f, List<Double> dev) {
                        List<List<DerivativeDescriber>> l = new ArrayList<>();
                        l.add(new ArrayList<>());
                        l.add(new ArrayList<>());
                        for(int i = 0;i < dev.size();i++) {
                            FullMatrix f1 = new FullMatrix(1, 1);
                            f1.set(0, 0, -dev.get(i));
                            l.get(0).add(new DoubleTypeDevDescriber(dev.get(i)));
                            l.get(1).add(new MatrixTypeDevDescriber(f1));
                        }
                        return l;

                    }
                });
                return c;
            }
            if (!(b instanceof ScalaFlowNode)) {
                throw new Exception("in subtract operation,the second parameter is expected to be scala");
            }
            ScalaFlowNode c = new ScalaFlowNode();
            singleTypeScalaFactory.func2(a, b, c, FlowOpSingleParamSubtractForDoubleType.instance);
            return c;
        } else if(a instanceof MatrixFlowNode){
            if(b instanceof ScalaFlowNode){
                if(a.getShape()[0] != 1 || a.getShape()[1] != 1){
                    throw new Exception("in subtract operation,when working with a scala,a matrix needs to have shape(1,1)");
                }
                ScalaFlowNode c = new ScalaFlowNode();
                c.setChildren(Arrays.asList(a,b));
                c.setOp(new FlowOp<Double>(){

                    @Override
                    public void forward(FlowNode<Double> f) {
                        int size = Math.max(f.getChildren().get(0).getData().size(),f.getChildren().get(1).getData().size());
                        try {
                            List<Double> l = f.getData() == null ? new ArrayList<>():f.getData();
                            if(l.size() == 0){
                                for(int i =0;i < size;i++)
                                    l.add(null);
                            }
                            boolean leftTrain = f.getChildren().get(0).isTrainable(), rightTrain = f.getChildren().get(1).isTrainable();
                            int leftPos = 0, rightPos = 0;
                            for(int i = 0;i < size;i++){
                                l.set(i,((Matrix)f.getChildren().get(0).getData().get(leftPos)).get(0,0) -(Double)f.getChildren().get(1).getData().get(rightPos));
                                if(!leftTrain)
                                    leftPos++;
                                if(!rightTrain)
                                    rightPos++;
                            }
                            f.setData(l);
                        } catch (MatrixException matrixException) {
                            matrixException.printStackTrace();
                        }
                    }

                    @Override
                    public List<List<DerivativeDescriber>> backward(FlowNode<Double> f, List<Double> dev) {
                        List<List<DerivativeDescriber>> l = new ArrayList<>();
                        l.add(new ArrayList<>());
                        l.add(new ArrayList<>());
                        for(int i = 0;i < dev.size();i++) {
                            FullMatrix f1 = new FullMatrix(1, 1);
                            f1.set(0, 0, dev.get(i));
                            l.get(0).add(new MatrixTypeDevDescriber(f1));
                            l.get(1).add(new DoubleTypeDevDescriber(-dev.get(i)));
                        }
                        return l;

                    }
                });
                return c;
            }

            if (!(b instanceof MatrixFlowNode)) {
                throw new Exception("in add operation,the second parameter is expected to be Matrix");
            }
            MatrixFlowNode c = new FullMatrixFlowNode(a.getShape()[0],a.getShape()[1]);
            singleTypeMatrixFactory.func2(a, b, c, FlowOpSingleParamAddForMatrixType.instance);
            return c;

        }
        return null;
    }

    static public FlowNode sigmoid(FlowNode a){
        if(a instanceof ScalaFlowNode){
            ScalaFlowNode d = new ScalaFlowNode();
            singleTypeScalaFactory.func1(a,d, FlowOpSingleParamSigmoidForDoubleType.instance);
            return d;
        } else if (a instanceof MatrixFlowNode){
           FullMatrixFlowNode d = new FullMatrixFlowNode(a.getShape()[0],a.getShape()[1]);
           singleTypeMatrixFactory.func1(a,d,FlowOpSingleParamSigmoidForMatrixType.instance);
           return d;
        }
        return null;
    }

    static public FlowNode pow(FlowNode a, double s){
        if(a instanceof ScalaFlowNode){
            ScalaFlowNode d = new ScalaFlowNode();
            singleTypeScalaFactory.func1(a,d, new FlowOpSingleParamPowForDoubleType(s));
            return d;
        }
        return null;
    }

    static public FlowNode averageSum(FlowNode a){
        if(a instanceof ScalaFlowNode){
            ScalaFlowNode res = new ScalaFlowNode();
            res.setChildren(Arrays.asList(a));
            res.setOp(FlowOpAverageSum.instance);
            return res;
        }
        return null;
    }
}
