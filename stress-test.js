import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  stages: [
    { duration: '30s', target: 50 },
    { duration: '30s', target: 100 },
    { duration: '1m', target: 200 },
    { duration: '1m', target: 400 },
    { duration: '10s', target: 0 },
  ],
};

export default function () {
  const res = http.get('http://host.docker.internal:30080/api/health');

  sleep(0.1);
}
