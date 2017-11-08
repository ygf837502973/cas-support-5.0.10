package org.apereo.cas.web.flow.config;

import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.web.flow.CasWebflowConfigurer;
import org.apereo.cas.web.flow.OtherCaptchaWebflowConfigurer;
import org.apereo.cas.web.flow.OtherValidateCaptchaAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;
import org.springframework.webflow.execution.Action;

/**
 * <p>
 * description:
 * </p>
 *
 * @author guanfeng_yang
 * @date 2017/11/6 14:17
 */
@Configuration("casCaptchaConfiguration")
@EnableConfigurationProperties({CasConfigurationProperties.class})
public class OtherCaptchaConfiguration {
    @Autowired
    @Qualifier("loginFlowRegistry")
    private FlowDefinitionRegistry loginFlowDefinitionRegistry;
    @Autowired
    private FlowBuilderServices flowBuilderServices;
    @Autowired
    private CasConfigurationProperties casProperties;

    @ConditionalOnMissingBean(
            name = {"captchaWebflowConfigurer"}
    )
    @Bean
    public CasWebflowConfigurer captchaWebflowConfigurer() {
        OtherCaptchaWebflowConfigurer w = new OtherCaptchaWebflowConfigurer();
        w.setLoginFlowDefinitionRegistry(this.loginFlowDefinitionRegistry);
        w.setFlowBuilderServices(this.flowBuilderServices);
        return w;
    }

    @RefreshScope
    @Bean
    public Action otherValidateCaptchaAction() {
        return new OtherValidateCaptchaAction();
    }
}
