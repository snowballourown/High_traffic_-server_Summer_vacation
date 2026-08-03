package challenge_summer.bigtraffic;

import challenge_summer.bigtraffic.domain.Reservation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BigTrafficApplication {

	public static void main(String[] args) {
		SpringApplication.run(BigTrafficApplication.class, args);
	}

}
