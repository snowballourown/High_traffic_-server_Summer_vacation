# 대용량 트래픽 예매 시스템 2개월 로드맵

## 1. 프로젝트 목표

이 프로젝트의 주목적은 다음 두 가지다.

1. 예매 시스템에서 대용량 트래픽과 동시성 문제를 직접 재현하고 해결한다.
2. 구현 과정에서 운영체제, 네트워크, 데이터베이스, Java 동시성 등 CS 지식을 코드와 연결한다.

단순히 API 개수를 늘리는 것이 목표가 아니다. 반드시 문제를 먼저 재현하고,
측정한 결과를 근거로 개선한 뒤 전후 차이를 문서로 남긴다.

최종적으로 다음 질문에 수치와 코드로 답할 수 있어야 한다.

- 같은 좌석에 100명이 동시에 요청하면 왜 중복 선점이 발생하는가?
- 트랜잭션과 락을 적용하면 왜 한 명만 성공하는가?
- 서버는 초당 몇 요청까지 처리하며, 어느 지점에서 느려지거나 실패하는가?
- 병목은 애플리케이션, Tomcat 스레드, DB 커넥션, 쿼리 중 어디에 있는가?
- 인덱스와 쿼리 개선 전후의 응답 시간은 얼마나 달라지는가?
- 장애나 타임아웃이 발생해도 좌석과 결제 데이터가 일관된가?

---

## 2. 진행 원칙

- 기능 구현 → 정상 동작 테스트 → 문제 재현 → 원인 설명 → 개선 → 재측정 순서를 지킨다.
- Redis, Kafka, 분산 락은 필요하다는 측정 결과가 나오기 전에는 도입하지 않는다.
- 실제 PG 결제는 연동하지 않고 성공/실패를 제어할 수 있는 가짜 결제로 구현한다.
- 부하 테스트는 로컬 또는 허가된 테스트 서버에서만 실행한다.
- 평균 응답 시간만 보지 않고 처리량, p95, p99, 실패율을 함께 기록한다.
- 매주 코드 결과물과 학습 문서를 하나 이상 남긴다.

---

## 3. 현재 위치

2026-08-03 기준 현재 위치는 **4주차 Day 18, 동시성 테스트 준비 단계**다.

완료한 내용:

- 요구사항, ERD, API 초안
- Member, Event, Schedule, Seat 기본 API
- ScheduleSeat 생성 및 조회
- 좌석 선점과 자동 만료 처리
- 가짜 Payment와 Reservation 생성
- 회원별 예약 조회 API
- 선점 → 결제 → 예약 기본 흐름 구현
- Java 스레드와 `ExecutorService` 기초 학습

현재 학습 및 구현 내용:

1. `CountDownLatch`의 `startLatch`, `doneLatch` 이해
2. `@SpringBootTest`와 H2 테스트 DB 준비
3. 같은 좌석에 여러 스레드가 접근하는 테스트 작성
4. 락이 없는 상태에서 중복 선점 문제 재현

아직 적용하지 않을 내용:

- 문제 재현 전 낙관적 락 또는 비관적 락 적용
- Redis, Kafka, 분산 락, 대기열
- 쿼리 개선 전 k6 부하 테스트
- 실제 외부 결제 연동

### 현재 확정한 3주 실행 순서

```text
현재 주: 동시성 문제 재현과 락 비교
-> 다음 주: N+1, SQL, 인덱스 개선
-> 다다음 주: k6로 실제 HTTP 부하를 높여 서버 한계 측정
-> 이후: 병목 개선 후 같은 조건으로 재측정
```

#### 현재 주: 동시성 및 락

