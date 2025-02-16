package com.big19.contacts_project.authentication.session;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component // Spring Container 에 bean 으로 등록하는 annotation
public class SessionURLFilter implements Filter {
    // 로그인을 안한 사용자를 login 페이지로 redirect 하는 메소드
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 요청을 처리하는 변수
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // 응답을 처리하는 변수
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // 요청에 세션을 가져오고, session 에 대입
        // HttpServletRequest 의 getSession() 은 세션을 가져오는 메소드인데,
        // false 를 입력하면 (getSession(false)) 세션이 없으면 null 을 반환 하라는 거고
        // true 를 입력하면 (getSession(true)) 세션이 없는 경우 새로운 세션을 반환
        HttpSession session = httpRequest.getSession(false);
        // 요청이 로그인/회원가입 페이지인지 여부하는 boolean
        boolean isAuthenticating = (httpRequest.getRequestURI().startsWith("/login")) || (httpRequest.getRequestURI().startsWith("/signup"));
        // 세션이 없거나 세션에 입증된 사용자가 없는지 여부하는 boolean
        boolean sessionIsNullOrHasNoUser = (session == null || session.getAttribute("user") == null);
        // 요청이 로그인/회원가입 페이지가 아니고 세션이 없거나 세션에 입증된 사용자 없으면 로그인 페이지로 redirect
        if (!isAuthenticating && sessionIsNullOrHasNoUser) {
            httpResponse.sendRedirect("/login");
            return;
        }
        // 아니면 보내준다
        chain.doFilter(request, response);
    }
}
