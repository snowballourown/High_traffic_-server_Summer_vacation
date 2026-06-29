package Challenge_summer.bigTraffic.service;


import Challenge_summer.bigTraffic.domain.Member;
import Challenge_summer.bigTraffic.dto.MemberCreateRequest;
import Challenge_summer.bigTraffic.repository.MemberRepository;
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
    public void createMember(MemberCreateRequest memberCreateRequest) {
        if (memberCreateRequest.getName() == null || memberCreateRequest.getName().isBlank()) {
            throw new IllegalArgumentException("회원 이름은 필수입니다.");
        }
        Member member = new Member();
        member.setName(memberCreateRequest.getName());
        memberRepository.createMember(member);

    }

    @Transactional(readOnly = true)
    public Optional<Member> findById(Long Id) {
        return memberRepository.findById(Id);
    }

    public List<Member> findByAll() {
        return memberRepository.findByAll();
    }

    public void removeMember(Long Id) {
        Member member = findById(Id).get();
        if (member == null) throw new IllegalArgumentException("삭제할값이 없습니다.");
        memberRepository.removeMember(member);
    }





}
