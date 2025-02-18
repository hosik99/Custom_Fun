import React, { useEffect, useState } from "react";
import { CustomPageableDTO, decreasePage, increasePage, IncreasePage } from "../../hooks/types/CustomPageableDTO";
import { PostLogDTO, PostLogFilterArray } from "../../hooks/types/PostLogDTO";
import PostLogList from "../../components/list/PostLogList";
import { delPostLog, delPostLogs, getPostLogs } from "../../hooks/api/controller/postLogHandler";
import MsgPopup from "../../components/popupBox/MsgPopup";
import { handleInputChange } from "../../hooks/util/changeEventHandler";
import FilterBox from "../../components/filterBox/FilterBox";

const PostLogPage = () => {
    const [postLogList,setPostLogList] = useState<PostLogDTO[]>([]);

    const [PostLogPageableDTO, setPLPageableDTO] = useState<CustomPageableDTO>({
        page: 0,
        size: 5,
        sortValue: "id",
        sortDirection: "DESC",
        filterOption: "",
        filterValue: "",
        dateOption: "",
        dateStart: "",
        dateEnd: "",
    });

    const postLogHandler = handleInputChange(setPLPageableDTO);

    const refreshPostLogs = async () => {
        const result = await getPostLogs(PostLogPageableDTO);
        if(result.success){ 
          setPostLogList(result.data); 
        }
        else{
          setPostLogList([]);
          alert(result.message);
        }
    }

    useEffect(() => {
        refreshPostLogs();
    },[PostLogPageableDTO]);

  return (
    <div>
        <FilterBox pageableDTO={PostLogPageableDTO} dto={PostLogFilterArray} handleChange={postLogHandler}/>

        <button onClick={refreshPostLogs}>Search</button>
        <PostLogList list={postLogList} setSelectedList={setSelectedList} selectedList={selectedList}/>  
        <button onClick={()=>decreasePage(PostLogPageableDTO, setPLPageableDTO)}>-</button>
        <button onClick={()=>increasePage(PostLogPageableDTO, setPLPageableDTO)}>+</button>
    </div>
  );
};

export default PostLogPage;
