package com.wuhao.project.support.sensitive.algorithm;

import com.wuhao.project.support.sensitive.TrafficLimiter;

import java.util.LinkedList;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public class SlidingTimeWindowLimiter implements TrafficLimiter {
    private int reqCount;
    private LinkedList<Integer> slots = new LinkedList<>();
    private int limitNum = 100;
    private long windowLength = 100l;
    private int windowNum = 100;
    public SlidingTimeWindowLimiter(){
        slots.addLast(0);
        new Thread(()->{
            while (true){
                try {
                    Thread.sleep(windowLength);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                slots.addLast(0);
                if(slots.size() > windowNum){
                    reqCount = reqCount-slots.peekFirst();
                    slots.removeFirst();
                }
            }
        }).start();
    }
    @Override
    public Boolean limit() {
        if((reqCount+1) > limitNum){
            return true;
        }
        slots.set(slots.size() - 1,slots.peekLast() + 1);
        reqCount++;
        return false;
    }
}
