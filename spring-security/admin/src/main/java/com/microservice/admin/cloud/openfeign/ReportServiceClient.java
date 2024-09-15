package com.microservice.admin.cloud.openfeign;

import com.microservice.core.admin.constant.export.ExportUserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "report-service", url = "${report-service.url}")
public interface ReportServiceClient {
    @PostMapping("/admin/export")
    ResponseEntity<ByteArrayResource> report(@RequestBody ExportUserRequest exportUserRequest);
}