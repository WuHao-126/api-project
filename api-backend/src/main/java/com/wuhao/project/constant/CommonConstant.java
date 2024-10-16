package com.wuhao.project.constant;

/**
 * 通用常量
 *
 */
public interface CommonConstant {

    /**
     * 升序
     */
    String SORT_ORDER_ASC = "ascend";

    /**
     * 降序
     */
    String SORT_ORDER_DESC = " descend";


    public enum Ids {
        /**
         * 雪花算法
         */
        SnowFlake,
        /**
         * 日期算法
         */
        ShortCode,
        /**
         * 随机算法
         */
        RandomNumeric;
    }

    String BLOG_LIKE="api:blog:like:";

    String BLOG_Collect="api:blog:collect:";

    String EMAIL_CODE="api:email:code:";
    String BLOG_LIKE_HOT="api:blog:hot:like";
}
