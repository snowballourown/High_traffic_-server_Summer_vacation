package challenge_summer.bigtraffic.service;


import challenge_summer.bigtraffic.domain.*;
import challenge_summer.bigtraffic.dto.TestDataResponse;
import challenge_summer.bigtraffic.repository.EventRepository;
import challenge_summer.bigtraffic.repository.ScheduleRepository;
import challenge_summer.bigtraffic.repository.ScheduleSeatRepository;
import challenge_summer.bigtraffic.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestDataService {

    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleSeatRepository scheduleSeatRepository;



    @Transactional
    public TestDataResponse createScheduleSeats10000() {

            Event event = new Event("최초 리센느 팬콘서트");
            eventRepository.CreateEvent(event);

            List<Seat> seats = new ArrayList<>();

            // 좌석 100개를 먼저 생성
            for (int i = 1; i <= 100; i++) {
                Seat seat = new Seat("A" + i);
                seatRepository.create(seat);
                seats.add(seat);
            }

            long startTime = System.currentTimeMillis();
            Long firstScheduleId = null;

            // 회차 100개 생성
            for (int scheduleNumber = 1; scheduleNumber <= 100; scheduleNumber++) {
                Schedule schedule = new Schedule(
                        event,
                        LocalDateTime.now().plusDays(scheduleNumber)
                );
                scheduleRepository.save(schedule);

                if (firstScheduleId == null) {
                    firstScheduleId = schedule.getId();
                }

                // 각 회차에 좌석 100개 배치
                for (int seatNumber = 1; seatNumber <= 100; seatNumber++) {
                    int sequence = (scheduleNumber - 1) * 100 + seatNumber;

                    Status status =
                            sequence % 20 == 0 ? Status.HELD
                                    : sequence % 10 == 0 ? Status.CONFIRMED
                                    : Status.AVAILABLE;

                    scheduleSeatRepository.create(
                            new ScheduleSeat(
                                    seats.get(seatNumber - 1),
                                    schedule,
                                    status
                            )
                    );
                }
            }

            long count = scheduleSeatRepository.count();
            long elapsed = System.currentTimeMillis() - startTime;



            System.out.println("ScheduleSeat 개수 = " + count);
            System.out.println("생성 시간 = " + elapsed + "ms");

            return new TestDataResponse(firstScheduleId, 100, 100, count);
    }


}
