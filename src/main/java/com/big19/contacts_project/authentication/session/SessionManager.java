package com.big19.contacts_project.authentication.session;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component // Spring Container 에 bean 으로 등록하는 annotation
public class SessionManager {
    // 현제 세션 정보
    @Autowired
    private HttpSession session;

    // 세션이 살아있으면 사용자의 id를 반환하는 메소드
    // 만료됬으면 null 반환
    public String getCurrentMemberId() {
        return (String)this.session.getAttribute("user");
    }

    // 세션을 생성하는 메소드
    public void createSession(String memberId) {
        // 세션에 사용자 id 저장
        this.session.setAttribute("user", memberId);
        // 1시간 후 자동 세션 만료
        this.session.setMaxInactiveInterval(3600);
    }

    // 세션을 무효화하는 메소드
    public void invalidateSession() {
        this.session.invalidate();
    }
}
