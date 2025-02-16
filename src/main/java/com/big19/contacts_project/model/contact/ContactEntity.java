package com.big19.contacts_project.model.contact;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // 클래스가 테이블에 매핑될 수 있음 지정
@Table(name="members_and_contacts") // table 명 지정
@NoArgsConstructor // 파라미터 없는 생성자
@AllArgsConstructor // 모든 변수가 파라미터로 있는 생성자
@Data // getter, setter, toString
public class ContactEntity {
    @Id // primary key 지정
    @Column(name="contact_id") // column 명 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 새로 생성한 연락처에 ID를 AUTO INCREMENT 하라고 지정
    private int id;

    @Column(name="contact_name")
    private String name;

    @Column(name="contact_number")
    private String phoneNumber;

    @Column(name="contact_address")
    private String address;

    @Column(name="relationship")
    private String relationship;

    @Column(name="member_id")
    private String memberId;
}
