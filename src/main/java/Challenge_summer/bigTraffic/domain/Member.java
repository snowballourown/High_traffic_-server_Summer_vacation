package Challenge_summer.bigTraffic.domain;


import jakarta.persistence.*;

@Entity
public class Member {  //index까지 적는편이라함

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long Id;

    @Column
    private String name;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
