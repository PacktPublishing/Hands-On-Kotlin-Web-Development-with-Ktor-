import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    stages: [
        { duration: '10s', target: 20 },
        { duration: '20s', target: 10 },
        { duration: '30s', target: 0 },
    ],
};

export default function() {
    let res = http.get('http://0.0.0.0:8080/');
    check(res, { 'status was 200': r => r.status == 200 });
    sleep(1);
}