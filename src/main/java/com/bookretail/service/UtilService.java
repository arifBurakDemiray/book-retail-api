package com.bookretail.service;

import org.springframework.stereotype.Service;
import com.bookretail.dto.Response;
import com.bookretail.dto.util.DateTime;

import java.util.Date;

@Service
public class UtilService {

    public Response<DateTime> getServerTime() {
        return Response.ok(new DateTime(new Date()));
    }
}
