
//Handle Response
export function responseStatus(response,data) {
    //Inital State, Message
	const status = response?.status || 999;
	const message = response?.data?.message  || getDefaultMessage(status);
	
    //Fianl return type
	const result = {
        status,
        success: true,
        message,
    };
    
    return addData(result,data);
};

//Handle Error Response 
export function errorStatus(error) {

    const status = error?.response?.status || 999;
    const message = error?.response?.data?.error.message || getDefaultMessage(status);

    return {
        status,
        success: false,
        message,
    };
}

function getDefaultMessage(status) {

    const messages = {
        200: 'Succesful',
        201: 'Succesfully CREATED',
        202: 'Succesfully ACCEPTED',
        204: 'No Data',
        209: 'Succesful',

        400: 'Bad Request',
        401: 'Unauthorized',
        403: 'Forbidden. You do not have access',
        404: 'Not Found',
        409: 'Data is Already Exists',
        429: 'Too Many Requests, Please try later',

        500: 'Internal Server Error. Please try again later',
        504: 'Gateway Timeout',

        999: 'An unexpected error occurred.',
    };

    return messages[status] || `An unexpected error occurred.`;
}

//Add to result if data exists
function addData(result, data)  {
    return data ? { ...result, data } : result;
}