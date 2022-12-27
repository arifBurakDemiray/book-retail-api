package com.bookretail.dto.order;

import com.bookretail.dto.auth.RegisterDto;
import com.bookretail.dto.book.BookDto;
import com.bookretail.enums.EOrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto {

    private Long id;

    private Long quantity;

    private Double cost;

    private Long bookId;

    private Long userId;

    private EOrderStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date updatedAt;

    private RegisterDto user;

    private BookDto book;

}
