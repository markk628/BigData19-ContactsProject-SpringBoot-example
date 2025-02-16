package com.big19.contacts_project.authentication.session;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Spring 한테 가지고 있는 메소들 중 Bean annotation 이 걸린 메소드가 있다고 알려주는 annotation
public class SessionURLFilterConfig {
    // Spring 한테 SessionURLFilter 을 필터로 인증해주는 메소드
    @Bean // Spring 한테 반환된 filter 를 Bean 으로 처리해야된다고 알려주는 annotation
    public FilterRegistrationBean<SessionURLFilter> sessionFilter() {
        FilterRegistrationBean<SessionURLFilter> registrationBean = new FilterRegistrationBean<>();
        // FilterRegistrationBean 에 필터 지정
        registrationBean.setFilter(new SessionURLFilter());
        // 어떤 URL 패턴을 가지고 있는 요청을 필터 처리해야하는지 지정
        registrationBean.addUrlPatterns("/*");
        // 반환 되면서 bean 으로 처리될때 SessionURLFilter 필터로 인증
        return registrationBean;
    }
}
