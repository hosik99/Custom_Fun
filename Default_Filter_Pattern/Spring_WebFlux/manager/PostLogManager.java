package com.icetea.monstu_back.manager.log;

import com.icetea.monstu_back.mongo.pageable.CustomPageableDTO;
import com.icetea.monstu_back.mongo.pageable.PageableManager;
import com.icetea.monstu_back.model.log.PostLog;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.time.LocalDateTime;

@Component
public class PostLogManager implements PageableManager<PostLog> {

    public PostLogManager() {}

    @Override
    public CustomPageableDTO extract(ServerRequest request) {
        return extractDefault(request);
    }

    @Override
    public Sort createSort(CustomPageableDTO dto) {
        return createSortDefault(dto);
    }

    @Override
    public Class<?> convertFilterValue(String filterOption) {
        return switch (filterOption) {
            case "postId", "authorId","id","viewCount" -> Long.class;
            case "createdAt","lastView" -> LocalDateTime.class;
            default -> String.class;
        };
    }

}
