import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 100 },
        { duration: '30s', target: 300 },
        { duration: '30s', target: 500 },
        { duration: '1m', target: 500 },
        { duration: '30s', target: 0 },
    ],
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

  if (response.status !== 200) {
      console.error(
          `status=${response.status}, ` +
          `error_code=${response.error_code}, ` +
          `error=${response.error}`
      );
  }

  sleep(1);

}
