package com.Implan.What2Do.Service;

import com.Implan.What2Do.Repository.MemberRepository;
import com.Implan.What2Do.domain.Member;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    //회원등록
    public String join(Member member) {
        validateDuplicateMember(member); //중복회원
        memberRepository.save(member);
        return member.getId();
    }
    
    //중복회원
    private void validateDuplicateMember(Member member) {
        memberRepository.findById(member.getId())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
    
    //전체조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //회원 자세히보기
    public Member viewDetail(String id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
    }

    //회원삭제
    public void delete(String id){
        memberRepository.deleteById(id);
    }

    //회원수정 불러오기
    public Member mod(String id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
    }

    //회원수정 저장
    public void update(
            Member member) {
        Member mod = memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        mod.setPw(member.getPw());
        mod.setName(member.getName());
        mod.setAddr(member.getAddr());
        mod.setTel(member.getTel());

        memberRepository.save(mod);
    }
}