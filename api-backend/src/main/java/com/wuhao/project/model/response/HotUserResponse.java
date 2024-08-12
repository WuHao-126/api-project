package com.wuhao.project.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: 热门文章返回值
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotUserResponse {
    private Long userId;
    private String username;
    private Integer writeCount;
}
