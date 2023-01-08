package com.bookretail.validator.coc;

import com.bookretail.util.exception.ValidationCocException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class ValidatorCoc {

    private ValidatorCoc next;

    public abstract void validate() throws ValidationCocException;

    public void checkNext() throws ValidationCocException {
        if (next != null) {
            next.validate();
        }
    }


}
