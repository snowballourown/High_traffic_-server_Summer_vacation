# Gradle 테스트 실패 원인 정리

## 결론

Gradle 자체가 문제였던 것은 아니고, Gradle이 테스트를 실행하는 과정에서 Spring Boot 테스트가 실패한 것이다.

실패 원인은 `spring-boot-starter-data-jpa`와 MySQL 드라이버 의존성이 추가되어 있는데, DB 접속 설정이 없었기 때문이다.

## 실패 흐름

```text
Gradle test 실행
-> @SpringBootTest 테스트 실행
-> Spring Boot 애플리케이션 전체 실행 시도
-> JPA 의존성 때문에 DataSource 자동 설정 시도
-> spring.datasource.url 설정이 없음
-> 어떤 DB에 연결해야 하는지 몰라서 실패
-> Gradle 테스트 실패로 표시됨
```

## 관련 의존성

`build.gradle`에 아래 의존성이 있었다.

```gradle
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
runtimeOnly 'com.mysql:mysql-connector-j'
```

이 의존성이 있으면 Spring Boot는 기본적으로 DB를 사용하는 프로젝트라고 판단하고, 테스트 실행 중에도 DB 설정을 자동으로 만들려고 한다.

## 실제 에러 핵심 문구

```text
Failed to configure a DataSource: 'url' attribute is not specified
Reason: Failed to determine a suitable driver class
```

즉, DB를 쓰려고 했지만 `spring.datasource.url` 같은 DB 접속 정보가 없어서 실패한 것이다.

## 왜 IntelliJ에서는 Gradle 오류처럼 보였나

IntelliJ에서 테스트를 실행하면 내부적으로 Gradle이 테스트를 돌린다.

그래서 화면에는 Gradle 테스트 실패처럼 보이지만, 실제 원인은 Gradle 설정 문제가 아니라 Spring Boot 애플리케이션이 DB 설정 없이 시작되면서 실패한 것이다.

## 해결한 방법

테스트 환경에서만 사용할 수 있는 H2 메모리 DB를 추가했다.

`build.gradle`에 테스트용 H2 의존성을 추가했다.

```gradle
testRuntimeOnly 'com.h2database:h2'
```

그리고 `src/test/resources/application.properties` 파일을 만들어 테스트 실행 시 H2 DB를 사용하도록 설정했다.

```properties
spring.datasource.url=jdbc:h2:mem:bigtraffic-test;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create-drop
```

## 정리

이번 오류는 "DB 의존성을 추가했는데 DB 설정이 없어서" 발생했다.

테스트에서는 실제 MySQL을 켜지 않고도 실행할 수 있도록 H2 메모리 DB를 사용하게 해서 해결했다.
