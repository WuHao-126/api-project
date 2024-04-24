package com.wuhao.project.support.sensitive.algorithm;

import com.wuhao.project.support.sensitive.TrafficLimiter;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public class CounterLimiter implements TrafficLimiter {
    private long timeStamp = System.currentTimeMillis();
    private int reqCount;
    private int limitNum = 1;
    private long interval = 1000l;
    @Override
    public synchronized Boolean limit() {
        long now = System.currentTimeMillis();
        if(now < timeStamp + interval){
            if(reqCount + 1 > limitNum){
                return true;
            }
            reqCount++;
            return false;
        }else{
            timeStamp = now;
            reqCount = 1;
            return false;
        }
    }
}
