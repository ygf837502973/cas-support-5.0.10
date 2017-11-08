package org.apereo.cas.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * <p>
 * description:自定义cas配置文件的Properties类，以获取配置文件的自定义的参数，具体使用参考springboot的配置文件这一块
 * </p>
 *
 * @author guanfeng_yang
 * @date 2017/11/6 18:13
 */
@ConfigurationProperties("cas")
public class OtherCasConfigurationProperties {
    @NestedConfigurationProperty
    private OtherAuthenticationProperties authn;

    public OtherAuthenticationProperties getAuthn() {
        return authn;
    }

    public void setAuthn(OtherAuthenticationProperties authn) {
        this.authn = authn;
    }
}
