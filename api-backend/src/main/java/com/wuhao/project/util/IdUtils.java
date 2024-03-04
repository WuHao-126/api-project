package com.wuhao.project.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.wuhao.project.constant.CommonConstant;
import com.wuhao.project.support.Id.IdWorker;
import com.wuhao.project.support.Id.algorithm.RandomNumeric;
import com.wuhao.project.support.Id.algorithm.ShortCodeAgorithm;
import com.wuhao.project.support.Id.algorithm.SnowFlakeAgorithm;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public class IdUtils {
    public static Long getId(){
        ShortCodeAgorithm randomNumeric=new ShortCodeAgorithm();
        return randomNumeric.getId();
    }
    public static Long getId(CommonConstant constant){
        SnowFlakeAgorithm snowFlakeAgorithm=new SnowFlakeAgorithm(1);
        ShortCodeAgorithm shortCodeAgorithm=new ShortCodeAgorithm();
        RandomNumeric randomNumeric=new RandomNumeric();
        Map<CommonConstant.Ids,IdWorker> map=new HashMap<>();
        map.put(CommonConstant.Ids.SnowFlake,snowFlakeAgorithm);
        map.put(CommonConstant.Ids.RandomNumeric,randomNumeric);
        map.put(CommonConstant.Ids.ShortCode,shortCodeAgorithm);
        return map.get(CommonConstant.Ids.RandomNumeric).getId();
    }

    public static void main(String[] args) {
        Long id = IdUtils.getId();
        System.out.println(id);
    }
}
