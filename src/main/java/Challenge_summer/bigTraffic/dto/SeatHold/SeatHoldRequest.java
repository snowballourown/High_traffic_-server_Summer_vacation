package challenge_summer.bigtraffic.dto.seathold;


import challenge_summer.bigtraffic.domain.Member;

public record SeatHoldRequest (
        Long memberId,
        Long scheduleSeatId
){ }
