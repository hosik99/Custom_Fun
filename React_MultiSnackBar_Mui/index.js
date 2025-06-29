
/*  # index.js
    index.js 수정 -> App을 MultiSnackBar로 감싸서 어느 컴포넌트에서든 표시할 수 있게 설정
*/
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <MultiSnackBar>
      <App />
    </MultiSnackBar>
  </React.StrictMode>
);