package Challenge_summer.bigTraffic.repository;

import Challenge_summer.bigTraffic.domain.Member;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@AllArgsConstructor
public class MemoryMemberRepositoryTest {

    private final MemberRepository memberRepository;


    @Test
    public void save() {
        Member member = new Member();
        member.setName("spring");

        memberRepository.createMember(member);

        Optional<Member> result = memberRepository.findById(member.getId());
        assertEquals(member, result);
    }

    @Test
    void findByName() {
        Member member = new Member();
        member.setName("spring1");
        memberRepository.createMember(member);

        Member member1 = new Member();
        member1.setName("spring2");
        memberRepository.createMember(member1);


        Member result = memberRepository.findByName("spring1").get(0);// 동명이인 일수도있어서 보냄
        assertEquals(member, result);
    }
}