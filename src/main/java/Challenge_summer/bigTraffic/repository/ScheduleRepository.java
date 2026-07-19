package Challenge_summer.bigTraffic.repository;


import Challenge_summer.bigTraffic.domain.Schedule;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ScheduleRepository {

    private  final EntityManager em;

    // 시간 + 이벤트 +
    // event를 불러온다음에 // 스케줄을 불러와야겠네
    public void save(Schedule schedule) { // 저장만
        em.persist(schedule);
    }




}
