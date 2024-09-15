package com.microservice.core.admin.constant.export;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExportUserRequest {
    String fileName;
    String typeFile;
    List<ExportUserDto> userResponses;
}
