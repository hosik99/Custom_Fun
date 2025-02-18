
const PostLogPage = () => {
    const [msg, setMsg] = useState<string>('');
    const [msgId, setMsgId] = useState<number>(0);

    //Delete PostLogs use selectedList(ID List)
    const delPostLogHub = async () => {
      let result;
      if(selectedList.length === 0){
        setMsg('삭제 항목을 선택 해주세요'); 
        setMsgId((prevMsgId) => prevMsgId + 1);
        return;
      }
    }

  return (
    <div>

        <button onClick={refreshPostLogs}>Search</button>
        <button onClick={delPostLogHub}>Delete</button>
        <PostLogList list={postLogList} setSelectedList={setSelectedList} selectedList={selectedList}/>  

        <MsgPopup msg={msg} id={msgId}/>
    </div>
  );
};

export default PostLogPage;
