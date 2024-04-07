package com.wuhao.project.support.sensitive;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public interface TextWorker {
    //添加敏感词
    void insert(String word);

    //搜索敏感词
    Boolean search(String word);

    //文本搜索敏感词
    Boolean containsSensitiveWord(String text);

}
