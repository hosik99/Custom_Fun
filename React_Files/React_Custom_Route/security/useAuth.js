import { useState, useEffect } from 'react';
import Cookies from 'js-cookie';

/* 인증된 사용자인지 확인하는  */
export const useAuth = () => {
    const [isAuthenticated, setIsAuthenticated] = useState(()=>{
        return !!Cookies.get('auth-token');
    });

    /* 여러 창으로 로그인 사용 시 동시 적용되게 */
    useEffect(() => {
        //authToken의 변화를 감지하여 isAuthenticated 상태를 업데이트
        const handleAuthChange = () => {
            setIsAuthenticated(!!Cookies.get('auth-token'));
        };
        const token = Cookies.get('auth-token');
        // window.addEventListener('storage', handleAuthChange);   //storage 이벤트 리스너를 등록, storage 이벤트는 localStorage나 sessionStorage에서 데이터가 변경될 때 발생
        
        //쿠키는 localStorage와 달리 storage 이벤트로 감지할 수 없으므로, 일정 간격으로 쿠키를 체크하여 인증 상태를 업데이트합니다
        const interval = setInterval(handleAuthChange,1000); // 5초마다 authToken 체크

        return () => {
            // window.removeEventListener('storage', handleAuthChange);
            clearInterval(interval); // 컴포넌트 언마운트 시 interval 정리
        };
    }, []);

    return isAuthenticated;
};