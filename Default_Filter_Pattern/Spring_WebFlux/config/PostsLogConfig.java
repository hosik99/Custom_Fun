package com.icetea.monstu_back.config.log;

import com.icetea.monstu_back.handler.log.PostsLogHandler;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class PostsLogConfig {
    @Bean
    public RouterFunction<ServerResponse> PostsLogRouter(PostsLogHandler handler) {
        return  RouterFunctions.nest(path("/post/log"),
                RouterFunctions.route()
                        .GET("/{page}/{size}", accept(MediaType.APPLICATION_JSON), handler::getPostLog)
                        .DELETE("/{id}", accept(MediaType.APPLICATION_JSON), handler::deleteByIdPostLog)
                        .POST("/del", accept(MediaType.APPLICATION_JSON), handler::deleteAllByIdPostLog)
                        .build()
        );
    }
}
