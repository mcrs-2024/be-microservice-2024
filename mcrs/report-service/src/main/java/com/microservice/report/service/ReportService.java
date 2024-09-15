package com.microservice.report.service;

import com.microservice.core.admin.constant.export.ExportUserRequest;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

public interface ReportService {
    ResponseEntity<ByteArrayResource> export(ExportUserRequest exportUserRequest) throws JRException;
}
