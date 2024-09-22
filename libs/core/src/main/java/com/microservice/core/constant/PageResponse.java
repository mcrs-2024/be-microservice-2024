package com.microservice.core.constant;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse<T> {
    int pageNumber;
    Long totalPage;
    int limit;
    Page<T> items;
    public PageResponse(Page<T> page) {
        this.pageNumber = page.getNumber();
        this.totalPage = page.getTotalElements();
        this.limit = page.getSize();
        this.items = page;
    }
}
