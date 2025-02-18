package com.icetea.monstu_back.mongo.pageable;

import reactor.core.publisher.Flux;

public interface PageableCustomRepository<T> {

    Flux<T> findWithPagination( CustomPageableDTO pageableDTO );

    // filter를 추가한 Pageable 검색
    Flux<T> findByWithPagination( CustomPageableDTO pageableDTO );

    // Date Filtering
    Flux<T> findByDateWithPagination( CustomPageableDTO dto );

    // filtering & Date Filtering
    Flux<T> findWithOptions( CustomPageableDTO dto );
}
