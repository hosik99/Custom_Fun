import React, { createContext, useContext, useCallback } from 'react';
import { SnackbarProvider, useSnackbar } from 'notistack';

/*  # MultiSnackBar.js
    어디서든 Snackbar를 호출할 수 있도록 Context를 제공하는 컴포넌트
*/

//Snackbar 호출 함수를 담는 Context.
const SnackContext = createContext(() => {});

//이 Context를 어디서든 꺼내 쓸 수 있는 커스텀 Hook.
export function useSnack() { return useContext(SnackContext); }

function InnerSnackProvider({ children }) {
  const { enqueueSnackbar } = useSnackbar();
  const showSnack = useCallback(
    (variant, msg) => {
      enqueueSnackbar(msg, { variant });
    },
    [enqueueSnackbar]
  );

  return (
    <SnackContext.Provider value={showSnack}>
      {children}
    </SnackContext.Provider>
  );
}

// 4) 최상위 Provider 컴포넌트
export default function MultiSnackBar({ children }) {
  return (
    <SnackbarProvider maxSnack={3}>
      <InnerSnackProvider>
        {children}
      </InnerSnackProvider>
    </SnackbarProvider>
  );
}
