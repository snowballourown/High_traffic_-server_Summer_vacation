package challenge_summer.bigtraffic.repository;

import challenge_summer.bigtraffic.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class MemoryMemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;


    @Test
    public void save() {
        Member member = new Member("Spring");


        memberRepository.createMember(member);

        Member result = memberRepository.findById(member.getId()).orElseThrow();
        assertEquals(member, result);
    }

    @Test
    void findByName() {
        Member member = new Member("spring1");
        memberRepository.createMember(member);

        Member member1 = new Member("spring2");
        memberRepository.createMember(member1);


        Member result = memberRepository.findByName("spring1").get(0);// 동명이인 일수도있어서 보냄
        assertEquals(member, result);
    }
}
