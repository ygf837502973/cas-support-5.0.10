package org.apereo.cas.exception;

import org.apereo.cas.authentication.AuthenticationException;

/**
 * <p>
 * description:自定义异常处理，密码错误提示异常
 * </p>
 *
 * @author guanfeng_yang
 * @date 2017/11/7 17:47
 */
public class InvalidReTryTimeException extends AuthenticationException {
    private static final long serialVersionUID = -6699562791525619208L;

    public InvalidReTryTimeException() {
    }

    public InvalidReTryTimeException(String message) {
        super(message);
    }
}
