package com.seemingwang.machineLearning.models;

abstract class NetworkUnit {
    int shape[];
    public static void makePair(NetworkUnit a,NetworkUnit b){
        a.next = b;
        b.previous = a;
    }
    double [][] output,input,weight,wdev;;
    NetworkUnit previous, next;
    public static void reset(double [][] mat){
        for(int i = 0;i < mat.length;i++){
            for(int j = 0;j < mat[0].length;j++){
                mat[i][j] = 0;
            }
        }
    }
    abstract void cal();
    abstract void exportWeight();
    abstract double[][] calDev(double [][]dev,double learningRate);
    public void forward(){
        cal();
        if(next != null){
            next.input = output;
            next.forward();
        }
    }

    public void backward(double [][]dev,double learningRate) {
        double[][] dev2 = calDev(dev,learningRate);
        if (previous != null) {
            previous.backward(dev2,learningRate);
        }
    }

}
class SquareError extends NetworkUnit{

    public SquareError(double[] label) {
        this.label = label;
    }

    public double label[];

    @Override
    void cal() {
        output = new double [input.length][1];
        for(int i = 0; i < input.length;i++){
            output[i][0] = (input[i][0] - label[i]) * (input[i][0] - label[i]);
        }
    }

    @Override
    void exportWeight() {

    }

    @Override
    double[][] calDev(double[][] dev, double learningRate) {
        double [][] w = new double[dev.length][1];
        for(int i = 0;i < dev.length;i++){
            w[i][0] = 2.0 * (input[i][0] - label[i]) * dev[i][0];
        }
        return w;
    }
}

class FullyConnectedTestUnit extends NetworkUnit{

    public void setWeight(double[][] weight) {
        this.weight = weight;
    }

    public void setBias(double []bias){
        this.bias = bias;
    }
    FullyConnectedTestUnit(int r,int c){
        shape = new int[]{r,c};
        weight = new double [r][c];
    }

    public double bias[],wbias[];

    @Override
    void cal() {
        int r1 = input.length,c = shape[1],r = shape[0];
        output = new double [r1][c];
        reset(output);
        for(int i = 0;i < r1;i++){
            for(int j = 0;j < c;j++){
                for(int k = 0;k < r;k++){
                    output[i][j] += input[i][k] * weight[k][j];
                }
                output[i][j] += bias[j];
            }
        }
    }

    @Override
    void exportWeight() {
        System.out.println("weight matrix");
        for(int i = 0;i < weight.length;i++){
            for(int j = 0;j < weight[0].length;j++){
                System.out.print(weight[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("wbias");
        for(int i = 0;i < bias.length;i++){
            System.out.print(bias[i] + " ");
        }
        System.out.println();
    }

    @Override
    double[][] calDev(double[][] dev,double learningRate) {
        int r1 = input.length,c = shape[1],r = shape[0];
        double dev2[][] = new double[r1][r];
        wdev= new double[r][c];
        wbias = new double [c];
        reset(dev2);
        reset(wdev);
        for(int i = 0;i < r1;i++){
            for(int j = 0;j < c;j++){
                for(int k = 0;k < r;k++){
                    //output[i][j] += input[i][k] * weight[k][j];
                    dev2[i][k] += dev[i][j] * weight[k][j];
                    wdev[k][j] += dev[i][j] * input[i][k];

                }
                wbias[j] += dev[i][j];
            }
        }
        for(int i = 0;i < r;i++){
            for(int j = 0;j < c;j++){
                weight[i][j] -= wdev[i][j] * learningRate;
            }
        }
        for(int i = 0;i < c;i++)
            bias[i] -= wbias[i] * learningRate;
        return dev2;
    }
}

class SigmoidTestUnit extends NetworkUnit{

    double sigmoid(double x){
        return Math.exp(x)/(1.0 + Math.exp(x));
    }
    @Override
    void cal() {
        int r = input.length, c = input[0].length;
        output = new double[r][c];
        for(int i = 0;i < r;i++){
            for(int j = 0;j < c;j++){
                output[i][j] = sigmoid(input[i][j]);
            }
        }
    }

    @Override
    void exportWeight() {

    }

    @Override
    double[][] calDev(double[][] dev,double l) {
        int r = dev.length, c = dev[0].length;
        double [][] dev2 = new double[r][c];
        for(int i = 0;i < r;i++){
            for(int j =0;j < c;j++){
                dev2[i][j] = sigmoid(input[i][j]) * (1.0 - sigmoid(input[i][j])) * dev[i][j];
            }
        }
        return dev2;
    }
}
