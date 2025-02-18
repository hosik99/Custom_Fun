import { buildPageableDTO, CustomPageableDTO } from "../../types/CustomPageableDTO";
import { PostLogDTO } from "../../types/PostLogDTO";
import { ReResponse } from "../../types/ReResponse"
import { errorStatus, responseStatus } from "../../util/handleStatus";
import { connectSpring } from "../preAxios";

const baseURL = '/post/log';

// Get PostLog Data List
export const getPostLogs = async( pageable:CustomPageableDTO ) : Promise<ReResponse> => {
    try {
        const url  = baseURL+buildPageableDTO(pageable);
        const response = await connectSpring.get< {result: PostLogDTO[]} >(url);
        return responseStatus(response,response.data);  
    } catch (error) {
        console.log("Server Errored")
        return errorStatus(error);
    }
}