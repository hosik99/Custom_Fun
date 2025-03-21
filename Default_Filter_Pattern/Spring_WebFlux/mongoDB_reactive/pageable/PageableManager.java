package com.icetea.monstu_back.mongo.pageable;

import org.springframework.data.domain.Sort;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.time.LocalDateTime;

public interface PageableManager<T> {
    CustomPageableDTO extract(ServerRequest request);   // request에서 값을 추출해서 CustomPageableDTO를 반환
    Sort createSort(CustomPageableDTO dto);     // CustomPageableDTO 객체를 사용해서 Sort 객체를 반환
    Class<?> convertFilterValue(String filterOption);     //filterOption 타입을 확인

    default CustomPageableDTO extractDefault(ServerRequest request) {
        String dateStartParam = request.queryParam("dateStart").orElse(null);
        String dateEndParam = request.queryParam("dateEnd").orElse(null);

        int page = Integer.parseInt(request.pathVariable("page"));
        int size = Integer.parseInt(request.pathVariable("size"));

        if ( page < 0 || size <= 0) throw new IllegalArgumentException("유효한 Page, Size 값이여야 합니다.");

        return CustomPageableDTO.builder()
                .page(page)
                .size(size)

                .sortValue(request.queryParam("sortValue").orElse("id"))
                .sortDirection(request.queryParam("sortDirection").orElse("DESC"))

                .filterOption(request.queryParam("filterOption").orElse(null))
                .filterValue(request.queryParam("filterValue").orElse(null))

                .dateOption(request.queryParam("dateOption").orElse(null))
                .dateStart( (dateStartParam != null) ? LocalDateTime.parse(dateStartParam) : null )
                .dateEnd( (dateEndParam != null) ? LocalDateTime.parse(dateEndParam) : null )
                .build();
    }


    default Sort createSortDefault(CustomPageableDTO dto) {
        return dto.getSortDirection().equals("DESC")
                ? Sort.by(Sort.Direction.DESC, dto.getSortValue())
                : Sort.by(Sort.Direction.ASC, dto.getSortValue());
    }


/*
    Class<?> type = cateLogManager.convertFilterValue( dto.getFilterOption() );

    Query query = new Query()
        .addCriteria(Criteria.where( dto.getFilterOption() ).is( cateLogManager.castValue(type,dto.getFilterValue()) ))
 */
// manager.convertFilterValue() 메소드에 설정해둔 클래스로 캐스팅
    default <U> U castValue(Class<U> clazz, String value) {
        if (clazz == Long.class) {
            return clazz.cast(Long.parseLong(value));
        } else if (clazz == LocalDateTime.class) {
            return clazz.cast(LocalDateTime.parse(value));
        } else if (clazz == String.class) {
            return clazz.cast(value);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + clazz.getName());
        }
    }

}


