## 예매시스템 API 기능을 등록 나열하기:   
조회하기, 등록하기, 예약한다, 취소한다 

form형태를 쓰지는 않을 꺼니 -> POST, GET으로 단정지을 필요없다 

일단  
* 공연    events
* 공연 회차	   schedules
* 실제 좌석 자체	seats
* 회차별 좌석 상태	schedule-seats
* 좌석 임시 선점	holds
* 예약 확정	reservations
* 결제	payments
* 사용자	members 
---- 
# 예매 시스템 요구사항 초안


## event(공연)

* 사용자는 전체 공연 목록을 조회할 수 있다.
* 사용자는 특정 공연의 상세 정보를 조회할 수 있다.
* 관리자는 새로운 공연을 등록할 수 있다.

1. 공연 조회 GET : /events/{eventId}   ->  공연에 대한 정보 누가나오는지 그런
2. 공연 전체조회 GET : /events          ->  전체 보는거 
3. 공연 등록 POST : /events            ->  공연 등록

---
##  공연 회차
* 하나의 공연은 여러 회차를 가질 수 있다.   (데이터)  
* 사용자는 특정 공연의 회차 목록을 조회할 수 있다.
* 각 회차는 공연 날짜와 시작 시간을 가진다. (데이터)

1. 하나의 공연이 시간대 조회 GET : /events/{eventId}/schedules

--
##  좌석
* 좌석은 공연장이 가진 고정 좌석이다.
* 같은 좌석이라도 공연 회차마다 예약 상태가 다를 수 있다.
* 사용자는 특정 공연 회차의 좌석 상태를 조회할 수 있다.
* 좌석 상태는 `AVAILABLE`, `HELD`, `CONFIRMED` 중 하나를 가진다.

* 공연 좌석 상태 조회 GET : /events/{eventId}/schedules/{scheduleId}/schedule-seats


---

### hold(좌석 선점), 도움 받음

1. 사용자는 예약 가능한 좌석을 일정 시간 동안 선점할 수 있다.
2. 이미 선점된 좌석은 다른 사용자가 다시 선점할 수 없다.
4. 선점 시간이 지나면 좌석은 다시 예약 가능한 상태가 된다.

* 공연 좌석 선점 요청 POST : /events/{eventId}/schedules/{scheduleId}/schedule-seats/{scheduleSeatId}/holds




---

### member(사용자)
* 사용자는 서비스에 등록할 수 있다.
* 사용자는 자신의 정보를 조회할 수 있다.
1. 사용자 등록 POST : /members
2. 사용자 조회 GET : /members/{memberId}
---


## 6. 예약 확정

1. 사용자는 자신이 선점한 좌석에 대해 결제를 진행할 수 있다.
2. 결제가 완료되면 좌석은 예약 확정 상태가 된다.
3. 예약 확정된 좌석은 다른 사용자가 예약할 수 없다.



---

## 7. 중복 요청

1. 같은 좌석에 동시에 여러 선점 요청이 들어와도 한 명만 성공해야 한다.
2. 같은 결제 요청이 여러 번 들어와도 결제는 한 번만 처리되어야 한다.


| 기능 | Method | URI | Request | Response | Status |
|---|---|---|---|---|---|
| 회원 등록 | POST | /members | name | 없음 | 201 Created |
| 공연 목록 조회 | GET | /events | 없음 | 공연 목록 | 200 OK |
| 공연 회차 조회 | GET | /events/{eventId}/schedules | 없음 | 회차 목록 | 200 OK |
| 좌석 상태 조회 | GET | /events/{eventId}/schedules/{scheduleId}/schedule-seats | 없음 | 좌석 상태 목록 | 200 OK |
| 좌석 선점 | POST | /events/{eventId}/schedules/{scheduleId}/schedule-seats/{scheduleSeatId}/holds | memberId | holdId, expiresAt | 201 Created |
| 결제 요청 | POST | /payments | holdId, memberId | 없음 | 201 Created |
| 예약 조회 | GET | /members/{memberId}/reservations | 없음 | 예약 목록 | 200 OK |