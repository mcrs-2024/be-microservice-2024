package com.microservice.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi chưa xử lý", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Lỗi chưa xử lý", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(1003, "Tên người dùng phải có ít nhất {min} ký tự", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Mật khẩu phải có ít nhất {min} Ký tự", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Chưa xác thực", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "Không có quyền", HttpStatus.FORBIDDEN),
    UNAUTHORIZED_EXCEPTION(409, "Tài khoản này đã tồn tại", HttpStatus.CONFLICT),
    EMAIL_EXISTED(1008,"Email này đã tồn tại", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1009,"User này đã tồn tại", HttpStatus.BAD_REQUEST),
    USERNAME_IS_MISSING(1010,"Làm ơn nhập username", HttpStatus.BAD_REQUEST),
    OBJECT_IS_NULL(1011,"Đối tượng rỗng", HttpStatus.BAD_REQUEST),
    INPUT_ERROR(1012,"Đầu vào không hợp lệ", HttpStatus.BAD_REQUEST),
    OBJECT_NOT(1013,"Không tìm thấy đối tượng", HttpStatus.BAD_REQUEST)
    ;
    private final int code;
    private final String message;
    private final HttpStatus statusCode;
}
