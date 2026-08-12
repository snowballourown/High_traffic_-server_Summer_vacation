package challenge_summer.bigtraffic.dto;

public record TestDataResponse(
        Long firstScheduleId,
        int scheduleCount,
        int seatsPerSchedule,
        long scheduleSeatCount
) {
}