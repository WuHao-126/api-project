package com.wuhao.project.support.Id.algorithm;

import com.wuhao.project.support.Id.IdWorker;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: 短码生成策略 利用日期时间来构建
 */
public class ShortCodeAgorithm implements IdWorker {
    @Override
    public Long getId() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int second = calendar.get(Calendar.SECOND);

        // 打乱排序：2020年为准 + 小时 + 周期 + 日 + 三位随机数
        StringBuilder idStr = new StringBuilder();
        idStr.append(year - 2020);
        idStr.append(hour);
        idStr.append(String.format("%02d", week));
        idStr.append(day);
        idStr.append(second);
        idStr.append(String.format("%08d", new Random().nextInt(100000000)));

        return Long.parseLong(idStr.toString());
    }
}
