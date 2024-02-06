package com.wuhao.project.controller;

import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import com.wuhao.project.common.Result;
import com.wuhao.project.model.entity.SystemInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@RestController
@RequestMapping("/sys")
public class SystemInfoController {
    @PostMapping("/cpu")
    public Result getCpuUsed(){
        long l = System.currentTimeMillis();
        SystemInfo systemInfo=new SystemInfo();
        CpuInfo cpuInfo = OshiUtil.getCpuInfo();
        //cpu的使用率
        systemInfo.setCpuUsed((int)cpuInfo.getUsed());
        //内存使用率
        double total = (double)OshiUtil.getMemory().getTotal();
        double available =(double)OshiUtil.getMemory().getAvailable();
        systemInfo.setMemoryUsed((int)((available/total)*100));
        File win = new File("C:\\");
        if (win.exists()) {
            long disktotal = win.getTotalSpace();
            long freeSpace = win.getFreeSpace();
            systemInfo.setDiskTotal(disktotal/1024/1024/1024);
            systemInfo.setDiskResidue(freeSpace/1024/1024/1024);
            systemInfo.setUsedDiaskTotal((disktotal - freeSpace));
        }
        long l1 = System.currentTimeMillis();
        System.out.println(l1-l);
        return Result.success(systemInfo);
    }
    @PostMapping("/disk")
    public Result getDiskInfo(){
        SystemInfo systemInfo=new SystemInfo();
        File win = new File("C:\\");
        if (win.exists()) {
            long disktotal = win.getTotalSpace();
            long freeSpace = win.getFreeSpace();
            systemInfo.setDiskTotal(disktotal/1024/1024/1024);
            systemInfo.setDiskResidue(freeSpace/1024/1024/1024);
            systemInfo.setUsedDiaskTotal((disktotal - freeSpace));
        }
        return Result.success(systemInfo);
    }
}
