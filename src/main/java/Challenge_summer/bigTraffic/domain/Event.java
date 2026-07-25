package Challenge_summer.bigTraffic.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "event",
        uniqueConstraints = {
                //이걸 쓰는이유   필드명위에다가 안쓰는 이유는 로그보면
                // 이름을 알수없게 변형되어 어떤게 제약이 됐다고 로그가뜸 이렇게 씀으로써 알아볼수있게 로그를 알수있게 함
                @UniqueConstraint(name = "uk_event_name", columnNames = "eventName") //name은 제약조건이름
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

    // 공연
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long Id;

    @Setter
    @Column(name = "eventName", nullable = false)  //겹치는 값이 못들어오게 막음
    private String name;




    public Event(String name) {
        this.name = name;
    }
}
