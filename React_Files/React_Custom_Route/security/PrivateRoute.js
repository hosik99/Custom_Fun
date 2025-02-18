import React from 'react';
import { Navigate } from 'react-router-dom';
import {useAuth} from "../security/useAuth"
/* ONLY AUTHENTICATED USERS */
/*
    { children } -> 객체 디스트럭처링(destructuring)을 사용
     props.children를 destructuring사용하면 { children }로 사용

     replace={true}: 현재의 히스토리 항목을 새 항목으로 교체
     사용자가 로그인 후 이전 페이지로 돌아가려고 할 때, 로그인 페이지로의 리다이렉트가 보이지 않게 됩니다.
*/
const PrivateRoute = ({ children }) => {

    const isAuthenticated = useAuth();

    console.log('isAuthenticated : '+isAuthenticated);
    
    if (!isAuthenticated) {
        return <Navigate to="/login" replace />;    
    }
    return children;
};

export default PrivateRoute;
