package com.big19.contacts_project.controller;

import com.big19.contacts_project.authentication.AuthenticationService;
import com.big19.contacts_project.authentication.session.SessionManager;
import com.big19.contacts_project.model.contact.ContactDTO;
import com.big19.contacts_project.model.contact.ContactEntity;
import com.big19.contacts_project.model.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// 연락처 관련된 URL 들을 처리하는 controller
@Controller
public class ContactController {

    // Database 에 연락처 관련된 쿼리문을 실행하는 Repository
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AuthenticationService authenticationService;

    // 세션 관련된 논리를 처리하는 Service
    @Autowired
    private SessionManager sessionManager;

    // 연락처 입력 실패시 입력한 정보를 다시 보여줄수있게 ContactDTO 일시 저장할 공간
    private ContactDTO attemptedInsertInfo = null;

    // 연락처 입력 페이지
    @GetMapping("/contacts/add")
    public String addContactPage(Model model) {
        model.addAttribute("pageTitle", "연락처 추가");
        if (this.attemptedInsertInfo != null) {
            model.addAttribute("contact", this.attemptedInsertInfo);
            this.attemptedInsertInfo = null;
        }
        return "contacts/insert";
    }

    // 연락처 입력을 처리하는 메소드
    // contacts.mustache 에 입력결과를 보내기위해 RedirectAttributes 이 필요함
    @PostMapping("/contacts/add")
    public String addContact(RedirectAttributes attributes, ContactDTO dto) {
        int relationship;
        switch (dto.getRelationship()) {
            case "가족":
                relationship = 1;
                break;
            case "친구":
                relationship = 2;
                break;
            case "기타":
                relationship = 3;
                break;
            default:
                // 입력한 관계가 가족, 친구, 기타 중 하나가 아니면 /contacts/add 로 redirect
                this.attemptedInsertInfo = dto;
                attributes.addFlashAttribute("msg", "관계는 가족, 친구, 기타 중 하나야합니다");
                return "redirect:/contacts/add";
        }
        // 연락처 태이블에 연락처 추가
        this.contactRepository.insertContact(dto.getName(),
                                             dto.getPhoneNumber(),
                                             dto.getAddress(),
                                             relationship);
        // 연락처 추가 후 연락처 + 사용자 태이블에 사용자 아이디와 마지막으로 입력한 연락처 아이디 추가
        this.contactRepository.insertMemberContact(this.sessionManager.getCurrentMemberId(),
                                                   this.contactRepository.getLastInsertedId());
        attributes.addFlashAttribute("msg", dto.getName() + " 입력 성공");
        return "redirect:/contacts";
    }

    // 모든 연락처 페이지
    // contacts.mustache 에 연락처들을 보내기위해 Model 이 필요함
    @GetMapping("/contacts")
    public String viewContacts(Model model) {
        // 사용자 id를 이용해 database 에서 가저온 사용자의 연락처를 contacts.mustache 에 보낸다
        model.addAttribute("pageTitle", "연락처");
        model.addAttribute("contacts", this.contactRepository.getAllContacts(this.sessionManager.getCurrentMemberId()));
        return "contacts/contacts";
    }

    // 사용자가 검색한 연락처들을 보여주는 패이지
    @GetMapping("/contacts/{name}/search")
    public String viewContactsWithName(Model model, @PathVariable String name) {
        model.addAttribute("pageTitle", "연락처");
        model.addAttribute("contacts", this.contactRepository.getContactsWithName(this.sessionManager.getCurrentMemberId(), name));
        return "contacts/contacts";
    }

    // 연락처 검색을 처리하는 메소드
    // RequestParam 을 통해 contacts.mustache 에 있는 검색 form 에서 검섹한 이름을 가저온다
    @PostMapping("/contacts/search")
    public String searchContacts(@RequestParam("contact-to-search") String name) {
        return "redirect:/contacts/" + name + "/search";
    }

