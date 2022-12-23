package com.bookretail.validator;

import lombok.AllArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.bookretail.config.MimeTypesConfig;

@Component
@AllArgsConstructor
public class UserValidator {
    private final MimeTypesConfig mimeTypesConfig;
    private final MessageSourceAccessor messageSource;

    public ValidationResult validate(MultipartFile multipartFile) {
        var isSupported = mimeTypesConfig
                .getValidImageMimeTypes()
                .contains(multipartFile.getContentType());

        return isSupported ?
                ValidationResult.success() :
                ValidationResult.failed(messageSource
                        .getMessage("update_profile_picture.mime_type.invalid"));
    }
}
