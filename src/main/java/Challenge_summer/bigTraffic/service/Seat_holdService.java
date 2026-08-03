package challenge_summer.bigtraffic.service;


import challenge_summer.bigtraffic.domain.Member;
import challenge_summer.bigtraffic.domain.ScheduleSeat;
import challenge_summer.bigtraffic.domain.Seat_hold;
import challenge_summer.bigtraffic.domain.Status;
import challenge_summer.bigtraffic.dto.seathold.SeatHoldResponse;
import challenge_summer.bigtraffic.repository.MemberRepository;
import challenge_summer.bigtraffic.repository.ScheduleSeatRepository;
import challenge_summer.bigtraffic.repository.Seat_holdRepository;
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

        ScheduleSeat scheduleSeat = scheduleSeatRepository.findByIdWithPessimisticLock(scheduleSeatIdRequest)
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
