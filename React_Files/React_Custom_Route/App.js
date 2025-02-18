import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import React, { useState } from "react";

import Main from './pages/Main';
import PostDesPage from './pages/post/PostDesPage';
import PostMngPage from './pages/post/PostMngPage';
import PostLogPage from './pages/log/PostLogPage';
import PostCreatePage from './pages/post/PostCreatePage';
import MainSidebar from './components/MainSidebar';

function App() {

  //Public URL
  const [publicRoutes, setPublicRoutes] = useState([
    { path: "/", element: <Main/> }, /* 메인 페이지 */
    { path: "/des/post", element: <PostDesPage/> }, /* 게시물 통계 페이지 */
    { path: "/mng/post", element: <PostMngPage/> }, /* 게시물 관리 페이지 */
    { path: "/mng/post/log", element: <PostLogPage/> }, /* Post Log  관리 페이지 */
    { path: "/mng/post/cre", element: <PostCreatePage/> }, /* Post 추가 페이지 */
    { path: "/*", element: <div>존재하지 않는 페이지</div> },/* 존재하지 않는 페이지 */
  ]);

  //Required for certification
  const [privateRoutes, setPrivateRoutes] = useState([
    { path: "/logout", element: <LogoutPage /> },
  ]);

  return (
    <BrowserRouter>
      <div style={{ display: "flex", height: "100vh" }}>
        <MainSidebar />
        <div style={{ flex: 1, overflow: "auto", padding: "20px" }}>
          
          <Routes>
            {publicRoutes.map((route, index) => (
              <Route key={index} path={route.path} element={route.element} />
            ))}

            {privateRoutes.map((route, index) => (
              <Route key={index} path={route.path} element={ <PrivateRoute> {route.element} </PrivateRoute> } />
            ))}
          </Routes>
          
        </div>
      </div>
    </BrowserRouter>
  );
}

export default App;
