package com.microservice.core.util;

public class CheckEmpty {
    public static String checkStr(Object o){
        if(o == null) {
            return null;
        }
        return String.valueOf(o);
    }
    public static Double checkDouble(Object o){
        if(o == null){
            return null;
        }
        return Double.valueOf(String.valueOf(o));
    }

}
