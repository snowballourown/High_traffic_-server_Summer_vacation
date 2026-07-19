package Challenge_summer.bigTraffic.repository;


import Challenge_summer.bigTraffic.domain.Seat;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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


}
