package com.bookretail.validator.coc;

import com.bookretail.model.Book;
import com.bookretail.util.exception.ValidationCocException;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.Optional;

public class BookValidator extends ValidatorCoc {

    private final MessageSourceAccessor messageSource;

    private final Optional<Book> maybeBook;

    public BookValidator(ValidatorCoc coc, MessageSourceAccessor messageSource, Optional<Book> maybeBook) {
        super(coc);
        this.messageSource = messageSource;
        this.maybeBook = maybeBook;
    }

    @Override
    public void validate() throws ValidationCocException {
        if (maybeBook.isEmpty()) {
            throw new ValidationCocException(messageSource.getMessage("validation.order.book.not_found"));
        }

        checkNext();
    }
}
