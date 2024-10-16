package com.wuhao.project.service.chain;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public interface AddFilterateArmory {
     AddFilterate  addNext(AddFilterate addFilterate);

     AddFilterate next();
}
