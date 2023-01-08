package com.bookretail.validator.coc;

import com.bookretail.dto.order.OrderCreateDto;
import com.bookretail.model.Book;
import com.bookretail.model.User;
import com.bookretail.util.exception.ValidationCocException;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.Optional;

public class UserBookValidator extends ValidatorCoc {

    private final User user;

    private final MessageSourceAccessor messageSource;

    private final Optional<Book> maybeBook;

    private final OrderCreateDto body;

    public UserBookValidator(MessageSourceAccessor messageSource, User user, Optional<Book> maybeBook, OrderCreateDto body) {
        super();
        this.messageSource = messageSource;
        this.user = user;
        this.maybeBook = maybeBook;
        this.body = body;
    }

    @Override
    public void validate() throws ValidationCocException {
        var book = maybeBook.get();

        if (book.getBookDetail().getStock() < body.getQuantity()) {
            throw new ValidationCocException(messageSource.getMessage("validation.order.book.stock.not_enough"));
        }

        if (user.getMoney() == null || user.getMoney() < book.getBookDetail().getPrice() * body.getQuantity()) {
            throw new ValidationCocException(messageSource.getMessage("validation.order.user.money.not_enough"));
        }

        checkNext();
    }
}
