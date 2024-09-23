package com.microservice.core.constant;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageListResponse<T> {

    int pageNumber;
    Long totalPage;
    int limit;
    Collection<T> items;

    public PageListResponse(int pageNumber, Long totalPage, int limit, Collection<T> items){
        this.pageNumber = pageNumber;
        this.totalPage = totalPage;
        this.limit = limit;
        this.items = items;
    }
}
