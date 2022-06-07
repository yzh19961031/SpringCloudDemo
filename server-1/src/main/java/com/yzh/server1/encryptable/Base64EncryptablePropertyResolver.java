package com.yzh.server1.encryptable;

import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

/**
 * 自定义解析器
 *
 * @author yuanzhihao
 * @since 2022/1/27
 */
//@Component("encryptablePropertyResolver")
public class Base64EncryptablePropertyResolver implements EncryptablePropertyResolver {
    @Autowired
    private Base64EncryptablePropertyDetector encryptablePropertyDetector;

    @Override
    public String resolvePropertyValue(String value) {
        return Optional.ofNullable(value)
                .filter(encryptablePropertyDetector::isEncrypted)
                .map(resolveValue -> {
                    final String unwrapEncryptedValue = encryptablePropertyDetector.unwrapEncryptedValue(resolveValue);
                    return new String(Base64.getDecoder().decode(unwrapEncryptedValue),
                            StandardCharsets.UTF_8);
                })
                .orElse(value);
    }
}
