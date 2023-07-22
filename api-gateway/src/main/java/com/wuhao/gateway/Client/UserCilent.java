package com.wuhao.gateway.Client;

import com.wuhao.common.entity.InterfaceInfo;
import com.wuhao.common.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("apibackend")
public interface UserCilent {
    @PostMapping("/user/accesskey")
    User getAccessKeyUser(@RequestParam("accessKey") String accessKey);

    @PostMapping("/userInterfaceInfo/invokecount")
    Boolean invokeCount(@RequestParam("interfaceInfoId") long interfaceInfoId,@RequestParam("userId") long userId);

    @PostMapping("/interface/url")
    InterfaceInfo getInterfaceInfoByUrl(@RequestParam("url") String url,@RequestParam("method") String method);
}
