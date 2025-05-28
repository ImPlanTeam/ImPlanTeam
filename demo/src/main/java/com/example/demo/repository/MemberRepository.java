package com.example.demo.repository;

import com.example.demo.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findID(long id);

    Optional<Member> findByID(Long id);

    Optional<Member> findByName(String name);
    List<Member> findAll();
}
