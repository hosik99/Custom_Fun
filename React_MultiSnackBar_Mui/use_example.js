
/* 사용 예시 */
const showSnack = useSnack();
showSnack('success', '완료되었습니다.');
showSnack('error',   '문제가 발생했습니다.');

/* 커스텀 Response과 함께 사용한 예시 */
const handleDelete = useCallback(
    async ( ) => {
      const {success, message} = await deleteMembers( selected );
      if(success) {handleSearchBtn()}
      showSnack( (success) ? "info" : "error", message);
  },[selected]
);