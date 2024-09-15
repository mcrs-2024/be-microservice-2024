package com.microservice.report.controller;

import com.microservice.core.admin.constant.export.ExportUserRequest;
import com.microservice.core.report.constant.ApiUrl;
import com.microservice.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUrl.REPORT_URL)
public class ExportController {

    private final ReportService reportService;

    @PostMapping("/admin/export")
    public ResponseEntity<ByteArrayResource> getList(
            @RequestBody ExportUserRequest exportUserRequest
            ) throws JRException {
        return reportService.export(exportUserRequest);
    }
}
