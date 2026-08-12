import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 5, // 가상유저 5개
    duration: '30s', // 10초동안 반복
};

export default function () {
    const response = http.get(
        'http://localhost:8080/schedules/1/seats'
    );

    check(response, {
        '응답 상태는 200이다': (res) => res.status === 200,
        'AVAILABLE 좌석은 90개다': (res) =>
            res.status === 200 && res.json().length === 90,
    });

    sleep(1);
}
