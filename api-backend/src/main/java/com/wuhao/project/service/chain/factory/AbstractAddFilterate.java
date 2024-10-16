package com.wuhao.project.service.chain.factory;

import com.wuhao.project.service.chain.AddFilterate;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public abstract class AbstractAddFilterate implements AddFilterate {

    private AddFilterate addFilterate;

    @Override
    public AddFilterate addNext(AddFilterate addFilterate) {
        this.addFilterate = addFilterate;
        return addFilterate;
    }

    @Override
    public AddFilterate next() {
        return addFilterate;
    }
}
