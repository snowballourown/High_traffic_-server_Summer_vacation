#  6주차 완료

1기존 조회
   schedule_id 외래 키 단일 인덱스로 100행 조회
   → status 필터
   → AVAILABLE 90행 반환

2. 복합 인덱스
   (schedule_id, status)
   → 조건에 맞는 AVAILABLE 90행 직접 조회
   → 별도 Filter 제거

3. 왼쪽 우선 규칙
   schedule_id만 조회: 사용 가능
   schedule_id + status: 사용 가능
   status만 조회: Full Scan

4. 실제 API 적용
   GET /schedules/{scheduleId}/seats
   → 전체 10,000개가 아니라 해당 회차의 AVAILABLE 좌석만 조회

5. 페이지네이션 미적용 이유
   회차당 좌석이 100개이고 좌석 배치도에서 전체 상태가 필요하므로,
   페이지네이션보다 scheduleId로 조회 범위를 제한하는 것이 적절하다고 판단