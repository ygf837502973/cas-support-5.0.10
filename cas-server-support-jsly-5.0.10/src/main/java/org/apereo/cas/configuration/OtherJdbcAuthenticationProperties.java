package org.apereo.cas.configuration;

import org.apereo.cas.configuration.model.support.jdbc.JdbcAuthenticationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;

/**
 * <p>
 * description:自定义cas配置文件的Properties类，以获取配置文件的自定义的参数，具体使用参考springboot的配置文件这一块
 * </p>
 *
 * @author guanfeng_yang
 * @date 2017/11/6 18:15
 */
public class OtherJdbcAuthenticationProperties {
    @NestedConfigurationProperty
    private List<OtherJdbcAuthenticationProperties.OtherQuery> query;

    public List<OtherJdbcAuthenticationProperties.OtherQuery> getQuery() {
        return this.query;
    }

    public void setQuery(List<OtherQuery> query) {
        this.query = query;
    }

    public static class OtherQuery extends JdbcAuthenticationProperties.Query {
        //重试次数
        private int retryTimes;
        //锁定账号sql
        private String lockedSql;

        private String fieldPassword;

        private String fieldLocked;

        private String fieldDisabled;

        public int getRetryTimes() {
            return retryTimes;
        }

        public void setRetryTimes(int retryTimes) {
            this.retryTimes = retryTimes;
        }

        public String getLockedSql() {
            return lockedSql;
        }

        public void setLockedSql(String lockedSql) {
            this.lockedSql = lockedSql;
        }

        public String getFieldPassword() {
            return fieldPassword;
        }

        public void setFieldPassword(String fieldPassword) {
            this.fieldPassword = fieldPassword;
        }

        public String getFieldLocked() {
            return fieldLocked;
        }
        public void setFieldLocked(String fieldLocked) {
            this.fieldLocked = fieldLocked;
        }

        public String getFieldDisabled() {
            return fieldDisabled;
        }

        public void setFieldDisabled(String fieldDisabled) {
            this.fieldDisabled = fieldDisabled;
        }
    }

}
