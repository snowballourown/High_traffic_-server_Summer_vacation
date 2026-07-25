package Challenge_summer.bigTraffic.service;


import Challenge_summer.bigTraffic.domain.Member;
import Challenge_summer.bigTraffic.domain.ScheduleSeat;
import Challenge_summer.bigTraffic.domain.Seat_hold;
import Challenge_summer.bigTraffic.domain.Status;
import Challenge_summer.bigTraffic.dto.SeatHold.SeatHoldResponse;
import Challenge_summer.bigTraffic.repository.MemberRepository;
import Challenge_summer.bigTraffic.repository.ScheduleSeatRepository;
import Challenge_summer.bigTraffic.repository.Seat_holdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Seat_holdService {

    private final Seat_holdRepository seatHoldRepository;
    private final MemberRepository memberRepository;
    private final ScheduleSeatRepository scheduleSeatRepository;

    @Transactional
    public SeatHoldResponse createHold(Long  scheduleSeatIdRequest, Long memberIdRequest) {

        Member member = memberRepository.findById(memberIdRequest)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        ScheduleSeat scheduleSeat = scheduleSeatRepository.findById(scheduleSeatIdRequest)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약은 존재하지않습니다"));


        scheduleSeat.hold();


        Seat_hold seatHold = new Seat_hold(
                scheduleSeat, member, LocalDateTime.now().plusSeconds(10)); // 현재 시간 10초더해서 넣는것임
        seatHoldRepository.create(seatHold);

        SeatHoldResponse seatHoldResponse =
                new SeatHoldResponse(seatHold.getId(), seatHold.getExpiresAt() );



                return seatHoldResponse;
    }

    public List<SeatHoldResponse> seatHoldResponseAll() {
        return seatHoldRepository.findAll().stream().map( seatHold ->
            new SeatHoldResponse(seatHold.getId(), seatHold.getExpiresAt())).toList();
    }
}
