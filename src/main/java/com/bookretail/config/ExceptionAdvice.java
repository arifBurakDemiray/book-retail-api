package com.bookretail.config;

import com.bookretail.dto.Response;
import com.bookretail.enums.EErrorCode;
import com.bookretail.util.StringUtil;
import com.bookretail.util.exception.ValidationCocException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ExceptionAdvice {
    private final Logger logger = LoggerFactory.getLogger(getClass());


    private final MessageSourceAccessor messageSource;

    @Autowired
    public ExceptionAdvice(MessageSourceAccessor messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Response<?> pageNotFoundHandler() {
        var message = messageSource.getMessage("exception.advice.not_found");
        return Response.notOk(message, EErrorCode.NOT_FOUND);
    }

    @ExceptionHandler({BindException.class,})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Response<?> notValidArguments(@NotNull BindException ex) {
        String message = StringUtil.getMessageFromBindingResult(ex.getBindingResult());
        return Response.notOk(message, EErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Response<Object>> accessDeniedHandler(HttpServletRequest request) {
        if (request.getHeader(HttpHeaders.AUTHORIZATION) == null ||
                request.getAttribute("expired") != null) {
            var message = messageSource.getMessage("exception.advice.unauthorized");
            return Response.notOk(message, EErrorCode.UNAUTHORIZED).toResponseEntity();
        }

        var message = messageSource.getMessage("exception.advice.forbidden");
        return Response.notOk(message, EErrorCode.ACCESS_DENIED).toResponseEntity();
    }

    @ExceptionHandler({ValidationCocException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<?> validationCocException(@NotNull Exception exception) {
        return Response.notOk(exception.getMessage(), EErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler({HttpMediaTypeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<?> badRequest() {
        var message = messageSource.getMessage("exception.advice.bad_request");
        return Response.notOk(message, EErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<?> maxUploadSizeExceededHandler(Exception ex, @NotNull HttpServletRequest req) {
        logger.error(String.format("An error occurred on path %s: %s", req.getServletPath(), ex.getMessage()), ex);
        var message = messageSource.getMessage("exception.advice.max_file_size");
        return Response.notOk(message, EErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<?> globalExceptionHandler(Exception ex, @NotNull HttpServletRequest req) {
        logger.error(String.format("An error occurred on path %s: %s", req.getServletPath(), ex.getMessage()), ex);
        var message = messageSource.getMessage("exception.advice.unhandled");
        return Response.notOk(message, EErrorCode.UNHANDLED);
    }
}
