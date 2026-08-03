package challenge_summer.bigtraffic.repository;


import challenge_summer.bigtraffic.domain.Event;
import challenge_summer.bigtraffic.domain.Member;
import challenge_summer.bigtraffic.domain.Schedule;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleRepository {

    private  final EntityManager em;

    // 시간 + 이벤트 +
    // event를 불러온다음에 // 스케줄을 불러와야겠네
    public void save(Schedule schedule) { // 저장만
        em.persist(schedule);
    }

    // 모든 스케줄을 나오게해라
    public List<Schedule> findAll() {
        return em.createQuery("select s from Schedule s",Schedule.class)
                .getResultList();
    }

    // 스케줄 id 찾기
    public Optional<Schedule> findById(Long Id) {
        return em.createQuery("select s from Schedule s where s.id =:id", Schedule.class)
                .setParameter("id", Id).getResultList().stream().findAny();
    }


    // 해당 이벤트의 스케줄을 다나오게해라
    public List<Schedule> findByEventId(Long eventId) {
         return em.createQuery("select s from Schedule s where s.event.id =:eventId"
                , Schedule.class)
                .setParameter("eventId", eventId)
                .getResultList();
    }








}
