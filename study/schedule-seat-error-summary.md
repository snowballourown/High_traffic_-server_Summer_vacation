# ScheduleSeat 생성 오류 정리

## 발생한 오류

`ScheduleSeat`를 생성하면서 `status`에 `AVAILABLE`을 저장하려고 할 때 다음 오류가 발생했다.

```text
Data truncated for column 'status' at row 1
```

## 원인

처음 `Status` enum은 다음 값으로 작성되어 있었다.

```java
public enum Status {
    HOLD,
    SUCCESS,
    FAIL
}
```

이 상태에서 DB의 `schedule_seat.status` 컬럼이 만들어진 다음, Java enum 값을 다음과 같이 변경했다.

```java
public enum Status {
    AVAILABLE,
    HELD,
    CONFIRMED
}
```

Java 코드의 enum 값은 바뀌었지만 DB의 `status` 컬럼에는 기존 값인
`HOLD`, `SUCCESS`, `FAIL`이 남아 있었다.

`ScheduleSeat`에서는 enum을 문자열로 저장하고 있다.

```java
@Enumerated(EnumType.STRING)
private Status status;
```

따라서 Java가 새로운 값인 `AVAILABLE`을 DB에 저장하려 했지만,
DB에는 `AVAILABLE`이 허용되어 있지 않아 오류가 발생했다.

## 해결

DB의 `status` 컬럼도 현재 Java enum 값과 동일하게 변경한다.

MySQL enum 타입을 유지하는 경우:

```sql
ALTER TABLE schedule_seat
MODIFY COLUMN status ENUM('AVAILABLE', 'HELD', 'CONFIRMED');
```

개발 중 enum 값이 계속 바뀔 수 있다면 문자열 타입으로 변경할 수도 있다.

```sql
ALTER TABLE schedule_seat
MODIFY COLUMN status VARCHAR(20);
```

## 결론

이번 오류의 원인은 하나다.

```text
Java의 Status enum 값은 변경했지만 DB의 status 컬럼 값은 변경하지 않았다.
```

그래서 DB가 새로운 값인 `AVAILABLE`을 받을 수 없었고,
Java enum과 DB의 `status` 정의를 동일하게 맞춰 해결했다.
