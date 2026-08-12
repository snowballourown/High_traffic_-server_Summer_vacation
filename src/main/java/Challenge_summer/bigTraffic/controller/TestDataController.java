package challenge_summer.bigtraffic.controller;

import challenge_summer.bigtraffic.dto.TestDataResponse;
import challenge_summer.bigtraffic.service.TestDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("local")
@RestController
@RequestMapping("/dev/test-data")
public class TestDataController {
    @Autowired
    private final TestDataService testDataService;

    public TestDataController(TestDataService testDataService) {
        this.testDataService = testDataService;
    }


    @PostMapping
    public ResponseEntity<TestDataResponse> createTestData() {
        TestDataResponse response = testDataService.createScheduleSeats10000();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
