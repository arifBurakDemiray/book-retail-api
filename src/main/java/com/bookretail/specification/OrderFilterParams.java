package com.bookretail.specification;

import com.bookretail.enums.EOrderStatus;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;

@Parameters({
        @Parameter(name = "startTime", schema = @Schema(implementation = Date.class), in = ParameterIn.QUERY),
        @Parameter(name = "endTime", schema = @Schema(implementation = Date.class), in = ParameterIn.QUERY),
        @Parameter(name = "type", schema = @Schema(implementation = EOrderStatus.class), in = ParameterIn.QUERY)
})
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderFilterParams {
}
