# 자리 선점 자동 만료 어노테이션 정리

## 어노테이션이란?

어노테이션은 클래스나 메서드 위에 붙여서 Spring, JPA, Lombok에
특정 기능이나 설정을 알려주는 표시다.

```java
@Transactional
public void createHold() {
}
```

위 코드에서 `@Transactional`이 어노테이션이다.

## @Scheduled

메서드를 일정한 시간 간격으로 반복 실행하도록 설정한다.

```java
@Scheduled(fixedDelay = 1000)
public void releaseExpiredHolds() {
}
```

`1000`의 단위는 밀리초이므로 1초를 의미한다.

동작 순서:

```text
메서드 실행
→ 메서드 실행 완료
→ 1초 기다림
→ 다시 실행
```

이 코드는 10초 뒤에 한 번 실행되는 것이 아니다.
1초마다 만료된 자리 선점이 있는지 계속 확인하기 위한 설정이다.

## @EnableScheduling

Spring 프로젝트에서 스케줄 기능을 활성화한다.

```java
@EnableScheduling
@SpringBootApplication
public class BigTrafficApplication {
}
```

`@Scheduled`를 메서드에 붙여도 `@EnableScheduling`이 없으면 반복 실행되지 않는다.

두 어노테이션의 관계:

```text
@EnableScheduling = 프로젝트의 스케줄 기능 켜기
@Scheduled = 특정 메서드를 반복 실행하도록 등록하기
```

## @Transactional

메서드 안에서 실행되는 여러 DB 작업을 하나의 작업 단위로 묶는다.

```java
@Transactional
public void releaseExpiredHolds() {
    scheduleSeat.setStatus(Status.AVAILABLE);
    seatHoldRepository.delete(seatHold);
}
```

좌석 상태 변경과 선점 삭제가 모두 성공하면 DB에 반영된다.
중간에 오류가 발생하면 앞에서 실행한 변경도 취소된다.

또한 JPA로 조회한 엔티티의 값을 트랜잭션 안에서 변경하면 별도의 `save()`를
호출하지 않아도 트랜잭션 종료 시 DB에 반영된다.

```java
scheduleSeat.setStatus(Status.AVAILABLE);
```

이 기능을 JPA의 변경 감지라고 한다.

## @SpringBootApplication

Spring Boot 애플리케이션의 시작 클래스에 붙인다.

```java
@SpringBootApplication
public class BigTrafficApplication {
    public static void main(String[] args) {
        SpringApplication.run(BigTrafficApplication.class, args);
    }
}
```

Spring Boot가 애플리케이션 설정을 적용하고 프로젝트 내부의 Spring 클래스를
찾아서 관리할 수 있게 한다.

일반적으로 프로젝트 생성 시 메인 클래스에 이미 작성되어 있다.

## @Entity

해당 클래스가 DB 테이블과 연결되는 JPA 엔티티임을 나타낸다.

```java
@Entity
public class Seat_hold {
}
```

`Seat_hold` 객체를 JPA로 저장하면 연결된 `seat_hold` 테이블에 데이터가 저장된다.

## @Getter

Lombok이 필드의 Getter 메서드를 자동으로 만들어 준다.

```java
@Getter
@Entity
public class Seat_hold {
    private ScheduleSeat scheduleSeat;
}
```

위 코드는 다음과 같은 Getter를 자동으로 만든다.

```java
public ScheduleSeat getScheduleSeat() {
    return scheduleSeat;
}
```

따라서 외부에서 다음처럼 값을 조회할 수 있다.

```java
seatHold.getScheduleSeat();
```

## @NoArgsConstructor

Lombok이 파라미터가 없는 기본 생성자를 자동으로 만들어 준다.

```java
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat_hold {
}
```

실제로는 다음과 같은 생성자가 만들어진다.

```java
protected Seat_hold() {
}
```

JPA는 DB에서 조회한 데이터를 객체로 만들 때 기본 생성자가 필요하다.
접근 범위를 `PROTECTED`로 설정하면 JPA는 사용할 수 있지만 외부에서 의미 없이
`new Seat_hold()`를 호출하는 것은 제한할 수 있다.

## 전체 동작 관계

```text
@SpringBootApplication
→ Spring Boot 애플리케이션을 실행한다.

@EnableScheduling
→ 프로젝트의 스케줄 기능을 활성화한다.

@Scheduled
→ 만료된 자리 선점을 확인하는 메서드를 반복 실행한다.

@Transactional
→ 좌석 상태 변경과 만료된 선점 처리를 하나의 DB 작업으로 묶는다.

@Entity
→ Seat_hold 클래스를 DB 테이블과 연결한다.

@Getter
→ Seat_hold의 값을 읽는 메서드를 자동으로 만든다.

@NoArgsConstructor
→ JPA가 사용할 기본 생성자를 자동으로 만든다.
```
