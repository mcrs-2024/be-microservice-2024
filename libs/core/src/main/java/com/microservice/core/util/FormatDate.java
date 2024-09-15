package com.microservice.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatDate {
    static SimpleDateFormat sf = new SimpleDateFormat(DateType.DATE_VN);
    public static String dateFormatVN(Date date){
        return sf.format(date);
    }
}