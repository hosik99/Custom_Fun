package com.icetea.monstu_back.handler.log;

import com.icetea.monstu_back.mongo.pageable.CustomPageableDTO;
import com.icetea.monstu_back.manager.log.PostLogManager;
import com.icetea.monstu_back.model.log.PostLog;
import com.icetea.monstu_back.repository.PostsRepository;
import com.icetea.monstu_back.repository.custom.PostLogCustomRepository;
import com.icetea.monstu_back.repository.log.PostLogRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class PostsLogHandler {

    private final PostLogCustomRepository postLogCustomRps;
    private final PostLogRepository postLogRps;
    private final PostsRepository postsRps;
    private final PostLogManager pageableManager;

    public PostsLogHandler(PostLogCustomRepository postLogCustomRps, PostLogManager pageableManager,
                        PostsRepository postsRps, PostLogRepository postLogRps) {
        this.postLogCustomRps = postLogCustomRps;
        this.postLogRps = postLogRps;
        this.postsRps = postsRps;
        this.pageableManager = pageableManager;
    }

    public Mono<ServerResponse> getPostLog(ServerRequest request) {
        CustomPageableDTO pageableDTO = pageableManager.extract(request);   // page,size 추출

        Flux<PostLog> postLogsFlux = getPostLogs( pageableDTO ) ;

        return postLogsFlux.hasElements()
                .flatMap(hasElements -> hasElements
                        ? ServerResponse.ok().body(postLogsFlux, PostLog.class)
                        : ServerResponse.noContent().build());  // return HTTP Status 204
    }

    // Get PostLog
    private Flux<PostLog> getPostLogs( CustomPageableDTO dto ) {
        Boolean filterBoo = dto.getFilterOption() != null && dto.getFilterValue() != null;
        Boolean dateFilterBoo = dto.getDateOption() != null && dto.getDateStart() != null && dto.getDateEnd() != null;

        if( filterBoo && dateFilterBoo ){
            return postLogCustomRps.findWithOptions(dto);  // filtering, Date Filtering
        }else if( !filterBoo && !dateFilterBoo ){
            return postLogCustomRps.findWithPagination(dto);  //just find
        } else if ( !filterBoo) {
            return postLogCustomRps.findByDateWithPagination(dto);    //Date Filtering
        }else {
            return postLogCustomRps.findByWithPagination(dto);    // filtering
        }
    }

}
