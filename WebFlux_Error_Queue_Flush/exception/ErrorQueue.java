package com.icetea.monstu_back.exception;

import com.icetea.monstu_back.model.log.ErrorLog;
import com.icetea.monstu_back.repository.log.ErrorLogRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class ErrorQueue {

    private final ErrorLogRepository errorLogRepository;

    //ConcurrentLinkedQueue -> 비동기 환경에서 안전하게 사용할 수 있는 큐(Queue) 구현체, 동시성 보장
    private final Queue<ErrorLog> errorQueue = new ConcurrentLinkedQueue<>();
    private final int batchSize = 10; // 배치 크기

    public ErrorQueue(ErrorLogRepository errorLogRepository) {
        this.errorLogRepository = errorLogRepository;
    }

    //Queue에 Error 추가, Queue size 초과 시 DB에 저장 함수 호출
    public void addError(ErrorLog error) {
        errorQueue.add(error);
        if (errorQueue.size() >= batchSize) {
            flushToDatabase();
        }
    }

    //DB에 Queue에 들어있는 Error 요소들 저장
    public void flushToDatabase() {
        if (!errorQueue.isEmpty()) {
            List<ErrorLog> batch = new ArrayList<>();
            while (!errorQueue.isEmpty() && batch.size() < batchSize) {
                batch.add(errorQueue.poll());
            }
            saveToDatabase(batch).subscribe();
        }
    }

    private Mono<Void> saveToDatabase(List<ErrorLog> errors) {
        return errorLogRepository.saveAll(errors)
                .then() // 결과를 Mono<Void>로 변환
                .doOnSuccess(unused -> System.out.println("DB 저장 완료"))
                .doOnError(error -> System.err.println("DB 저장 실패: " + error.getMessage()));
    }


}

