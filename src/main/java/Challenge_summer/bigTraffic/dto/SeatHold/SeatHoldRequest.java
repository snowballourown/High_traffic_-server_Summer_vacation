package Challenge_summer.bigTraffic.dto.SeatHold;


import Challenge_summer.bigTraffic.domain.Member;

public record SeatHoldRequest (
        Long memberId,
        Long scheduleSeatId
){ }
