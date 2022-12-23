package com.bookretail.validator.annotation;

import org.jetbrains.annotations.NotNull;
import com.bookretail.enums.EClientId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ClientIdValidator implements ConstraintValidator<ClientId, String> {
    private final List<String> clientIds;

    public ClientIdValidator(@NotNull EClientId eClientId) {
        clientIds = List.of(
                eClientId.getWebClientId(),
                eClientId.getMobileClientId()
        );
    }

    @Override
    public boolean isValid(String client_id, ConstraintValidatorContext constraintValidatorContext) {
        if (client_id == null || client_id.isEmpty()) {
            return false;
        }
        return clientIds.contains(client_id);
    }
}
