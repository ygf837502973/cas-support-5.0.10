package org.apereo.cas.adaptors.jdbc;

import org.apereo.cas.exception.InvalidReTryTimeException;
import org.apereo.cas.web.flow.AuthenticationExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;

/**
 * <p>
 * description: 扩展AuthenticationExceptionHandler以出处理自定义异常的消息传递，供前端显示
 * </p>
 *
 * @author guanfeng_yang
 * @date 2017/11/7 17:36
 */
public class OtherExceptionHandler extends AuthenticationExceptionHandler {
    private transient Logger logger = LoggerFactory.getLogger(this.getClass());

    public OtherExceptionHandler() {
        super();
    }

    public String handle(Exception e, MessageContext messageContext) {
        if (e instanceof InvalidReTryTimeException) { //如果是自定义异常，直接传递异常消息，不从message.properties文件获取
            messageContext.addMessage((new MessageBuilder()).error().defaultText(e.getMessage()).build());
            return "InvalidReTryTimeException";
        } else {
            return super.handle(e, messageContext);
        }
    }
}