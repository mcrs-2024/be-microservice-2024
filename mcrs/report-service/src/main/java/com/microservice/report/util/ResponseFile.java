package com.microservice.report.util;

import com.microservice.core.constant.ExportType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;

public class ResponseFile {

    public static ResponseEntity<ByteArrayResource> createFileResponse(
            byte[] fileBytes,
            String fileName,
            String fileType
    ) {
        MediaType mediaType;
        switch (fileType.toLowerCase()) {
            case "pdf":
                mediaType = MediaType.APPLICATION_PDF;
                break;
            case "excel":
                mediaType = MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                break;
            case "word":
                mediaType = MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                break;
            case "html":
                mediaType = MediaType.TEXT_HTML;
                break;
            default:
                return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
        ByteArrayResource resource = new ByteArrayResource(fileBytes);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName + "." + fileType)
                .contentType(mediaType)
                .contentLength(fileBytes.length)
                .body(resource);
    }

    public static byte[] exportPdf(JasperPrint jasperPrint){
        try {
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] exportExcel(JasperPrint jasperPrint) {
        // Tạo đối tượng ByteArrayOutputStream để lưu trữ dữ liệu xuất
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JRXlsxExporter exporter = new JRXlsxExporter();
        try {
            // Cấu hình cho xuất báo cáo Excel
            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setIgnoreGraphics(false);
            configuration.setWhitePageBackground(false);
            configuration.setOnePagePerSheet(false);
            configuration.setDetectCellType(true);
            // Thiết lập các thành phần của exporter
            ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
            ExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(outputStream);
            exporter.setExporterInput(exporterInput);
            exporter.setExporterOutput((OutputStreamExporterOutput) exporterOutput);
            exporter.setConfiguration(configuration);
            // Thực hiện xuất báo cáo
            exporter.exportReport();
            // Trả về dữ liệu dưới dạng byte[]
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi xuất file excel ", e);
        } finally {
            try {
                outputStream.close();
            } catch (Exception e) {
                // Log lỗi khi đóng stream
                e.printStackTrace();
            }
        }
    }

    // Phương thức xuất báo cáo sang định dạng Word
    public static byte[] exportWord(JasperPrint jasperPrint) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JRDocxExporter exporter = new JRDocxExporter();
        try {
            SimpleDocxReportConfiguration configuration = new SimpleDocxReportConfiguration();
            // Chỉ định cấu hình cơ bản nếu cần
            // Có thể không cần thiết phải cấu hình các phương thức đã lỗi
            ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
            ExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(outputStream);
            exporter.setExporterInput(exporterInput);
            exporter.setExporterOutput((OutputStreamExporterOutput) exporterOutput);
            exporter.setConfiguration(configuration);
            exporter.exportReport();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xuất file word", e);
        } finally {
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static ResponseEntity<ByteArrayResource> responseFile(String typeFile,String fileName, JasperPrint jasperPrint){
        byte[] excelFile = null;
        switch (typeFile) {
            case "pdf":
                excelFile = ResponseFile.exportPdf(jasperPrint);
                break;
            case "xlsx":
                excelFile = ResponseFile.exportExcel(jasperPrint);
                break;
            case "doc":
                excelFile = ResponseFile.exportWord(jasperPrint);
                break;
        }
        if(excelFile != null){
            return ResponseFile.createFileResponse(excelFile, fileName, ExportType.FILE_TYPE_EXCEL);
        }
        return null;
    }
}
