package com.bookretail.util.status;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.Response;
import com.bookretail.enums.EErrorCode;
import com.bookretail.enums.ESuccessCode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnumUtil {

    @NotNull
    public static List<Response> globalResponses() {
        var all = errorResponses();
        var isOkey = all.addAll(successResponses());
        if (isOkey) {
            return all;
        } else {
            return new ArrayList<>();
        }
    }

    private static List<Response> successResponses() {
        return ESuccessCode.successCodes().stream().map(EnumUtil::mapToResponse).collect(Collectors.toList());
    }

    private static List<Response> errorResponses() {
        return EErrorCode.errorCodes().stream().map(EnumUtil::mapToResponse).collect(Collectors.toList());
    }

    private static Response mapToResponse(@NotNull EStatusCode code) {
        return new ResponseBuilder()
                .code(code.getStatusCode())
                .description(code.getStatusName())
                .representation(MediaType.ALL)
                .apply(r ->
                        r.model(m ->
                                m.referenceModel(ref ->
                                        ref.key(k ->
                                                k.qualifiedModelName(q ->
                                                        q.namespace(AdditionalModel.PATH)
                                                                .name(AdditionalModel.NAME)
                                                                .build())
                                                        .build())
                                                .build())
                                        .build())
                                .build())
                .build();
    }
}
