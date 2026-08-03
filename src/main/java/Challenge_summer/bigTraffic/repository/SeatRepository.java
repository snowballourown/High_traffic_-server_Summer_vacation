package challenge_summer.bigtraffic.repository;


import challenge_summer.bigtraffic.domain.Seat;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class SeatRepository {
    //CRUD 만들고 좌서 번호 저장
    private final EntityManager em;

    public void create(Seat seat) {
        em.persist(seat);
    }

    public List<Seat> findAll() {
       return em.createQuery("select s from Seat s", Seat.class).getResultList();
    }

    public Optional<Seat> findById(Long id) {
        //event 마다 자동으로
        return Optional.ofNullable(em.find(Seat.class, id));
    }

}
