package com.yzh.server1.encryptable;

import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyDetector;
import org.springframework.stereotype.Component;

/**
 * 自定义发现器
 *
 * @author yuanzhihao
 * @since 2022/1/27
 */
//@Component("encryptablePropertyDetector")
public class Base64EncryptablePropertyDetector implements EncryptablePropertyDetector {
    private static final String PREFIX = "password:";

    @Override
    public boolean isEncrypted(String property) {
        if (property == null) {
            return false;
        }
        return property.startsWith(PREFIX);
    }

    @Override
    public String unwrapEncryptedValue(String property) {
        return property.substring(PREFIX.length());
    }
}
