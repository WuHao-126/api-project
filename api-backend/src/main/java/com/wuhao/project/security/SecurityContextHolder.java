package com.wuhao.project.security;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SecurityContextHolder {
    private static final TransmittableThreadLocal<Map<String, Object>> THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void set(String key,Object value){
        Map<String, Object> localMap = getLocalMap();
        localMap.put(key,value == null ? "null" : value);
    }

    public static String get(String key){
        Map<String, Object> localMap = getLocalMap();
        return localMap.get(key).toString();
    }

    public static <T> T get(String key,Class<T> clazz){
       Map<String, Object> localMap = getLocalMap();
       return (T)localMap.get(key);
    }

    public static Map<String, Object> getLocalMap() {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<String, Object>();
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
