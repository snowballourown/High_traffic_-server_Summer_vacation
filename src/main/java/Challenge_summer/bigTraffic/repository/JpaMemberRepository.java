package Challenge_summer.bigTraffic.repository;


import Challenge_summer.bigTraffic.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@AllArgsConstructor
public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;


    @Override
    public void createMember(Member member) {
        em.persist(member);
    }

    @Override
    public void removeMember(Member member) {
        em.remove(member);
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public List<Member> findByName(String name) {
        return em.createQuery(
                        "select m from Member m where.name = :name",
                        Member.class
                )
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public List<Member> findByAll() {
        return em.createQuery("select m from Member.m").getResultList();
    }


}
