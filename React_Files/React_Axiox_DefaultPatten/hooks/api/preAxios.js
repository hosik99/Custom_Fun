import axios from "axios";
// import Cookies from 'js-cookie';

export const connectSpring = axios.create({     //axios 생성
    baseURL: 'http://localhost:8080',
    headers: { 'Content-Type': 'application/json', },
    withCredentials: true, // 쿠키 포함
});

// 요청 인터셉터 추가
// connectSpring.interceptors.request.use(
//     (config) => {
//         const token = Cookies.get('auth-token');
//         if (token) {
//             config.headers['Authorization'] = `Bearer ${token}`; // 헤더에 토큰 추가
//         }
//         return config;
//     },
//     (error) => {
//         return Promise.reject(error);
//     }
// );