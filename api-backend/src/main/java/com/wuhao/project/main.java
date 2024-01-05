package com.wuhao.project;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public class main {
    public static void main(String[] args) {
        SystemInfo systemInfo=new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();

        CentralProcessor centralProcessor = hardware.getProcessor();
        //获取CPU名称
        String name = centralProcessor.getProcessorIdentifier().getName();
        //获取CPU占用率
        long[] currentFreq = centralProcessor.getCurrentFreq();
        int logicalProcessorCount = centralProcessor.getLogicalProcessorCount();

        System.out.println("CPU名称: " + name);
        System.out.println("CPU核心数: " + logicalProcessorCount);

        GlobalMemory memory = hardware.getMemory();
        long totla=memory.getTotal()/1024/1024/1024;

    }
}
