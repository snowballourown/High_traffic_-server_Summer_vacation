package challenge_summer.bigtraffic.repository;


import challenge_summer.bigtraffic.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@AllArgsConstructor
public class MemberRepository  {

    private final EntityManager em;


    public void createMember(Member member) {
        em.persist(member);
    }

    public void removeMember(Long id) {
        Member member = em.find(Member.class, id);
        em.remove(member);
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public List<Member> findByName(String name) {
        return em.createQuery(
                        "select m from Member m where m.name = :name",
                        Member.class
                )
                .setParameter("name", name)
                .getResultList();

    }

    public List<Member> findByAll() {
        return em.createQuery("select m from Member m").getResultList();
    }


}
