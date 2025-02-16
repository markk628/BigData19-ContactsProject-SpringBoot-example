package com.big19.contacts_project.model.repository;

import com.big19.contacts_project.model.contact.ContactEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Database 에 연락처 관련된 쿼리문을 실행하는 Repository
@Repository
public interface ContactRepository extends CrudRepository<ContactEntity, Integer> {
    String GET_LAST_INSERTED_ID =
            "SELECT LAST_INSERT_ID() AS id";

    String INSERT_CONTACT =
            "INSERT INTO contacts (contact_name, contact_number, contact_address, relationship_id) " +
            "VALUES (:name, :phoneNumber, :address, :relationship)                                 ";

    String INSERT_MEMBER_CONTACT_MAP =
            "INSERT INTO members_contacts  " +
            "VALUES (:memberId, :contactId)";

    String GET_ALL_CONTACTS =
            "WITH contacts_relationship AS (                 " +
            "   SELECT c.contact_id,                         " +
            "          c.contact_name,                       " +
            "          c.contact_number,                     " +
            "          c.contact_address,                    " +
            "          r.relationship                        " +
            "     FROM contacts c                            " +
            "    INNER JOIN relationship r                   " +
            "       ON c.relationship_id = r.relationship_id " +
            "),                                              " +
            "members_and_contacts AS (                       " +
            "   SELECT c.contact_id,                         " +
            "          c.contact_name,                       " +
            "          c.contact_number,                     " +
            "          c.contact_address,                    " +
            "          c.relationship,                       " +
            "          m.member_id                           " +
            "     FROM members_contacts m                    " +
            "    INNER JOIN contacts_relationship c          " +
            "       ON m.contact_id = c.contact_id           " +
            ")                                               " +
            "SELECT *                                        " +
            "  FROM members_and_contacts m                   " +
            " WHERE m.member_id = :memberId                  ";

    String GET_CONTACTS_WITH_NAME =
            "WITH contacts_relationship AS (                 " +
            "   SELECT c.contact_id,                         " +
            "          c.contact_name,                       " +
            "          c.contact_number,                     " +
            "          c.contact_address,                    " +
            "          r.relationship                        " +
            "     FROM contacts c                            " +
            "    INNER JOIN relationship r                   " +
            "       ON c.relationship_id = r.relationship_id " +
            "),                                              " +
            "members_and_contacts AS (                       " +
            "   SELECT c.contact_id,                         " +
            "          c.contact_name,                       " +
            "          c.contact_number,                     " +
            "          c.contact_address,                    " +
            "          c.relationship,                       " +
            "          m.member_id                           " +
            "     FROM members_contacts m                    " +
            "    INNER JOIN contacts_relationship c          " +
            "       ON m.contact_id = c.contact_id           " +
            ")                                               " +
            "SELECT *                                        " +
            "  FROM members_and_contacts m                   " +
            " WHERE m.member_id = :memberId                  " +
            "   AND m.contact_name LIKE %:name%              ";

    String GET_CONTACT_BY_ID =
            "WITH contacts_relationship AS (                 " +
            "   SELECT c.contact_id,                         " +
            "          c.contact_name,                       " +
            "          c.contact_number,                     " +
            "          c.contact_address,                    " +
            "          r.relationship                        " +
            "     FROM contacts c                            " +
            "    INNER JOIN relationship r                   " +
            "       ON c.relationship_id = r.relationship_id " +
            "),                                              " +
            "members_and_contacts AS (                       " +
            "   SELECT c.contact_id,                         " +
            "          c.contact_name,                       " +
            "          c.contact_number,                     " +
            "          c.contact_address,                    " +
            "          c.relationship,                       " +
            "          m.member_id                           " +
            "     FROM members_contacts m                    " +
            "    INNER JOIN contacts_relationship c          " +
            "       ON m.contact_id = c.contact_id           " +
            ")                                               " +
            "SELECT *                                        " +
            "  FROM members_and_contacts m                   " +
            " WHERE m.member_id = :memberId                  " +
            "   AND m.contact_id = :contactId                ";

