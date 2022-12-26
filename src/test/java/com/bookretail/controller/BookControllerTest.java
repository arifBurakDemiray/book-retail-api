package com.bookretail.controller;

import com.bookretail.dto.Response;
import com.bookretail.dto.book.BookCreateDto;
import com.bookretail.dto.book.BookDto;
import com.bookretail.enums.EClientId;
import com.bookretail.enums.ERole;
import com.bookretail.factory.BookFactory;
import com.bookretail.factory.BookTestFactory;
import com.bookretail.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BookControllerTest {

    private final static String CONTENT_TYPE = "application/json";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EClientId eClientId;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private BookFactory bookFactory;

    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private MessageSource messageSource;


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @Nested
    class GetAllBooks_Endpoint_Test_Cases {

        @Test
        @WithMockUser("spring")
        void GetAllBooks_Returns200() throws Exception {
            //given
            //when
            Page<BookDto> rentalPage = new PageImpl<>(new ArrayList<>());

            //when
            when(bookService.getAllBooks(any())).thenReturn(Response.ok(rentalPage));
            ResultActions actions = mockMvc.perform(
                    get("/book")).andDo(print());

            //then
            actions.andExpect(status().isOk());
            verify(bookService).getAllBooks(any());
        }

        @Test
        @WithMockUser("spring")
        void GetAllBooks_Returns500_PageInvalid() throws Exception {
            //given
            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            ResultActions actions = mockMvc.perform(
                            get("/book")
                                    .queryParam("page", "-1")
                                    .queryParam("page_size", "10"))
                    .andDo(print());

            //then
            actions.andExpect(status().isInternalServerError());
        }

    }

    @Nested
    class CreateABook_Endpoint_Test_Cases {

        @Test
        @WithMockUser("spring")
        void CreateABook_Returns403_USER() throws Exception {

            //given
            var body = BookTestFactory.createBookCreateDto();

            ResultActions actions = mockMvc.perform(
                            post("/book")
                                    .contentType(CONTENT_TYPE)
                                    .content(objectMapper.writeValueAsString(body)))
                    .andDo(print());

            //theen
            actions.andExpect(status().is(403));

        }

        @Test
        @WithMockUser(authorities = {ERole.SYSADMIN})
        void CreateABook_Returns200() throws Exception {

            //given
            var body = BookTestFactory.createBookCreateDto();
            var response = Response.ok(BookTestFactory.createBookDto());

            //when
            when(bookService.createABook(any(BookCreateDto.class))).thenReturn(response);

            ResultActions actions = mockMvc.perform(
                            post("/book")
                                    .contentType(CONTENT_TYPE)
                                    .content(objectMapper.writeValueAsString(body)))
                    .andDo(print());


            //then
            ArgumentCaptor<BookCreateDto> captor = ArgumentCaptor.forClass(BookCreateDto.class);
            verify(bookService).createABook(captor.capture());
            assertThat(captor.getValue().getAuthor()).isEqualTo(body.getAuthor());
            assertThat(captor.getValue().getIsbn()).isEqualTo(body.getIsbn());
            assertThat(captor.getValue().getPrice()).isEqualTo(body.getPrice());
            assertThat(captor.getValue().getTitle()).isEqualTo(body.getTitle());
            actions.andExpect(status().isOk());

        }

        @Test
        @WithMockUser(authorities = {ERole.SYSADMIN})
        void CreateABook_Returns400_BlankNull() throws Exception {

            //given
            var body = BookTestFactory.createBookCreateDtoBlankNull();

            ResultActions actions = mockMvc.perform(
                            post("/book")
                                    .contentType(CONTENT_TYPE)
                                    .content(objectMapper.writeValueAsString(body)))
                    .andDo(print());


            //then
            verify(bookService, times(0)).createABook(any());

            actions.andExpect(mvcResult ->
                    Assertions.assertThat(mvcResult.getResponse().getContentAsString()).contains("validation.book.title.not_blank"));
            actions.andExpect(status().is(400));

        }

        @Test
        @WithMockUser(authorities = {ERole.SYSADMIN})
        void CreateABook_Returns400_NotPositive() throws Exception {
            //Successful post test
            //given
            var body = BookTestFactory.createBookCreateDtoNotPositive();

            ResultActions actions = mockMvc.perform(
                            post("/book")
                                    .contentType(CONTENT_TYPE)
                                    .content(objectMapper.writeValueAsString(body)))
                    .andDo(print());


            verify(bookService, times(0)).createABook(any());

            actions.andExpect(mvcResult ->
                    Assertions.assertThat(mvcResult.getResponse().getContentAsString()).contains("validation.generic.number.positive"));
            actions.andExpect(status().is(400));

        }
    }
}
