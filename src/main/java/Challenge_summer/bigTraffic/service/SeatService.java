package challenge_summer.bigtraffic.service;


import challenge_summer.bigtraffic.domain.Seat;
import challenge_summer.bigtraffic.dto.seat.SeatCreateRequest;
import challenge_summer.bigtraffic.dto.seat.SeatResponse;
import challenge_summer.bigtraffic.repository.SeatRepository;

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
        Seat seat = new Seat(seatCreateRequest.name());
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