    String UPDATE_CONTACT =
            "UPDATE contacts c                        " +
            "   SET c.contact_name = :name,           " +
            "       c.contact_number = :phoneNumber,  " +
            "       c.contact_address = :address,     " +
            "       c.relationship_id = :relationship " +
            " WHERE c.contact_id = :contactId         ";

    String DELETE_CONTACT =
            "DELETE FROM contacts m           " +
            " WHERE m.contact_id = :contactId ";

    String DELETE_MEMBER_CONTACT =
            "DELETE FROM members_contacts m   " +
            " WHERE m.member_id = :memberId   " +
            "   AND m.contact_id = :contactId ";

    // 마지막으로 입력받은 테이블에 삽입된 마지막 행의 AUTO_INCREMENT ID를 반환하는 메소드
    @Query(value = GET_LAST_INSERTED_ID, nativeQuery = true)
    int getLastInsertedId();

    // 연락처 추가 하는 메소드
    @Modifying // 데이타를 수정할때 필요한 annotation (UPDATE, DELETE, INSERT)
    @Transactional // 수정중 문제가 있으면 ROLLBACK 없으면 COMMIT 해주는 annotation
    @Query(value = INSERT_CONTACT, nativeQuery = true)
    void insertContact(@Param("name") String name,
                       @Param("phoneNumber") String phoneNumber,
                       @Param("address") String address,
                       @Param("relationship") int relationship);

    // 연락처 + 사용자 table 에 추가 하는 메소드
    @Modifying // 데이타를 수정할때 필요한 annotation (UPDATE, DELETE, INSERT)
    @Transactional // 수정중 문제가 있으면 ROLLBACK 없으면 COMMIT 해주는 annotation
    @Query(value = INSERT_MEMBER_CONTACT_MAP, nativeQuery = true)
    void insertMemberContact(@Param("memberId") String memberId,
                             @Param("contactId") int contactId);

    // 사용자가 만든 연락처들을 반환하는 메소드
    @Query(value = GET_ALL_CONTACTS, nativeQuery = true)
    List<ContactEntity> getAllContacts(@Param("memberId") String memberId);

    // 사용자가 입력한 이름을 가지고있는 연락저를 반환하는 메소드
    @Query(value = GET_CONTACTS_WITH_NAME, nativeQuery = true)
    List<ContactEntity> getContactsWithName(@Param("memberId") String memberId,
                                            @Param("name") String name);

    // 사용자가 선택한 연락저를 반환하는 메소드
    @Query(value = GET_CONTACT_BY_ID, nativeQuery = true)
    ContactEntity getContact(@Param("memberId") String memberId,
                             @Param("contactId") int contactId);

    // 연락처 수정 하는 메소드
    @Modifying // 데이타를 수정할때 필요한 annotation (UPDATE, DELETE, INSERT)
    @Transactional // 수정중 문제가 있으면 ROLLBACK 없으면 COMMIT 해주는 annotation
    @Query(value = UPDATE_CONTACT, nativeQuery = true)
    void updateContact(@Param("contactId") int contactId,
                       @Param("name") String name,
                       @Param("phoneNumber") String phoneNumber,
                       @Param("address") String address,
                       @Param("relationship") int relationship);

    // 연락처 삭제 하는 메소드
    @Modifying // 데이타를 수정할때 필요한 annotation (UPDATE, DELETE, INSERT)
    @Transactional // 수정중 문제가 있으면 ROLLBACK 없으면 COMMIT 해주는 annotation
    @Query(value = DELETE_CONTACT, nativeQuery = true)
    void deleteContact(@Param("contactId") int contactId);

    // 연락처 + 사용자 삭제 하는 메소드
    @Modifying // 데이타를 수정할때 필요한 annotation (UPDATE, DELETE, INSERT)
    @Transactional // 수정중 문제가 있으면 ROLLBACK 없으면 COMMIT 해주는 annotation
    @Query(value = DELETE_MEMBER_CONTACT, nativeQuery = true)
    void deleteMemberContact(@Param("memberId") String memberId,
                             @Param("contactId") int contactId);
}
