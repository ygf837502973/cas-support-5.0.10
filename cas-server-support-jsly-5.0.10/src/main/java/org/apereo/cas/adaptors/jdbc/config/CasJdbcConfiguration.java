package org.apereo.cas.adaptors.jdbc.config;

import com.google.common.base.Predicates;
import org.apache.commons.lang3.StringUtils;
import org.apereo.cas.adaptors.jdbc.OtherQueryDataBaseAuthenticationHandler;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.authentication.principal.PrincipalResolver;
import org.apereo.cas.authentication.support.PasswordPolicyConfiguration;
import org.apereo.cas.configuration.OtherCasConfigurationProperties;
import org.apereo.cas.configuration.OtherJdbcAuthenticationProperties;
import org.apereo.cas.configuration.support.Beans;
import org.apereo.cas.services.ServicesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * <p>
 * description:注册jdbc验证的handler,参考cas-server-supper-jdbc源码进行改造
 * </p>
 *
 * @author guanfeng_yang
 * @date 2017/11/6 14:47
 */
@Configuration("casJdbcConfiguration")
@EnableConfigurationProperties(OtherCasConfigurationProperties.class)
public class CasJdbcConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(CasJdbcConfiguration.class);
    @Autowired(required = false)
    @Qualifier("queryPasswordPolicyConfiguration")
    private PasswordPolicyConfiguration queryPasswordPolicyConfiguration;

    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;


    @Autowired
    private OtherCasConfigurationProperties otherCasProperties;

    @Autowired
    @Qualifier("personDirectoryPrincipalResolver")
    private PrincipalResolver personDirectoryPrincipalResolver;

    @Autowired
    @Qualifier("authenticationHandlersResolvers")
    private Map authenticationHandlersResolvers;

    @PostConstruct
    public void initializeJdbcAuthenticationHandlers() {
        otherCasProperties.getAuthn().getJdbc().getQuery()
                .forEach(b -> authenticationHandlersResolvers.put(
                        queryDatabaseAuthenticationHandler(b),
                        personDirectoryPrincipalResolver));
    }


    //update by guanfeng_yang 2017.11.6
    //TODO
    private AuthenticationHandler queryDatabaseAuthenticationHandler(final OtherJdbcAuthenticationProperties.OtherQuery b) {
        final OtherQueryDataBaseAuthenticationHandler h =
                new OtherQueryDataBaseAuthenticationHandler();
        h.setDataSource(Beans.newHickariDataSource(b));
        h.setSql(b.getSql());
        h.setRetryTimes(b.getRetryTimes());
        h.setLockedSql(b.getLockedSql());
        h.setFieldPassword(b.getFieldPassword());
        h.setFieldLocked(b.getFieldLocked());
        h.setFieldDisabled(b.getFieldDisabled());

        h.setPasswordEncoder(Beans.newPasswordEncoder(b.getPasswordEncoder()));
        h.setPrincipalNameTransformer(Beans.newPrincipalNameTransformer(b.getPrincipalTransformation()));

        if (queryPasswordPolicyConfiguration != null) {
            h.setPasswordPolicyConfiguration(queryPasswordPolicyConfiguration);
        }

        h.setPrincipalNameTransformer(Beans.newPrincipalNameTransformer(b.getPrincipalTransformation()));

        h.setPrincipalFactory(jdbcPrincipalFactory());
        h.setServicesManager(servicesManager);

        if (StringUtils.isNotBlank(b.getCredentialCriteria())) {
            h.setCredentialSelectionPredicate(credential -> Predicates.containsPattern(b.getCredentialCriteria())
                    .apply(credential.getId()));
        }
        return h;
    }

    @Bean
    @ConditionalOnMissingBean(name = "jdbcPrincipalFactory")
    public PrincipalFactory jdbcPrincipalFactory() {
        return new DefaultPrincipalFactory();
    }
}
