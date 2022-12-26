package com.bookretail.controller;


import com.bookretail.dto.Response;
import com.bookretail.dto.message.BasicResponse;
import com.bookretail.dto.user.ProfilePictureUpdateDto;
import com.bookretail.enums.EErrorCode;
import com.bookretail.factory.UserTestFactory;
import com.bookretail.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerTest {


    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private MessageSource messageSource;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @Nested
    class GetProfile_Endpoint_Test_Cases {

        @Test
        @WithMockUser("spring")
        void GetProfile_Returns200() throws Exception {
            //Successful post test
            //given
            var createdAt = LocalDate.ofInstant((new Date()).toInstant(), ZoneId.systemDefault()).atStartOfDay();
            Date resultCreatedAt = Date.from(createdAt.atZone((ZoneId.of("UTC"))).toInstant());
            var profileDto = UserTestFactory.createProfileDto(resultCreatedAt);

            when(userService.getProfile(anyString())).thenReturn(Response.ok(profileDto));
            var actions = mockMvc.perform(
                    get("/users/me")
                            .header(HttpHeaders.AUTHORIZATION, false)).andDo(print());

            //then
            actions.andExpect(status().isOk());
            verify(userService).getProfile(anyString());
        }

        @Test
        void GetProfile_Returns403() throws Exception {
            //given
            var createdAt = LocalDate.ofInstant((new Date()).toInstant(), ZoneId.systemDefault()).atStartOfDay();
            Date resultCreatedAt = Date.from(createdAt.atZone((ZoneId.of("UTC"))).toInstant());
            var profileDto = UserTestFactory.createProfileDto(resultCreatedAt);

            //when
            when(messageSource.getMessage(any(), any(), any())).thenReturn("Message");
            when(userService.getProfile(anyString())).thenReturn(Response.ok(profileDto));
            var actions = mockMvc.perform(
                    get("/users/me")
                            .header(HttpHeaders.AUTHORIZATION, false)).andDo(print());

            //then
            actions.andExpect(status().isForbidden());
        }


        @Test
        @WithMockUser("spring")
        void GetProfile_Returns500() throws Exception {
            //given

            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            var actions = mockMvc.perform(
                    get("/users/me")
                            .header(HttpHeaders.AUTHORIZATION, false)).andDo(print());

            //then
            actions.andExpect(status().isInternalServerError());
        }
    }


    @Nested
    class DepositMoney_Endpoint_Test_Cases {

        @Test
        @WithMockUser("spring")
        void DepositMoney_Returns200() throws Exception {
            //Successful post test
            //given
            when(userService.depositMoney(anyString(), anyDouble())).thenReturn(Response.ok(new BasicResponse("Message")));
            var actions = mockMvc.perform(
                    patch("/users")
                            .queryParam("amount", "10.0")
                            .header(HttpHeaders.AUTHORIZATION, false)).andDo(print());

            //then
            actions.andExpect(status().isOk());
            verify(userService).depositMoney(anyString(), anyDouble());
        }

        @Test
        @WithMockUser("spring")
        void DepositMoney_Returns400() throws Exception {
            //given

            //when
            when(userService.depositMoney(anyString(), anyDouble())).thenReturn(Response.notOk("Invalid", EErrorCode.BAD_REQUEST));
            var actions = mockMvc.perform(
                    patch("/users")
                            .queryParam("amount", "-10.0")
                            .header(HttpHeaders.AUTHORIZATION, false)).andDo(print());

            //then
            actions.andExpect(status().is(400));
            actions.andExpect(mvcResult -> assertThat(mvcResult.getResponse().getContentAsString()).contains("Invalid"));

            verify(userService).depositMoney(anyString(), anyDouble());
        }

        @Test
        @WithMockUser("spring")
        void DepositMoney_Returns500_ParamNull() throws Exception {
            //given

            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            var actions = mockMvc.perform(
                            patch("/users")
                                    .header(HttpHeaders.AUTHORIZATION, false))
                    .andDo(print());

            //then
            actions.andExpect(status().isInternalServerError());

        }
    }

    @Nested
    class GetProfilePictureUrl_Endpoint_Test_Cases {

        @Test
        @WithMockUser("spring")
        void GetProfilePictureUrl_Returns200() throws Exception {
            //Successful post test
            //given
            var profilePictureUpdateDto = new ProfilePictureUpdateDto("https://example.com/abc.jpg");
            var file
                    = new MockMultipartFile(
                    "file",
                    "photo.jpeg",
                    MediaType.IMAGE_JPEG_VALUE,
                    "photo".getBytes()
            );

            //when
            when(userService.updateProfilePicture(anyString(), any())).thenReturn(Response.ok(profilePictureUpdateDto));
            var actions = mockMvc.perform(
                            multipart("/users/me/picture")
                                    .file(file)
                                    .with(req -> {
                                        req.setMethod("PUT");
                                        return req;
                                    })
                                    .header(HttpHeaders.AUTHORIZATION, false))
                    .andDo(print());

            //then
            ArgumentCaptor<MockMultipartFile> captor = ArgumentCaptor.forClass(MockMultipartFile.class);
            actions.andExpect(status().isOk());
            verify(userService).updateProfilePicture(anyString(), captor.capture());
            assertThat(captor.getValue().getOriginalFilename()).isEqualTo(file.getOriginalFilename());
            assertThat(captor.getValue().getContentType()).isEqualTo(file.getContentType());
            assertThat(captor.getValue().getName()).isEqualTo(file.getName());
            assertThat(captor.getValue().getBytes()).isEqualTo(file.getBytes());
        }

        @Test
        void GetProfilePictureUrl_Returns403() throws Exception {
            //given
            var file
                    = new MockMultipartFile(
                    "file",
                    "photo.jpeg",
                    MediaType.IMAGE_JPEG_VALUE,
                    "photo".getBytes()
            );

            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            when(userService.updateProfilePicture(anyString(), any()))
                    .thenReturn(Response.notOk("403 Forbidden Error", EErrorCode.ACCESS_DENIED));
            var actions = mockMvc.perform(
                            multipart("/users/me/picture")
                                    .file(file)
                                    .with(req -> {
                                        req.setMethod("PUT");
                                        return req;
                                    })
                                    .header(HttpHeaders.AUTHORIZATION, false))
                    .andDo(print());

            //then
            actions.andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser("spring")
        void GetProfilePictureUrl_Returns404() throws Exception {
            //given
            var file
                    = new MockMultipartFile(
                    "file",
                    "photo.jpeg",
                    MediaType.IMAGE_JPEG_VALUE,
                    "photo".getBytes()
            );

            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            when(userService.updateProfilePicture(anyString(), any()))
                    .thenReturn(Response.notOk("404 Not Found", EErrorCode.NOT_FOUND));
            var actions = mockMvc.perform(
                            multipart("/me/picture")
                                    .file(file)
                                    .with(req -> {
                                        req.setMethod("PUT");
                                        return req;
                                    })
                                    .header(HttpHeaders.AUTHORIZATION, false))
                    .andDo(print());

            //then
            actions.andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser("spring")
        void GetProfilePictureUrl_Returns500() throws Exception {
            //given
            var file
                    = new MockMultipartFile(
                    "file",
                    "photo.jpeg",
                    MediaType.IMAGE_JPEG_VALUE,
                    "photo".getBytes()
            );

            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            var actions = mockMvc.perform(
                            multipart("/users/me/picture")
                                    .file(file)
                                    .with(req -> {
                                        req.setMethod("PUT");
                                        return req;
                                    })
                                    .header(HttpHeaders.AUTHORIZATION, false))
                    .andDo(print());

            //then
            actions.andExpect(status().isInternalServerError());
        }


    }

}