1. 테스트용 데이터를 만들고 10개 동시 작업이 정상 실행되는지 확인한다.
2. 같은 좌석에 100개 요청을 보내 락 없는 중복 선점을 재현한다.
3. 요청 수, 성공 수, 실패 수, SeatHold 생성 수와 실행 시간을 기록한다.
4. Race Condition과 트랜잭션 타임라인으로 실패 원인을 설명한다.
5. 낙관적 락을 적용하고 충돌 예외와 결과를 기록한다.
6. 비관적 락을 적용하고 DB 대기와 결과를 기록한다.
7. H2 결과를 MySQL에서 다시 검증하고 두 락의 선택 근거를 문서화한다.

#### 다음 주: 조회 성능

1. 회원 예약 조회에서 N+1 문제를 재현한다.
2. fetch join 또는 DTO 조회로 SQL 실행 횟수를 줄인다.
3. 대량 테스트 데이터를 준비한다.
4. MySQL `EXPLAIN`으로 Full Table Scan과 조회 행 수를 확인한다.
5. 필요한 인덱스만 적용하고 개선 전후 실행 시간을 비교한다.
6. 만료 선점 조회 쿼리와 `expires_at` 인덱스를 검토한다.

#### 다다음 주: 서버 한계 측정

1. k6로 좌석 조회와 좌석 선점 HTTP 시나리오를 작성한다.
2. VU를 `10 → 50 → 100 → 300 → 500 → 1,000`처럼 단계적으로 높인다.
3. TPS, 평균 응답 시간, p95, p99, 실패율을 기록한다.
4. CPU, 메모리, Tomcat 스레드와 HikariCP 커넥션 사용량을 함께 확인한다.
5. TPS가 증가하지 않거나 응답 시간이 급증하고 timeout 또는 5xx가 발생하는
   최초 지점을 서버 한계로 기록한다.
6. 병목 하나를 개선한 뒤 동일한 k6 조건으로 재측정한다.

서버를 무작정 종료시키는 것이 목적은 아니다. 요청 증가에 따라 성능이 무너지는
지점과 원인을 수치로 찾고, 개선 전후를 같은 조건으로 비교하는 것이 목적이다.

---

## 4. 8주 전체 흐름

```text
1주차 요구사항/HTTP/네트워크 기초
→ 2주차 JPA/DB 기본 API
→ 3주차 선점/결제/예약 전체 흐름
→ 4주차 동시성 문제 재현
→ 5주차 트랜잭션/락으로 해결
→ 6주차 쿼리/인덱스 성능 개선
→ 7주차 k6 부하 테스트와 서버 한계 측정
→ 8주차 병목 개선, 장애 실험, 최종 문서화
```

---

## 5. 40일 상세 계획

### 1주차: 요구사항, HTTP, 네트워크 기초

목표: 예매 흐름과 API 계약을 확정하고 HTTP 요청이 서버에 도착하는 과정을 이해한다.

#### Day 1

- 프로젝트 목표와 핵심 사용자 흐름 작성
- Event, Schedule, Seat, ScheduleSeat 역할 구분
- 결과물: `docs/requirements.md`
- CS: 클라이언트와 서버, 요청과 응답

#### Day 2

- Member, SeatHold, Payment, Reservation 역할 구분
- 좌석 상태와 선점 상태를 분리해서 설계
- CS: 상태와 상태 전이, 데이터 일관성

#### Day 3

- ERD 확정
- PK, FK, 1:N, 1:1 관계 검토
- CS: 관계형 데이터베이스, 정규화 기초

#### Day 4

- URI, HTTP Method, Request/Response DTO 표 작성
- CS: HTTP Method, 상태 코드, 멱등성의 의미

#### Day 5

- Postman으로 요청 헤더와 JSON Body 실습
- CS: TCP/IP 개요, 포트, HTTP와 JSON 직렬화
- 완료 기준: API 표를 보고 각 요청의 목적을 설명할 수 있다.

### 2주차: JPA와 기본 CRUD

목표: 기본 도메인 API와 JPA 관계를 구현한다.

#### Day 6

- Member, Event 생성/조회
- Entity와 DTO를 분리
- CS: 객체와 테이블의 차이

#### Day 7

