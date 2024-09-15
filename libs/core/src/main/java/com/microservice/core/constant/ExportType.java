package com.microservice.core.constant;

import com.microservice.core.util.FormatDate;

import java.util.Date;

public class ExportType {
    public static final String FILE_TYPE_EXCEL = "excel";
    public static final String FILE_TYPE_WORD = "word";
    public static final String FILE_TYPE_PDF = "pdf";
    public static final String FILE_TYPE_HTML = "html";
    public static final String REPORT = "REPORT - DATE: " + FormatDate.dateFormatVN(new Date());
    public static final String TITLE_REPORT = "report";
    private ExportType (){}
}
