import { useEffect, useState } from "react";
import { handleInputChange } from "../../hooks/util/changeEventHandler";
import FilterBox from "../../components/filterBox/FilterBox";

const Page = () => {

    const [target, setTarget] = useState({
        page: 0,
        size: 5,
        sortValue: 'id',
        sortDirection: 'DESC',
        filterOption: '',
        filterValue: '',
        dateOption: '',
        dateStart: '',
        dateEnd: ''
    });

    return(
        //target -> 초기화 할 객체, preViewList -> 미리보기 목록, handleChange -> Event 관리 메소드
        <div>
            <FilterBox target={target} preViewList={PreViewList} handleChange={()=>handleInputChange(setPostDTOPageDTO)}/>
        </div>
    );
}

export default Page;