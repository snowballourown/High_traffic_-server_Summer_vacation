package challenge_summer.bigtraffic.service;


import challenge_summer.bigtraffic.domain.Member;
import challenge_summer.bigtraffic.dto.member.MemberRequest;
import challenge_summer.bigtraffic.dto.member.MemberResponse;
import challenge_summer.bigtraffic.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    @Transactional
    public void createMember(MemberRequest memberCreateRequest) {
        if (memberCreateRequest.name() == null || memberCreateRequest.name().isBlank()) {
            throw new IllegalArgumentException("회원 이름은 필수입니다.");
        }
        Member member = new Member(memberCreateRequest.name());
        memberRepository.createMember(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse findById(Long Id) {
       Member member = memberRepository.findById(Id).orElseThrow(() ->
               new IllegalArgumentException("해당 회원은 존재하지 않습니다"));
        return new MemberResponse(member.getId(), member.getName());
    }

    @Transactional(readOnly = true)
    public List<Member> findByAll() {
        return memberRepository.findByAll();
    }
    @Transactional(readOnly = true)
    public void removeMember(Long Id) {
        memberRepository.removeMember(Id);
    }





}
