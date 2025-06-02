package What2Do.config;

import What2Do.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("인터셉터 등록 중");
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**") // 로그인 체크를 할 URL 패턴
                .excludePathPatterns(
                    "/login2", "/login2/**","/css/**", "/js/**", "/images/**","/","/resources/**",
                        "/join","/main","/error","/logincheck",
                        "/save","/save2","/logout","/checkId","/what2do","/allask",
                        "/listView","/categoryView");
            // 로그인 체크를 제외할 URL 패턴
    }
}
