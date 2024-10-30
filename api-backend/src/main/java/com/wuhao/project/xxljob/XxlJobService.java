package com.wuhao.project.xxljob;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

@Component
public class XxlJobService {

    @XxlJob("test")
    public void xxljob() {
        System.out.println("我是xxl-job我正在执行");
    }
}
