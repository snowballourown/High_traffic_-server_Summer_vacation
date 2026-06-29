#  MemberRepository를 interface로 한이유 

repository를  service 계층이 저장방식을 의존하지 않게하기위해서 
이렇게 설계한이유는 repository의 방식을 쉽게 바뀌게 하기위해서임 


### JPQL

- JPA에서 쓰는 객체 지향 쿼리 언어  

Java 코드에서 DB에 조회 요청을 할 때 쓰는데,   
SQL처럼 테이블을 대상으로 쓰는 게 아니라 엔티티 객체를 대상으로 쿼리

- :name은 JPQL에서 쓰는 파라미터 자리표시자