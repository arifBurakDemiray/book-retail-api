package com.bookretail.util;

public class ReadingIsGoodRegex {

    public static final String PHONE = "^[+]*[0-9]*[ ]{0,1}[(]{0,1}[ ]{0,1}[0-9]{1,3}[ ]{0,1}[)]{0,1}[ ]{0,1}[0-9]{1,3}[ ]{0,1}[0-9]{2}[ ]{0,1}[0-9]{2}[ ]{0,1}[-\\.\\/]{0,1}[ ]{0,1}[0-9]{1,5}$";
    //"^(0|90|\\+90)?5\\d{2}[1-9]\\d{6}$";

    public static final String EMAIL = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
}
