package com.big19.contacts_project.model.repository;

import com.big19.contacts_project.model.member.MemberEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends CrudRepository<MemberEntity, String> { }
