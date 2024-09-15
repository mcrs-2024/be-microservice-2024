package com.microservice.core.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.core.exception.response.KeyCloackError;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class ErrorNormalizer {

    private final ObjectMapper objectMapper;
    private final Map<String, ErrorCode> errorCodeMap;

    public ErrorNormalizer() {
        objectMapper = new ObjectMapper();
        errorCodeMap = new HashMap<>();
        errorCodeMap.put("User exists with same username", ErrorCode.USER_EXISTED);
        errorCodeMap.put("User exists with same email", ErrorCode.EMAIL_EXISTED);
    }

    public ApplicationException handleKeyCloakException(FeignException ex){
        try {
            log.warn("xảy ra lỗi: " , ex);
            var response = objectMapper.readValue(ex.contentUTF8(), KeyCloackError.class);
            if(Objects.nonNull(response.getErrorMessage()) &&
                    Objects.nonNull(errorCodeMap.get(response.getErrorMessage()))){
                return new ApplicationException(errorCodeMap.get(response.getErrorMessage()));
            }
        } catch (JsonMappingException e){
            log.error("Không thể parse ", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new ApplicationException(ErrorCode.UNCATEGORIZED_EXCEPTION);
    }

}