- Schedule 생성/조회
- Event와 Schedule의 `@ManyToOne` 연결
- CS: PK/FK와 JOIN

#### Day 8

- Seat 생성/조회
- Repository, Service, Controller 역할 구분
- CS: 계층 분리와 의존성

#### Day 9

- ScheduleSeat 생성/조회
- 새 Schedule에 모든 Seat를 일괄 배치
- CS: 반복 INSERT 비용과 배치 처리 개념

#### Day 10

- 기본 CRUD 통합 확인
- 잘못된 ID, null 요청, 중복 생성 실패 테스트
- CS: ACID, 영속성 컨텍스트, Dirty Checking
- 완료 기준: Postman으로 기본 데이터 생성과 조회가 가능하다.

### 3주차: 좌석 선점, 가짜 결제, 예약

목표: 한 사용자의 정상 예매 흐름을 처음부터 끝까지 완성한다.

#### Day 11

- ScheduleSeat 일괄 생성과 좌석 상태 조회 완성
- `(schedule_id, seat_id)` 중복 방지 검토
- CS: 유일성 제약조건과 데이터 무결성

#### Day 12

- SeatHold Entity, Repository, Service 작성
- `expiresAt`을 서버 시간으로 결정
- CS: 서버 시간, 만료 시간, 상태 전이

#### Day 13 - 현재 다음 작업

- `ScheduleSeat.hold()`, `release()`, `confirm()` 작성
- SeatHoldResponse 작성: `holdId`, `expiresAt`
- `POST /schedule-seats/{scheduleSeatId}/holds` 구현
- 완료 기준: 선점 후 ScheduleSeat이 `HELD`이고 응답에 `holdId`가 있다.

#### Day 14

- 만료된 SeatHold를 찾아 `AVAILABLE`로 복구
- 결제 완료된 좌석은 해제하지 않도록 조건 작성
- 스케줄러 동작과 요청 시 만료 검사의 차이 정리
- CS: polling, 주기 작업, 시간 기반 경쟁 조건

#### Day 15

- 가짜 Payment 성공/실패 API 구현
- 성공 시 `HELD → CONFIRMED`, Reservation 생성
- 실패/만료 시 예약 생성 금지
- 회원 예약 조회 API 구현
- CS: 원자성, 트랜잭션 경계, 실패 시 rollback
- 완료 기준: 선점 → 결제 → 예약 조회 전체 흐름이 Postman에서 동작한다.

### 4주차: Java 동시성과 문제 재현

목표: 락을 적용하기 전에 중복 선점 문제를 실제로 재현한다.

#### Day 16

- 프로세스와 스레드 학습
- stack, heap, context switching 개념 정리
- Java 스레드 생성과 실행 실습

#### Day 17

- Race Condition과 임계 영역 학습
- `synchronized`, `AtomicInteger` 간단 실습
- CS: 원자성, 가시성, 상호 배제

#### Day 18

- `ExecutorService`, `CountDownLatch` 학습
- 여러 작업을 동시에 시작하는 테스트 작성
- CS: 스레드 풀과 작업 큐

#### Day 19

- 같은 `scheduleSeatId`에 100개 선점 요청
- 성공 수와 SeatHold 행 개수 기록
- 락 없이 여러 요청이 성공하는 현상 재현

#### Day 20

- 실패 원인을 타임라인으로 문서화
- SELECT와 UPDATE 사이에 다른 트랜잭션이 들어오는 과정 설명
- 결과물: `docs/concurrency-before-lock.md`
- 완료 기준: 중복 선점 문제를 테스트로 재현하고 원인을 설명할 수 있다.

### 5주차: 트랜잭션, 락, 데이터 일관성

목표: 동시 요청에서도 한 명만 좌석을 선점하도록 만든다.

#### Day 21

- 트랜잭션 ACID와 격리 수준 학습
- Lost Update, Dirty Read, Non-repeatable Read 구분

#### Day 22

