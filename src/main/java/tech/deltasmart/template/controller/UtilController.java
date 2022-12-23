package com.bookretail.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bookretail.dto.Response;
import com.bookretail.dto.util.DateTime;
import com.bookretail.service.UtilService;

@RestController
@AllArgsConstructor
@Api(tags = UtilController.tag)
@RequestMapping(UtilController.tag)
public class UtilController {

    public static final String description = "Provides many utilities for our developers";
    public static final String tag = "utils";

    private final UtilService utilService;

    @GetMapping("time")
    @ApiOperation(value = "Get server time")
    public ResponseEntity<Response<DateTime>> getServerTime() {
        return utilService.getServerTime().toResponseEntity();
    }
}
