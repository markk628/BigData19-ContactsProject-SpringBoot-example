package com.big19.contacts_project.controller;

import com.big19.contacts_project.authentication.AuthenticationService;
import com.big19.contacts_project.authentication.session.SessionManager;
import com.big19.contacts_project.model.member.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// 로그인/회원가입 관련된 URL 들을 처리하는 controller
@Controller
public class AuthenticationController {

    // 로그인/회원가입에 관련된 논리를 처리하는 Service
    @Autowired
    private AuthenticationService authenticationService;

    // 세션 관련된 논리를 처리하는 Service
    @Autowired
    private SessionManager sessionManager;

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "authentication/login";
    }

    // 로그인을 처리하는 post request
    // 사용자가 login.mustache 에 입력한 사용자 정보는 MemberDTO 에 저장되어 있음
    // 로그인이 실패하면 사용자에게 실패했다는 메새지를 login.mustache 에 보내기위해 RedirectAttributes 필요함
    @PostMapping("/login")
    String login(MemberDTO dto, RedirectAttributes attributes) {
        // AuthenticationService 의 authenticate 메소드로 로그인 시도
        if (this.authenticationService.authenticate(dto)) {
            // 로그인이 성공이면 세션을 생성
            this.sessionManager.createSession(dto.getId());
            // 모든 연락처를 보여주는 페이지로 redirect
            return "redirect:/contacts";
        }
        // 로그인이 실패이면 RedirectAttributes 에 로그인 실패 메새지를 login.mustache 에 보낸다
        attributes.addFlashAttribute("msg", "누구새요");
        return "redirect:/login";
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signUpPage() {
        return "authentication/signup";
    }

    // 회원가입을 처리하는 post request
    // 사용자가 signup.mustache 에 입력한 사용자 정보는 MemberDTO 에 저장되어 있음
    // 회원가입이 실패하면 사용자에게 실패했다는 메새지를 signup.mustache 에 보내기위해 RedirectAttributes 필요함
    // 회원가입이 성공하면 사용자에게 성공했다는 메새지를 signup.mustache 에 보내기위해 RedirectAttributes 필요함
    @PostMapping("/signup")
    public String singUp(MemberDTO dto, RedirectAttributes attributes) {
        if (this.authenticationService.isDuplicateId(dto.getId())) {
            attributes.addFlashAttribute("msg", "이미 있는 ID 입니다");
            return "redirect:/signup";
        }
        this.authenticationService.signUp(dto);
        attributes.addFlashAttribute("msg", "회원가입 성공");
        return "redirect:/login";
    }

    // 로그아웃
    @PostMapping("/logout")
    public String logout() {
        this.sessionManager.invalidateSession();
        return "redirect:/login";
    }
}