- ScheduleSeat 조회에 비관적 락 적용
- `PESSIMISTIC_WRITE`가 만드는 DB 잠금 확인
- CS: lock wait와 timeout

#### Day 23

- 100개 동시 요청 테스트 재실행
- 성공 1개, 실패 99개인지 검증
- 락 적용 전후 시간과 결과 비교

#### Day 24

- 낙관적 락과 `@Version` 학습
- 비관적 락과 낙관적 락의 사용 조건 비교
- 선택한 방식과 이유 문서화

#### Day 25

- 결제 중복 요청 방지 검토
- DB 유일성 제약조건과 간단한 멱등키 적용
- Deadlock 발생 조건과 락 순서 학습
- 결과물: `docs/concurrency-after-lock.md`
- 완료 기준: 동시 선점과 중복 결제가 데이터 중복을 만들지 않는다.

### 6주차: 조회 성능, SQL, 인덱스

목표: 데이터가 많을 때 느린 조회를 재현하고 DB 관점에서 개선한다.

#### Day 26

- ScheduleSeat 테스트 데이터 대량 생성
- 데이터 개수와 생성 방법 기록
- CS: 디스크 I/O와 메모리 접근 비용

#### Day 27

- 좌석 조회 SQL과 JPA 실행 쿼리 확인
- N+1 문제 재현 여부 확인
- fetch join과 DTO 조회 비교

#### Day 28

- MySQL `EXPLAIN` 읽기
- Full Table Scan, rows, key 항목 확인
- CS: 쿼리 옵티마이저와 실행 계획

#### Day 29

- 필요한 복합 인덱스 설계
- B+Tree, 카디널리티, 선택도, 인덱스 순서 학습
- 인덱스 적용 전후 조회 시간 비교

#### Day 30

- 페이지네이션 또는 필요한 범위 조회 적용
- 실행 계획과 측정 결과 문서화
- 결과물: `docs/index-performance.md`
- 완료 기준: 인덱스 전후 차이를 실행 계획과 수치로 설명할 수 있다.

### 7주차: k6 부하 테스트와 서버 한계 측정

목표: 실제 HTTP 부하를 증가시키며 서버가 실패하는 지점을 찾는다.

#### Day 31

- k6 설치와 기본 스크립트 작성
- VU, duration, iteration 의미 학습
- CS: throughput, latency, concurrency 차이

#### Day 32

- 좌석 조회 API 기준선 측정
- 처리량, 평균, p95, p99, 실패율 기록

#### Day 33

- 좌석 선점 API 부하 테스트
- 경쟁 좌석과 서로 다른 좌석 시나리오 분리
- 정합성과 성능을 함께 확인

#### Day 34

- VU를 단계적으로 늘려 서버 한계 확인
- 오류 코드, 타임아웃, CPU, 메모리 기록
- 허가된 테스트 환경에서만 실행

#### Day 35

- Tomcat thread pool과 Hikari connection pool 학습
- 요청 큐, DB 커넥션 대기, backpressure 개념 연결
- 결과물: `docs/load-test-baseline.md`
- 완료 기준: 서버의 최초 한계와 병목 후보를 수치로 제시할 수 있다.

### 8주차: 병목 개선, 장애 실험, 포트폴리오 정리

목표: 측정한 병목 하나 이상을 개선하고 결과를 재검증한다.

#### Day 36

- 로그와 메트릭으로 병목 위치 좁히기
- Spring Actuator 또는 사용 가능한 모니터링 도구 적용
- CS: CPU-bound와 I/O-bound 작업 구분

#### Day 37

- 가장 큰 병목 하나 개선
- 쿼리, 인덱스, 풀 크기, 락 범위 중 근거가 있는 항목만 수정
- 설정값을 무작정 크게 올리지 않기

#### Day 38

- 동일한 k6 시나리오 재실행
- 개선 전후 처리량, p95, p99, 실패율 비교
- 캐시가 필요하다는 근거가 있을 때만 Redis 검토

#### Day 39

