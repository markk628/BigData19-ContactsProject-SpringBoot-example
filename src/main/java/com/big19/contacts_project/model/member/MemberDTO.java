package com.big19.contacts_project.model.member;

import lombok.Data;

@Data // getter, setter, toString
public class MemberDTO {
    private String id;
    private String password;

    // 가지고 있는 정보로 사용자 entity 생성과 반환
    public MemberEntity toEntity() {
        return new MemberEntity(this.id, this.password);
    }
}