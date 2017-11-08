package org.apereo.cas.configuration;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * <p>
 * description:自定义cas配置文件的Properties类，以获取配置文件的自定义的参数，具体使用参考springboot的配置文件这一块
 * </p>
 *
 * @author guanfeng_yang
 * @date 2017/11/6 18:21
 */
public class OtherAuthenticationProperties {
    @NestedConfigurationProperty
    private OtherJdbcAuthenticationProperties jdbc;

    public OtherJdbcAuthenticationProperties getJdbc() {
        return jdbc;
    }

    public void setJdbc(OtherJdbcAuthenticationProperties jdbc) {
        this.jdbc = jdbc;
    }
}
