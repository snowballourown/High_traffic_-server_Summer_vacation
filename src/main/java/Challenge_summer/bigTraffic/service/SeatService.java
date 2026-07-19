package Challenge_summer.bigTraffic.service;


import Challenge_summer.bigTraffic.domain.Seat;
import Challenge_summer.bigTraffic.dto.seat.SeatCreateRequest;
import Challenge_summer.bigTraffic.dto.seat.SeatResponse;
import Challenge_summer.bigTraffic.repository.SeatRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class SeatService {


    private final SeatRepository seatRepository;


    @Transactional
    public void SeatCreate(SeatCreateRequest seatCreateRequest) {
        Seat seat = new Seat();
        seat.setSeatName(seatCreateRequest.getName());
        seatRepository.create(seat);
    }


    @Transactional(readOnly = true)
    public List<SeatResponse> seatResponseList(){
        List<Seat> seats= seatRepository.findAll();
        return seats.stream().map(seat -> new SeatResponse(seat.getSeatName())).toList();
    }


}
