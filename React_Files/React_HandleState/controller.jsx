import { errorStatus, responseStatus } from "../../util/handleStatus";
import { connectSpring } from "../preAxios";

const baseURL = '/post/log';

// Get PostLog Data List
export const getPostLogs = async( pageable ) => {
    try {
        const url  = baseURL+'/all';
        
        const response = await axios.create({
            baseURL: 'http://localhost:8080',
            headers: { 'Content-Type': 'application/json', },
            withCredentials: true,
        }).get(url);

        return responseStatus(response,response.data);  
    } catch (error) {
        return errorStatus(error);
    }
}