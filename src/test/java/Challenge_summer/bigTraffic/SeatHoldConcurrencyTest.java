package Challenge_summer.bigTraffic;


import Challenge_summer.bigTraffic.service.Seat_holdService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@RequiredArgsConstructor
class SeatHoldConcurrencyTest {

    private final Seat_holdService seatHoldService;
    static long scheduleSeatId =1;
    static long memberId =1;

    @Test
    void 같은_좌석에_100명이_동시에_선점한다() throws Exception {
        int requestCount = 100;

        ExecutorService executorService =  Executors.newFixedThreadPool(requestCount);

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(requestCount);

        for (int i = 0; i < requestCount; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await();

                    // 자리 선점 서비스 호출
                    seatHoldService.createHold(scheduleSeatId, memberId);
                } catch (Exception e) {
                    // 실패 횟수 기록
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown(); // 100개 작업 출발
        doneLatch.await();      // 100개 작업 완료까지 main 테스트 스레드 대기

        // DB 상태와 성공 개수 검증
        executorService.shutdown();
    }
}
