package org.apereo.cas.web.flow;

import org.apereo.cas.authentication.model.UsernamePasswordCaptchaCredential;
import org.springframework.webflow.engine.ActionState;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.execution.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * description: 动态添加验证码流程
 * </p>
 *
 * @author guanfeng_yang
 * @date 2017/11/6 13:45
 */
public class OtherCaptchaWebflowConfigurer extends AbstractCasWebflowConfigurer {
    public OtherCaptchaWebflowConfigurer() {
    }

    protected void doInitialize() throws Exception {
        final Flow flow = getLoginFlow();

        //重新创建mode ,UsernamePasswordCaptchaCredential添加captcha
        createFlowVariable(flow, CasWebflowConstants.VAR_ID_CREDENTIAL, UsernamePasswordCaptchaCredential.class);

        ActionState state = (ActionState) flow.getState("realSubmit");
        List<Action> currentActions = new ArrayList();
        state.getActionList().forEach(currentActions::add);
        currentActions.forEach((a) -> {
            state.getActionList().remove(a);
        });
        state.getActionList().add(this.createEvaluateAction("otherValidateCaptchaAction"));
        currentActions.forEach((a) -> {
            state.getActionList().add(a);
        });
        state.getTransitionSet().add(this.createTransition("captchaError", "initializeLoginForm"));
    }

}
