package com.big19.contacts_project.model.member;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // 클래스가 테이블에 매핑될 수 있음 지정
@Table(name="members") // table 명 지정
@NoArgsConstructor // 파라미터 없는 생성자
@AllArgsConstructor // 모든 변수가 파라미터로 있는 생성자
@Data // getter, setter, toString
public class MemberEntity {
    @Id // primary key 지정
    @Column(name="member_id") // column 명 지정
    private String id;

    @Column(name="member_password")
    private String password;
}
