import http from 'k6/http';  // Smoke Test는 본격적인 테스트 전에 핵심 기능이 최소한으로 동작하는지 빠르게 확인하는 테스트
import { check } from 'k6';

export const options = {
    vus: 1, // 가상 사용자 -> 동시에 행동하는 사용자수
    iterations: 1, // 총 한번 실행
};

export default function () { // k6가 반복실행할 핵심함수
    const response = http.get(
        'http://localhost:8080/schedules/1/seats'
    );

    check(response, {
        '응답 상태는 200이다': (res) => res.status === 200,
        'AVAILABLE 좌석은 90개다': (res) => res.json().length === 90,
    });
}