package com.seemingwang.machineLearning.DataProvider;

import com.seemingwang.machineLearning.Utils.Pair;

import java.util.Random;

public class RandomShuffleBatch extends MiniBatch {
    int seq[];
    int length,batchSize,pos,epoch,epochCount;
    Random r;
    public RandomShuffleBatch(DataProvider data, DataProvider label,int batchSize) {
        this.data = data;
        this.label = label;
        length = data.getShape()[0];
        seq = new int[length];
        this.batchSize = batchSize > length ? length:batchSize;
        epoch = length/batchSize;
        r = new Random();
        shuffle();
        pos = 0;
    }

    void shuffle(){
        for(int i = 0;i < seq.length;i++)
            seq[i] = i;
        for(int i = 0;i < seq.length - 1;i++){
            int ind = r.nextInt(seq.length - i) + i;
            if(ind != i){
                int save = seq[ind];
                seq[ind] = seq[i];
                seq[i] = save;
            }
        }

    }
    DataProvider data,label;
    @Override
    public Pair<DataProvider, DataProvider> nextBatch() {
        Pair<DataProvider,DataProvider> res =  new Pair<>(new DataProvider() {
            final int position = pos;
            @Override
            public Integer[] getShape() {
                Integer[] shape = data.getShape().clone();
                shape[0] = batchSize;
                return shape;
            }

            @Override
            public double getData(Integer... Pos) {
                Integer []P = Pos.clone();
                P[0] = seq[(position + Pos[0]) % length];
                return data.getData(P);
            }
        }, new DataProvider() {
            final int position = pos;
            @Override
            public Integer[] getShape() {
                Integer[] shape = label.getShape().clone();
                shape[0] = batchSize;
                return shape;
            }

            @Override
            public double getData(Integer... Pos) {
                Integer []P = Pos.clone();
                P[0] = seq[(position + Pos[0]) % length];
                return label.getData(P);
            }
        });
        pos = (pos + batchSize) % length;
        epochCount++;
        if(epochCount/epoch >= 5){
            shuffle();
            epochCount = 0;
            pos = 0;
        }
        return res;
    }
}
