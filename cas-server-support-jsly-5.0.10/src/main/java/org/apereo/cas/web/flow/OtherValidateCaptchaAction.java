package org.apereo.cas.web.flow;

import org.apereo.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * description:进行验证码判断的action
 * </p>
 *
 * @author guanfeng_yang
 * @date 2017/11/6 14:12
 */
public class OtherValidateCaptchaAction extends AbstractAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(OtherValidateCaptchaAction.class);
    private static final String CODE = "captchaError";

    protected Event doExecute(RequestContext requestContext) throws Exception {
        HttpServletRequest request = WebUtils.getHttpServletRequest(requestContext);
        //从session中取出servlet生成的验证码
        String kaptchaExpected = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        //获取用户页面输入的验证码
        String kaptchaReceived = requestContext.getRequestParameters().get("captcha");
        //校验验证码是否正确
        if (kaptchaReceived == null) {
            return getNullError(requestContext);
        } else if (!kaptchaReceived.equalsIgnoreCase(kaptchaExpected)) {
            return getError(requestContext);
        }
        return null;
    }

    private Event getNullError(RequestContext requestContext) {
        MessageContext messageContext = requestContext.getMessageContext();
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.defaultText("验证码不能为空");
        messageContext.addMessage(messageBuilder.error().code("cas.captcha.null").build());
        return this.getEventFactorySupport().event(this, CODE);
    }

    private Event getError(RequestContext requestContext) {
        MessageContext messageContext = requestContext.getMessageContext();
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.defaultText("验证码错误");
        messageContext.addMessage(messageBuilder.error().code("cas.captcha.error").build());
        return this.getEventFactorySupport().event(this, CODE);
    }
}
