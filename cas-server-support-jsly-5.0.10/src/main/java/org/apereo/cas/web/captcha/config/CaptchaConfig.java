package org.apereo.cas.web.captcha.config;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * description: 注册码servlet注册
 * </p>
 *
 * @author guanfeng_yang
 * @date 2017/8/23 17:54
 */
@Configuration
public class CaptchaConfig extends WebMvcConfigurerAdapter {
    @Bean
    public ServletRegistrationBean getDemoServlet() {
        KaptchaServlet kaptchaServlet = new KaptchaServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean();
        registrationBean.setServlet(kaptchaServlet);
        List<String> urlMappings = new ArrayList<String>();
        urlMappings.add("/Kaptcha.jpg");
        registrationBean.setUrlMappings(urlMappings);
        registrationBean.setLoadOnStartup(1);
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("kaptcha.border", "no");
        initParameters.put("kaptcha.textproducer.font.color", "black");
        initParameters.put("kaptcha.textproducer.char.space", "5");
        initParameters.put("kaptcha.textproducer.char.length", "5");
        initParameters.put("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        initParameters.put("kaptcha.textproducer.char.string", "abcdefghjkmnoporstuvwxyz2345678ABCDEFGHJKMNPORSTUVWXYZ");
        registrationBean.setInitParameters(initParameters);
        return registrationBean;
    }


}