- DB 지연, 타임아웃, 서버 재시작 등 제한된 장애 실험
- rollback, 만료 복구, 데이터 정합성 확인
- CS: timeout, retry, 중복 처리, 장애 전파

#### Day 40

- 최종 README와 결과 문서 작성
- 실패했던 접근, 원인, 개선 근거, 남은 한계 작성
- 결과물: `README.md`, `docs/load-test.md`, `docs/troubleshooting.md`
- 완료 기준: 문제 재현부터 개선까지 수치와 코드로 설명할 수 있다.

---

## 6. 핵심 API

| 기능 | Method | URI | 핵심 입력 | 핵심 출력 |
|---|---|---|---|---|
| 회원 등록 | POST | `/members` | 회원 정보 | memberId |
| 공연 등록 | POST | `/events` | 공연 정보 | eventId |
| 공연 회차 생성 | POST | `/events/{eventId}/schedules` | 시작 시간 | scheduleId |
| 좌석 생성 | POST | `/seats` | 좌석명 | seatId |
| 회차 좌석 생성 | POST | `/schedule-seats` | scheduleId | 생성 개수 |
| 회차 좌석 조회 | GET | `/events/{eventId}/schedules/{scheduleId}/schedule-seats` | 없음 | 좌석 상태 목록 |
| 좌석 선점 | POST | `/schedule-seats/{scheduleSeatId}/holds` | memberId | holdId, expiresAt |
| 결제 요청 | POST | `/payments` | holdId, 요청 식별값 | paymentId, reservationId |
| 예약 조회 | GET | `/members/{memberId}/reservations` | 없음 | 예약 목록 |

---

## 7. 반드시 기록할 측정값

| 구간 | 기록할 값 |
|---|---|
| 동시성 테스트 | 요청 수, 성공 수, 실패 수, SeatHold 생성 수 |
| 락 비교 | 락 적용 전후 성공 수, 전체 실행 시간, lock timeout |
| 인덱스 비교 | 실행 SQL, EXPLAIN, 조회 행 수, 실행 시간 |
| 부하 테스트 | VU, RPS, 평균 응답 시간, p95, p99, error rate |
| 서버 한계 | 최초 오류 지점, CPU, 메모리, Tomcat 스레드, DB 커넥션 |
| 개선 결과 | 동일 조건에서 개선 전후 수치 |

---

## 8. 범위에서 제외하거나 뒤로 미룰 것

다음 기술은 이름을 넣기 위해 도입하지 않는다.

- 실제 PG사 결제 연동
- Kafka
- Kubernetes
- 무조건적인 Redis 캐시 또는 분산 락
- 복잡한 대기열 시스템
- 다중 서버 배포

단일 서버와 MySQL에서 문제를 충분히 재현하고 측정한 뒤,
현재 구조로 해결할 수 없는 이유가 확인될 때만 다음 단계로 검토한다.

---

## 9. 진행 피드백 기준

앞으로 현재 진행 상황을 물으면 이 문서를 기준으로 다음 형식으로 답한다.

```text
현재 위치: 몇 주차 / Day 몇
완료 근거: 실제 동작하거나 테스트된 코드
지금 할 일: 한 번에 한 가지
연결 CS: 지금 구현과 연결되는 개념
완료 기준: 어디까지 확인하면 끝인지
보류 항목: 아직 시작하지 않을 기술
```

코드를 작성했다는 것과 기능이 완료됐다는 것은 구분한다.
Controller 호출, DB 결과, 테스트 중 하나로 검증되어야 완료로 표시한다.

---

## 10. 최종 목표 문장

이 프로젝트는 예매 CRUD 구현에 그치지 않고, 같은 좌석에 요청이 몰리는 상황과
서버의 처리 한계를 직접 재현한다. Java 스레드, 트랜잭션, DB 락, 인덱스,
서버 스레드 풀과 커넥션 풀을 실험으로 연결하고, 개선 전후 결과를 수치로 검증한다.
