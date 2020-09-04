package com.seemingwang.machineLearning.DataProvider;

import com.seemingwang.machineLearning.Utils.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RandomShuffleBatchTest {
    @Test
    public void TestShuffleSequence(){
        double data[][] = new double[12][3];
        double label[][] = new double[12][1];
        int count[] = new int[36];
        int ind = 0;
        for(int i = 0;i < 12;i++){
            for(int j = 0;j < 3;j++){
                data[i][j] = ind++;
            }
            label[i][0] = ind - 1;
        }
        RandomShuffleBatch ran = new RandomShuffleBatch(new TwoDArrayDataProvider(data),new TwoDArrayDataProvider(label),3);
        Map<Integer,DataProvider> m = new HashMap<>();
        for(int i = 0;i < 8;i++){
            Pair<DataProvider,DataProvider> p = ran.nextBatch();
            Assert.assertEquals(p.first.getShape()[0],new Integer(3));
            Assert.assertEquals(p.second.getShape()[0],new Integer(3));
            for(int j = 0;j < 3;j++){
                for(int k = 0;k < 3;k++){
                    int t = new Double(p.first.getData(j,k) ).intValue();
                    System.out.println(t);
                    count[t]++;
                }
                Assert.assertEquals(p.first.getData(j,2),p.second.getData(j,0),1e-8);
            }
            if(i < 4){
                m.put(new Integer(i),p.first);
            } else {
                DataProvider dp = m.get(i - 4);
                for(int j = 0;j < 3;j++){
                    for(int k = 0;k < 3;k++){
                        Assert.assertEquals(dp.getData(j,k),p.first.getData(j,k),1e-8);
                    }
                }
            }
        }
        for(int i = 0;i < 36;i++)
            Assert.assertEquals(count[i],2);
    }

}