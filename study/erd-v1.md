#  예약프로그램 ERD

event
* PK : eventId

schedule
* PK : scheduleId 
* FK : eventId

seat
* PK : seatId

schedule_seat
* PK : scheduleSeatId
* FK : scheduleId
* FK : seatId

member
* PK : memberId

seat_hold
* PK : holdId
* FK : memberId
* FK : scheduleSeatId

payment
* PK : paymentId
* FK : holdId

reservation
* PK : reservationId
* FK : memberId
* FK : scheduleSeatId
* FK : paymentId

여기 어떤데이터가 에약 경쟁의 중심인지 말할수있는가?

reservation은 결과이고, schedule_seat는 경쟁 대상이다
