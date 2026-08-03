package challenge_summer.bigtraffic.scheduler;


import challenge_summer.bigtraffic.domain.Seat_hold;
import challenge_summer.bigtraffic.domain.Status;
import challenge_summer.bigtraffic.repository.Seat_holdRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SeatHoldExpirationScheduler {

    private final Seat_holdRepository seatHoldRepository;

    public SeatHoldExpirationScheduler(
            Seat_holdRepository seatHoldRepository
    ) {
        this.seatHoldRepository = seatHoldRepository;
    }

    @Scheduled(fixedDelay = 1000) //1초마다 실행 1000ms
    @Transactional
    public void releaseExpiredHolds() {
/*
            → 현재 시간 구하기
            → 만료 시간이 지난 Seat_hold 조회
            → 연결된 ScheduleSeat를 AVAILABLE로 변경
            → 만료된 Seat_hold 삭제
            → 작업 완료 후 1초 기다림
            → 다시 실행
*
* */
        LocalDateTime now = LocalDateTime.now();

        List<Seat_hold> expiredHolds =          //기한이 선점이 지난좌석들을 불러옴
                seatHoldRepository.findExpired(now);

        for (Seat_hold seatHold : expiredHolds) {
            seatHold.getScheduleSeat()
                    .release(); // 여기서 available로 만드는거임

            seatHoldRepository.delete(seatHold);
        }
    }
}
