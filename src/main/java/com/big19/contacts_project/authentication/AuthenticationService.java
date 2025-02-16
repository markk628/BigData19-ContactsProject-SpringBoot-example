package com.big19.contacts_project.authentication;

import com.big19.contacts_project.model.member.MemberDTO;
import com.big19.contacts_project.model.member.MemberEntity;
import com.big19.contacts_project.model.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    // Database 에 사용자 관련된 쿼리문을 실행하는 Repository
    @Autowired
    private MemberRepository memberRepository;

    // 로그인
    // 받은 사용자 DTO 로 로그인이 성공했는지 여부하는 메소드
    public boolean authenticate(MemberDTO dto) {
        // dto 의 id를 이용해 사용자 정보를 Database 에서 가저와 member 에 대입
        Optional<MemberEntity> member = this.memberRepository.findById(dto.getId());
        // member 가 null 이 아니고 member 의 비밀번호가 password 랑 같으면 true 반환 아니면 false
        return member.isPresent() && member.get().getPassword().equals(dto.getPassword());
    }

    // 회원가입
    // 받은 사용자 entity 를 저장하는 메소드
    public void signUp(MemberDTO dto) {
        this.memberRepository.save(dto.toEntity());
    }

    // 받은 id가 이미 Database 에 있는지 여부하는 메소드
    public boolean isDuplicateId(String id) {
        return this.memberRepository.findById(id).isPresent();
    }
}
