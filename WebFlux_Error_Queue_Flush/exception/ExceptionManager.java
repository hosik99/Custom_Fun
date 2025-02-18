package com.icetea.monstu_back.exception;

import com.icetea.monstu_back.model.log.ErrorLog;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.springframework.http.HttpStatus.*;

@Component
public class ExceptionManager {

    private final ErrorQueue errorQueue;

    public ExceptionManager(ErrorQueue errorQueue) {
        this.errorQueue = errorQueue;
    }

    //Error객체 ErrorQueue에 추가
    public void addQueue(ServerWebExchange exchange, ResponseStatusException ex) {
        //객체 값 셋팅
        ErrorLog errorLog = ErrorLog.builder()
                .code( ex.getStatusCode().value() )
    //                .userId(getUserIdFromExchange(exchange))
                .message( ex.getReason() )
                .endpoint( exchange.getRequest().getURI().getPath() )
                .httpMethod( exchange.getRequest().getMethod().toString() )
                .hostName( "MonStu_Back_Server" )
                .build();
        errorQueue.addError(errorLog);  //Queue에 추가
    }

    // Custom Method
    
    public void addQueue(ServerWebExchange exchange, int exCode, String message) {
        //객체 값 셋팅
        ErrorLog errorLog = ErrorLog.builder()
                .code( exCode )
                //                .userId(getUserIdFromExchange(exchange))
                .message( message )
                .endpoint( exchange.getRequest().getURI().getPath() )
                .httpMethod( exchange.getRequest().getMethod().toString() )
                .hostName( "MonStu_Back_Server" )
                .build();
        errorQueue.addError(errorLog);  //Queue에 추가
    }

    //Error Response 셋팅
    public Mono<Void> writeErrorResponse(ServerWebExchange exchange, HttpStatusCode status, String message){
        String finalMessage = message != null ? message : getStateErrorMessage(status) ;
        exchange.getResponse().setStatusCode(status);   //exchange.getResponse() -> 응답의 상태 코드와 바디를 설정
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap( generateErrorResponseBody( status.value(), finalMessage) ))
        );
    }

    // JSON 형태로 응답 본문 생성
    private byte[] generateErrorResponseBody(int errorCode, String errorMessage) {
        //객체나 다른 복잡한 객체를 JSON 형태로 변환하려면 ObjectMapper를 사용
        String errorResponse = String.format("{\"error\":\"%d\",\"message\":\"%s\"}", errorCode, errorMessage);
        return errorResponse.getBytes();
    }

    private String getStateErrorMessage(HttpStatusCode status) {
        return switch (status) {
            case BAD_REQUEST -> "Bad Request";
            case UNAUTHORIZED -> "Unauthorized";
            case FORBIDDEN -> "Forbidden. You do not have access";
            case NOT_FOUND -> "Not Found";
            case CONFLICT -> "Data is Already Exists";
            case TOO_MANY_REQUESTS -> "Too Many Requests, Please try later";
            case INTERNAL_SERVER_ERROR -> "Internal Server Error. Please try again later";
            case GATEWAY_TIMEOUT -> "Gateway Timeout";
            default -> "An unexpected error occurred";
        };
    }
}
