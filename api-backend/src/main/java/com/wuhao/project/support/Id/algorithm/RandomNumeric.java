package com.wuhao.project.support.Id.algorithm;

import com.wuhao.project.support.Id.IdWorker;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: 短码生成策略 利用日期时间来构建
 */
public class RandomNumeric implements IdWorker {

    @Override
    public Long getId() {
        return Long.parseLong(RandomStringUtils.randomNumeric(16));
    }

}
