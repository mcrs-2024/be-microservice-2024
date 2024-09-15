package com.microservice.core.exception;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationException extends RuntimeException {
    private ErrorCode errorCode;
}
