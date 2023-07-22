package com.wuhao.util;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.Digester;

public class SignUtils {
    private static final String ENCRYPTION="wuhao";
    public static String getSign(String secretKey){
        Digester md5=new Digester(DigestAlgorithm.SHA256);
        String content=ENCRYPTION+"."+secretKey;
         return md5.digestHex(content);
    }
}