    // 연락처 수정 패이지
    // 세션에 저장된 사용자 id 와 PathVariable 로 받은 연락처 id 를 이용해
    //  - id 를 가지고 있는 연락처가 있는지 확인
    //  = 사용자가 만든 연락처인지 확인
    @GetMapping("/contacts/{id}/edit")
    public String editContactPage(Model model, RedirectAttributes attributes, @PathVariable int id) {
        ContactEntity contact = this.contactRepository.getContact(this.sessionManager.getCurrentMemberId(), id);
        if (contact != null) {
            // 연락처가 null 이 아닉고 사용자가 만든 연락처 이면 정보를 update.mustache 에 보냄
            model.addAttribute("pageTitle", contact.getName() + " 수정");
            model.addAttribute("contact", contact);
        } else {
            // 아니면 뭐하냐고 물어고 /contacts 로 redirect
            attributes.addFlashAttribute("msg", "고객님의 연락처만 보세요");
            return "redirect:/contacts";
        }
        return "contacts/update";
    }

    // 연락처 수정을 처리하는 메소드
    // 세션에 저장된 사용자 id 와 PathVariable 로 받은 연락처 id 를 이용해
    //  - id 를 가지고 있는 연락처가 있는지 확인
    //  = 사용자가 만든 연락처인지 확인
    @PostMapping("/contacts/{id}/edit")
    public String editContact(RedirectAttributes attributes, @PathVariable int id, ContactDTO dto) {
        int relationship;
        if (this.contactRepository.getContact(this.sessionManager.getCurrentMemberId(), id) != null) {
            // 연락처가 null 이 아닉고 사용자가 만든 연락처 이면 정보 수정 허락
            switch (dto.getRelationship()) {
                case "가족":
                    relationship = 1;
                    break;
                case "친구":
                    relationship = 2;
                    break;
                case "기타":
                    relationship = 3;
                    break;
                default:
                    // 입력한 관계가 가족, 친구, 기타 중 하나가 아니면 /contacts/연락처 id/edit 로 redirect
                    attributes.addFlashAttribute("msg", "관계는 가족, 친구, 기타 중 하나야합니다");
                    return "redirect:/contacts/" + id + "/edit";
            }
            // 연락처 수정후 연락처 페이지로 redirect
            this.contactRepository.updateContact(dto.getId(),
                                                 dto.getName(),
                                                 dto.getPhoneNumber(),
                                                 dto.getAddress(),
                                                 relationship);
            return "redirect:/contacts";
        }
        // 아니면 뭐하냐고 물어고 /contacts 로 redirect
        attributes.addFlashAttribute("msg", "고객님의 연락처만 보세요");
        return "redirect:/contacts";
    }

    // 연락처 삭제 처리하는 메소드
    // 세션에 저장된 사용자 id 와 PathVariable 로 받은 연락처 id 를 이용해
    //  - id 를 가지고 있는 연락처가 있는지 확인
    //  = 사용자가 만든 연락처인지 확인
    @PostMapping("/contacts/{id}/delete")
    public String deleteContact(RedirectAttributes attributes, @PathVariable int id) {
        String memberId = this.sessionManager.getCurrentMemberId();
        if (this.contactRepository.getContact(memberId, id) != null) {
            // 연락처가 null 이 아닉고 사용자가 만든 연락처 이면 정보 삭제 허락
            // 우선 연락처 + 사용자 테이블에서 정보 삭제
            this.contactRepository.deleteMemberContact(memberId, id);
            // 연락처 삭제후 /contacts 로 redirect
            this.contactRepository.deleteContact(id);
            attributes.addFlashAttribute("msg", "삭제 성공");
        } else {
            // 아니면 뭐하냐고 물어고 /contacts 로 redirect
            attributes.addFlashAttribute("msg", "고객님의 연락처만 보세요");
        }
        return "redirect:/contacts";
    }
}
