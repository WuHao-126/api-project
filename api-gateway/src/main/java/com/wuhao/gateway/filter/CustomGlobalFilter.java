package com.wuhao.gateway.filter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wuhao.gateway.entity.TimeoutInterface;
import com.wuhao.gateway.entity.User;
import com.wuhao.gateway.mapper.TimeoutInterfaceMapper;
import com.wuhao.gateway.mapper.UserMapper;
import com.wuhao.util.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private UserMapper userMapper;

    @Autowired()
    private StringRedisTemplate redisTemplate;

    @Autowired
    private TimeoutInterfaceMapper timeoutInterfaceMapper;

    //黑白名单集合
    private static final List<String> IP_WHITE_LIST= Arrays.asList("127.0.0.1");
    private static final String INTERFACE_HOST = "http://www.wuhao.ltd:8090";
    private static final List<String> IP_TEST=Arrays.asList("0:0:0:0:0:0:0:1","127.0.0.1");
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();
        // 1. 请求日志
        ServerHttpRequest request = exchange.getRequest();
        String path = INTERFACE_HOST + request.getPath().value();
        String method = request.getMethod().toString();
        log.info("请求唯一标识：" + request.getId());
        log.info("请求路径：" + path);
        log.info("请求方法：" + method);
        log.info("请求参数：" + request.getQueryParams());
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求来源地址：" + sourceAddress);
        log.info("请求来源地址：" + request.getRemoteAddress());
        ServerHttpResponse response = exchange.getResponse();
        //是否是测试数据
//        if(IP_TEST.contains(sourceAddress)){
//            return chain.filter(exchange);
//        }
        // 2. 黑白名单
//        if(!IP_WHITE_LIST.contains(sourceAddress)){
//            handleNoAuth(response);
//        }
        // 3. 统一的鉴权
        HttpHeaders headers = request.getHeaders();
        //获取公钥
        String accessKey = headers.getFirst("accessKey");
//        //获取随机数，防止重返攻击
        String nonce = headers.getFirst("nonce");
//        //获取时间戳
//        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
//        //todo 没有确保唯一性
        String redisSccessKey = redisTemplate.opsForValue().get("accessKey");
        if(StringUtils.isEmpty(redisSccessKey)){
            User user = userMapper.selectOne(new QueryWrapper<User>().eq("accessKey", accessKey));
            if (user==null) {
                throw new RuntimeException("无权限");
            }
            redisSccessKey=user.getSecretKey();
            redisTemplate.opsForValue().set("api:gateway:accessKey:"+accessKey,redisSccessKey,7,TimeUnit.DAYS);
        }

        //防止重放攻击
//        if (Long.parseLong(nonce) > 10000) {
//            throw new RuntimeException("无权限");
//        }
//        Long currentTime=System.currentTimeMillis()/1000;
//        Long FIVE_MINUTES=60*5L;
        //时间和当前时间不能超过 5 分钟
//        if (currentTime-Long.parseLong(timestamp)>FIVE_MINUTES) {
//            handleNoAuth(response);
//        }
        //进行再次加密
//        String serverSign = SignUtils.getSign(redisSccessKey);
        if (!sign.equals(redisSccessKey)) {
            throw new RuntimeException("无权限");
        }
        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    // 在请求处理之后记录结束时间，并计算响应时间
                    long duration = System.currentTimeMillis() - startTime;
                    Long interfaceId = timeoutInterfaceMapper.getInterfaceId(path);
                    if(duration/1000>3){
                        TimeoutInterface timeoutInterface=new TimeoutInterface();
                        timeoutInterface.setInterfaceId(interfaceId);
                        timeoutInterface.setResponseTime(duration/1000.0);
                        timeoutInterface.setCreateTime(LocalDateTime.now());
                        timeoutInterfaceMapper.insert(timeoutInterface);
                    }
                    timeoutInterfaceMapper.updateInterfaceTotal(interfaceId);
                        log.info("Request to {} took {} ms", exchange.getRequest().getURI(), duration);
                })
        );
    }
    @Override
    public int getOrder() {
        return 0;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response ){
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }
}
