package Challenge_summer.bigTraffic.service;

import Challenge_summer.bigTraffic.dto.event.EventResponse;
import Challenge_summer.bigTraffic.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final EventService eventService;

    public void createSchdule() {
        // 이벤트 이름 + 시간


    }



}
