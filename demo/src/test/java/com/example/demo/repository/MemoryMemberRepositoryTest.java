package com.example.demo.repository;

import com.example.demo.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository() {
        @Override
        public Optional<Member> findID(long id) {
            return Optional.empty();
        }
    };

    @Test
    public void save(){
        Member member = new Member();
        member.setName("spring");
        repository.save(member);

        Member result = repository.findByID(member.getId()).get();
        assertThat(member).isEqualTo(result);

    }
}
