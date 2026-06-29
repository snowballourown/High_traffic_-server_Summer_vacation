# 객체 생성 ID 번호를 주는 전략 

- Identity 전략 - 특징   

  - 기본키 생성을  데이터 베이스한테  넘겨줘버림
  - 이렇게 되면 JPA은  영속성 콘텍스트에 넣을 때 1차캐쉬에 Id랑 객체를
  넣어야하는대 
  - Id가 없기에  Commit을 하지도않았는데 DB에 먼저 Insert문을 날려서 
    Id값을 반환하여 사용함 
  -   => 그럼 DB에서 들어갔다가 나온거니 select라는 의문???? 이 생길수있음 
     JDBC에 내부적으로 insert넣고 바로 ID값을 바로 반환받는게 코딩되어있기에 insert시점에 바로 알수있음 


----
- sequence 전략   특징 

@SequenceGenerator(
name = "member_seq_generator",
sequenceName = "member_seq",
allocationSize = 50
)    
1. Spring 서버가 DB sequence에 요청
   "ID 구간 하나 줘"

2. DB가 시퀀스 값을 줌

3. Spring/Hibernate가 그 값을 기준으로
   1~50 같은 ID 범위를 메모리에서 사용

4. 50개 다 쓰면 다시 DB에 요청

5. 다음 51~100 범위를 사용

---- 이렇게하면 여러번 DB서버에 쿼리를 날릴 필요가없음 

---주의점 하나:  
서버 실행   
→ 1~50 확보
→ 1~10까지 사용
→ 서버 꺼짐
→ 11~50은 안 쓰이고 버려질 수 있음  
→ 다시 켜짐
→ 다음 구간 51~100 확보
→ 51부터 사용