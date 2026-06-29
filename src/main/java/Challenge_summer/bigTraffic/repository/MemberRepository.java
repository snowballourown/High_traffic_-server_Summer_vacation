package Challenge_summer.bigTraffic.repository;

import Challenge_summer.bigTraffic.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository
{

    public void createMember(Member member);

    public void removeMember(Member member);

    public Optional<Member> findById(Long id);

    public List<Member> findByAll();

    public List<Member> findByName(String name);
}
