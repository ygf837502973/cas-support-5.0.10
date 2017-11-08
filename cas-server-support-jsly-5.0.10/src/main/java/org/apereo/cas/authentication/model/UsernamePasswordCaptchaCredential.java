package org.apereo.cas.authentication.model;

import org.apereo.cas.authentication.UsernamePasswordCredential;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>
 * description:自定义Credential，添加验证码字段
 * </p>
 *
 * @author guanfeng_yang
 * @date 2017/11/6 10:47
 */
public class UsernamePasswordCaptchaCredential extends UsernamePasswordCredential {
    private static final long serialVersionUID = -8026533193321791927L;
    @NotNull
    @Size(min = 1, message = "required.captcha")
    private String captcha;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
