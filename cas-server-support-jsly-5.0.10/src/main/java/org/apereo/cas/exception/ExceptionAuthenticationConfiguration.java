package org.apereo.cas.exception;

import org.apereo.cas.adaptors.jdbc.OtherExceptionHandler;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


/**
 * <p>
 * description: 自定义处理异常的handler注册类
 * </p>
 *
 * @author guanfeng_yang
 * @date 2017/11/7 17:33
 */
@Configuration("exceptionAuthenticationConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class ExceptionAuthenticationConfiguration {

    @Autowired
    private CasConfigurationProperties casProperties;

    @Bean
    public OtherExceptionHandler otherExceptionHandler() {
        final OtherExceptionHandler h = new OtherExceptionHandler();
        h.setErrors(casProperties.getAuthn().getExceptions().getExceptions());
        return h;
    }
}
