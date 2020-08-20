package com.seemingwang.machineLearning.Sequential;

import com.seemingwang.machineLearning.FlowNode.FlowNode;

import java.util.*;

public class Sequential {
    public static List<FlowNode> arrange(boolean forward, FlowNode ... T) throws Exception {
        HashMap<FlowNode,Integer> inDegree = new HashMap<>();
        Queue<FlowNode> q = new LinkedList<>();
        ArrayList<FlowNode> q1 = new ArrayList<>();
        for(FlowNode f: T) {
            if (f == null || inDegree.containsKey(f))
                continue;
            q.add(f);
            inDegree.put(f, 0);
        }
        while(q.size() > 0){
            FlowNode node = q.poll();
            List<FlowNode> l = node.getChildren();
            if(l == null)
                continue;
            for(FlowNode c: l){
                if(inDegree.containsKey(c)){
                    if(inDegree.get(c) == 0)
                        throw new Exception("in sequential run: the parameters shouldn't have incoming edges");
                    else
                        inDegree.put(c,inDegree.get(c) + 1);
                } else {
                    inDegree.put(c, 1);
                    q.add(c);
                }
            }
        }
        for(FlowNode f: T){
            if(f == null)
                continue;
            q1.add(f);
        }
        int numberOfNode = inDegree.size();
        int ind = 0;
        while(ind < q1.size()){
            FlowNode c = q1.get(ind++);
            List<FlowNode> l = c.getChildren();
            if(l == null)
                continue;
            for(FlowNode d: l){
                inDegree.put(d,inDegree.get(d) - 1);
                if(inDegree.get(d) == 0){
                    q1.add(d);
                }
            }
        }
        if(q1.size() != numberOfNode){
            throw new Exception("in sequential run: cycles are detected");
        }
        if(forward){
            Collections.reverse(q1);
        }
        return q1;
    }
}
