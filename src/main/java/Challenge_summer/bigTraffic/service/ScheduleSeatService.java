package Challenge_summer.bigTraffic.service;


import Challenge_summer.bigTraffic.domain.*;
import Challenge_summer.bigTraffic.dto.scheduleSeat.ScheduleSeatRequest;
import Challenge_summer.bigTraffic.dto.scheduleSeat.ScheduleSeatResponse;
import Challenge_summer.bigTraffic.repository.ScheduleRepository;
import Challenge_summer.bigTraffic.repository.ScheduleSeatRepository;
import Challenge_summer.bigTraffic.repository.SeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ScheduleSeatService {

    private final ScheduleSeatRepository scheduleSeatRepository;



    private final SeatRepository seatRepository;
    private final ScheduleRepository scheduleRepository;

    //
    @Transactional
    public void ScheduleSeatServiceCreate(ScheduleSeatRequest scheduleSeatRequest) {
        if (scheduleSeatRequest.scheduleId() == null ) {
            throw new IllegalArgumentException("scheduleId 필수입니다.");
        }

        // 해당 ID들이 있나 존재 확인

        Schedule schedule = scheduleRepository.findById(scheduleSeatRequest.scheduleId())
                .orElseThrow(() -> new IllegalArgumentException("해당 스케줄은없습니다"));


        List<Seat> seats = seatRepository.findAll();

        for (Seat seat : seats) { //  있는 좌석을 모두 넣기
            scheduleSeatRepository.create(
                    new ScheduleSeat(
                            seat,
                            schedule,
                            Status.AVAILABLE)
            );
        }
    }


    @Transactional(readOnly = true)
    public List<ScheduleSeatResponse> findAll() {
        return scheduleSeatRepository.findAll().stream().map(scheduleSeat ->
                        new ScheduleSeatResponse(scheduleSeat.getId(),
                                scheduleSeat.getSchedule().getId(),
                                scheduleSeat.getSeat().getId(),
                        scheduleSeat.getStatus()))
                .toList();

    }





}





















