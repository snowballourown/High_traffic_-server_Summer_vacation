package Challenge_summer.bigTraffic.service;


import Challenge_summer.bigTraffic.domain.Seat;
import Challenge_summer.bigTraffic.dto.seat.SeatCreateRequest;
import Challenge_summer.bigTraffic.dto.seat.SeatResponse;
import Challenge_summer.bigTraffic.repository.SeatRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class SeatService {


    private final SeatRepository seatRepository;


    @Transactional
    public void SeatCreate(SeatCreateRequest seatCreateRequest) {
        Seat seat = new Seat();
        seat.setSeatName(seatCreateRequest.name());
        seatRepository.create(seat);
    }


    @Transactional(readOnly = true)
    public List<SeatResponse> seatResponseList(){
        List<Seat> seats= seatRepository.findAll();
        return seats.stream().map(seat -> new SeatResponse(seat.getId(),seat.getSeatName())).toList();
    }


    @Transactional
    public SeatResponse seatFindById(Long id) {
        Seat seat = seatRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 좌석은 존재하지않음"));
        return new SeatResponse(seat.getId(),seat.getSeatName());
    }



}
