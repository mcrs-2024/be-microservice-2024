package com.microservice.core.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateType {
    public static final String DATE_VN = "dd/MM/yyyy";
    public static Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    private DateType(){}
}