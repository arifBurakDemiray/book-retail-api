package com.bookretail.enums;

public interface EStatusCode {

    /**
     * This function is for gettin status code of a Http Status
     *
     * @return Status Code of the Http
     */
    String getStatusCode();

    /**
     * This function returns name of the status
     *
     * @return name of the status
     */
    String getStatusName();

    /**
     * This function returns integer value of status code
     *
     * @return integer value of status code
     */
    int httpStatusCode();
}
