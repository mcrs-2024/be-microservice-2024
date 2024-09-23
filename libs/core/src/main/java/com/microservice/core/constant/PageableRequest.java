package com.microservice.core.constant;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
public class PageableRequest {

    public static Pageable formatPage(int pageRequest, int limit){
        int page = 0;
        if(pageRequest > 0){
            page = pageRequest - 1;
        }
        return PageRequest.of( page, limit);
    }

}
