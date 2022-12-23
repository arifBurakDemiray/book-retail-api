package com.bookretail.controller;

import com.bookretail.dto.Response;
import com.bookretail.dto.util.DateTime;
import com.bookretail.service.UtilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Tag(name = UtilController.tag, description = UtilController.description)
@RequestMapping(UtilController.tag)
public class UtilController {

    public static final String description = "Provides many utilities for our developers";
    public static final String tag = "utils";

    private final UtilService utilService;

    @GetMapping("time")
    @Operation(summary = "Get server time")
    public ResponseEntity<Response<DateTime>> getServerTime() {
        return utilService.getServerTime().toResponseEntity();
    }
}
