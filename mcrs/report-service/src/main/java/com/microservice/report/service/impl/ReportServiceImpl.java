package com.microservice.report.service.impl;

import com.microservice.core.admin.constant.export.ExportUserDto;
import com.microservice.core.admin.constant.export.ExportUserRequest;
import com.microservice.core.constant.ExportType;
import com.microservice.report.util.ResponseFile;
import com.microservice.core.report.constant.PathFile;
import com.microservice.report.service.ReportService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    @Override
    public ResponseEntity<ByteArrayResource> export(ExportUserRequest report) throws JRException {
        List<ExportUserDto> items = report.getUserResponses();
        JRDataSource dataSource = new JRBeanCollectionDataSource(items);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PathFile.PATH_FILE);
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(ExportType.TITLE_REPORT, ExportType.REPORT);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return ResponseFile.responseFile(report.getTypeFile(),report.getFileName(),jasperPrint );
    }


}
