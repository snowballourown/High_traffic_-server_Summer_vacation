package Challenge_summer.bigTraffic.service;

import Challenge_summer.bigTraffic.domain.Event;
import Challenge_summer.bigTraffic.domain.Schedule;
import Challenge_summer.bigTraffic.domain.Seat;
import Challenge_summer.bigTraffic.dto.event.EventRequest;
import Challenge_summer.bigTraffic.dto.event.EventResponse;
import Challenge_summer.bigTraffic.dto.schedule.ScheduleRequest;
import Challenge_summer.bigTraffic.dto.schedule.ScheduleResponse;
import Challenge_summer.bigTraffic.dto.seat.SeatResponse;
import Challenge_summer.bigTraffic.repository.EventRepository;
import Challenge_summer.bigTraffic.repository.ScheduleRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final EventRepository eventRepository;


    @Transactional
    public void createSchedule(ScheduleRequest scheduleRequest) {
        // 이벤트 이름 + 시간
        // event 이름
        // 이벤트 시작시간
        // 해당 공연이 존재하는지 확인하는거임

        Event event = eventRepository.findById(scheduleRequest.eventId()).orElseThrow(() ->
                new IllegalArgumentException("공연이 존재하지않습니다. "));

        Schedule schedule = new Schedule(event, scheduleRequest.localDateTime());
        scheduleRepository.save(schedule);
    }

    // 스케줄 전체 조회
    @Transactional(readOnly = true)
    public List<ScheduleResponse> AllSchedule() {
        List<Schedule> scheduleResponses = scheduleRepository.findAll();

        return scheduleResponses.stream().map(schedule -> new ScheduleResponse(schedule.getId(),
                schedule.getEvent().getId(),
                schedule.getEvent().getName()  ,
                schedule.getStartTime())).toList();
    }

    // 스케줄 하나 조회
    @Transactional(readOnly = true)
    public ScheduleResponse findById(Long Id) {
        Schedule schedule = scheduleRepository.findById(Id).orElseThrow(() -> new IllegalArgumentException("스케줄은 없습니다"));
        return new ScheduleResponse(schedule.getId(),
                schedule.getEvent().getId(),
                schedule.getEvent().getName(),
                schedule.getStartTime());
    }



    @Transactional(readOnly = true)
    public List<ScheduleResponse> findEventSchedules(Long Id) { //이벤트의 스케줄 확인용
       return scheduleRepository.findByEventId(Id).stream().map(
               schedule -> new ScheduleResponse(schedule.getId(),
                schedule.getEvent().getId(),
                schedule.getEvent().getName(),
                schedule.getStartTime())).toList();
    }



}



