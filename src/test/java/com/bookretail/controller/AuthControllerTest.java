package com.bookretail.controller;

import com.bookretail.dto.Response;
import com.bookretail.dto.auth.*;
import com.bookretail.enums.EClientId;
import com.bookretail.enums.EErrorCode;
import com.bookretail.factory.UserTestFactory;
import com.bookretail.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthControllerTest {

    private final static String CONTENT_TYPE = "application/json";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EClientId eClientId;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

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
    class Login_Endpoint_Test_Cases {


        private MockHttpServletRequestBuilder putLoginParams(MockHttpServletRequestBuilder requestBuilder) {
            var body = UserTestFactory.createLoginRequest();

            return requestBuilder.param("grant_type", body.getGrant_type().toString())
                    .param("client_id", body.getClient_id())
                    .param("client_secret", body.getClient_secret())
                    .param("username", body.getUsername())
                    .param("password", body.getPassword())
                    .param("refresh_token", body.getRefresh_token());
        }

        @Test
        @PreAuthorize("isAnonymous()")
        void Login_Returns200() throws Exception {
            //given
            var body = UserTestFactory.createLoginRequest();
            //when
            when(authService.login(any())).
                    thenReturn(LoginResponse.ok("access token", "refresh token"));


            ResultActions actions = mockMvc.perform(
                            putLoginParams(
                                    post("/auth/login")
                                            .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)))
                    .andDo(print());

            //then
            ArgumentCaptor<LoginRequest> captor = ArgumentCaptor.forClass(LoginRequest.class);

            verify(authService).login(captor.capture());

            actions.andExpect(status().isOk());
            assertThat(captor.getValue().getGrant_type()).isEqualTo(body.getGrant_type());
            assertThat(captor.getValue().getClient_id()).isEqualTo(body.getClient_id());
            assertThat(captor.getValue().getClient_secret()).isEqualTo(body.getClient_secret());
            assertThat(captor.getValue().getUsername()).isEqualTo(body.getUsername());
            assertThat(captor.getValue().getPassword()).isEqualTo(body.getPassword());
            assertThat(captor.getValue().getRefresh_token()).isEqualTo(body.getRefresh_token());
        }

        @Test
        @PreAuthorize("isAnonymous()")
        void Login_Returns400_WithBindException() throws Exception {
            //given
            var body = UserTestFactory.createLoginRequest();

            //when
            when(authService.login(any())).
                    thenReturn(LoginResponse.notOk("400 Bad Request", EErrorCode.BAD_REQUEST));
            ResultActions actions = mockMvc.perform(
                            post("/auth/login")
                                    .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                    .content(objectMapper.writeValueAsString(body)))
                    .andDo(print());

            //then
            actions.andExpect(status().isBadRequest());
        }

        @Test
        @PreAuthorize("isAnonymous()")
        void Login_Returns401() throws Exception {
            //given

            //when
            //If result.isSuccess() is false then method returns 401 Unauthorized Error
            when(authService.login((LoginRequest) any())).
                    thenReturn(LoginResponse.notOk("401 Unauthorized Error", EErrorCode.UNAUTHORIZED));
            ResultActions actions = mockMvc.perform(
                            putLoginParams(
                                    post("/auth/login")
                                            .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)))
                    .andDo(print());

            //then
            verify(authService).login(any());
            actions.andExpect(status().isUnauthorized());
        }

        @Test
        @PreAuthorize("isAnonymous()")
        void Login_Returns404() throws Exception {
            //given

            //when
            when(authService.login(any())).
                    thenReturn(LoginResponse.notOk("404 Not Found Error", EErrorCode.NOT_FOUND));
            ResultActions actions = mockMvc.perform(
                            putLoginParams(
                                    post("/auh/login")
                                            .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)))
                    .andDo(print());

            //then
            actions.andExpect(status().isNotFound());
        }

        @Test
        @PreAuthorize("isAnonymous()")
        void Login_Returns500() throws Exception {
            //given

            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            ResultActions actions = mockMvc.perform(
                            putLoginParams(
                                    post("/auth/login")
                                            .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)))
                    .andDo(print());

            //then
            actions.andExpect(status().isInternalServerError());
        }

    }

    @Nested
    class RegisterUser_Endpoint_Test_Cases {

        @Test
        @PreAuthorize("isAnonymous()")
        void RegisterUser_Returns200() throws Exception {
            //Successful post test
            //given
            var registerDto = UserTestFactory.createRegisterDto();
            var body = UserTestFactory.createRegisterRequest();

            //when
            when(authService.register(any())).thenReturn(Response.ok(registerDto));
            ResultActions actions = mockMvc.perform(
                    post("/auth/register")
                            .contentType(CONTENT_TYPE)
                            .content(objectMapper.writeValueAsString(body))).andDo(print());

            //then
            ArgumentCaptor<RegisterRequest> captor = ArgumentCaptor.forClass(RegisterRequest.class);
            actions.andExpect(status().isOk());
            verify(authService).register(captor.capture());
            assertThat(captor.getValue().getEmail()).isEqualTo(body.getEmail());
            assertThat(captor.getValue().getPassword()).isEqualTo(body.getPassword());
            assertThat(captor.getValue().getPhone()).isEqualTo(body.getPhone());
            assertThat(captor.getValue().getName()).isEqualTo(body.getName());
            assertThat(captor.getValue().getSurname()).isEqualTo(body.getSurname());
        }

        @Test
        @PreAuthorize("isAnonymous()")
        void RegisterUser_Returns404() throws Exception {
            //given
            var body = new RegisterRequest();

            //when
            when(authService.register(any()))
                    .thenReturn(Response.notOk("404 Not Found", EErrorCode.NOT_FOUND));
            ResultActions actions = mockMvc.perform(
                    post("/auths/register")
                            .contentType(CONTENT_TYPE)
                            .content(objectMapper.writeValueAsString(body))).andDo(print());

            //then
            actions.andExpect(status().isNotFound());
        }

        @Test
        @PreAuthorize("isAnonymous()")
        void RegisterUser_Returns500() throws Exception {
            //given
            var body = new RegisterRequest();

            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            ResultActions actions = mockMvc.perform(
                    post("/auth/register")
                            .contentType(CONTENT_TYPE)
                            .content(objectMapper.writeValueAsString(body))).andDo(print());

            //then
            actions.andExpect(status().isInternalServerError());
        }
    }

    @Nested
    class ResendActivationCode_Endpoint_Test_Cases {

        @Test
        @PreAuthorize("isAnonymous()")
        void ResendActivationCode_Returns200() throws Exception {
            //Successful post test
            //given
            var resendActivationCodeDto = new ResendActivationCodeDto("Successful");
            var body = new ResendActivationCodeRequest
                    (eClientId.getWebClientId(), "ali.veli@example.com");

            //when
            when(authService.resendActivationCode(any()))
                    .thenReturn(Response.ok(resendActivationCodeDto));
            ResultActions actions = mockMvc.perform(
                    post("/auth/resend-activation-code")
                            .contentType(CONTENT_TYPE)
                            .content(objectMapper.writeValueAsString(body))).andDo(print());

            //then
            ArgumentCaptor<ResendActivationCodeRequest> captor = ArgumentCaptor.forClass(ResendActivationCodeRequest.class);
            actions.andExpect(status().isOk());
            verify(authService).resendActivationCode(captor.capture());
            assertThat(captor.getValue().getEmail()).isEqualTo(body.getEmail());
            assertThat(captor.getValue().getClientId()).isEqualTo(body.getClientId());
        }

        @Test
        @PreAuthorize("isAnonymous()")
        void ResendActivationCode_Returns400() throws Exception {
            //Successful post test
            //given
            ResendActivationCodeRequest body = new ResendActivationCodeRequest
                    ("1234", "ali.veli@example.com");

            //when
            when(authService.resendActivationCode(any()))
                    .thenReturn(Response.notOk("400 Bad Request", EErrorCode.BAD_REQUEST));
            ResultActions actions = mockMvc.perform(
                    post("/auth/resend-activation-code")
                            .contentType(CONTENT_TYPE)
                            .content(objectMapper.writeValueAsString(body))).andDo(print());

            //then
            actions.andExpect(status().isBadRequest());
        }

        @Test
        @PreAuthorize("isAnonymous()")
        void ResendActivationCode_Returns404() throws Exception {
            //Successful post test
            //given
            var resendActivationCodeDto = new ResendActivationCodeDto("Successful");
            var body = new ResendActivationCodeRequest
                    (eClientId.getWebClientId(), "ali.veli@example.com");

            //when
            when(authService.resendActivationCode(any()))
                    .thenReturn(Response.ok(resendActivationCodeDto));
            ResultActions actions = mockMvc.perform(
                    post("/auths/resend-activation-code")
                            .contentType(CONTENT_TYPE)
                            .content(objectMapper.writeValueAsString(body))).andDo(print());

            //then
            actions.andExpect(status().isNotFound());
        }

        @Test
        @PreAuthorize("isAnonymous()")
        void ResendActivationCode_Returns500() throws Exception {
            //Successful post test
            //given
            var body = new ResendActivationCodeRequest
                    (eClientId.getWebClientId(), "ali.veli@example.com");

            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            ResultActions actions = mockMvc.perform(
                    post("/auth/resend-activation-code")
                            .contentType(CONTENT_TYPE)
                            .content(objectMapper.writeValueAsString(body))).andDo(print());

            //then
            actions.andExpect(status().isInternalServerError());
        }

    }

    @Nested
    class ActivateAccount_Endpoint_Test_Cases {

        @Test
        @PreAuthorize("isAnonymous()")
        void ActivateAccount_Returns200() throws Exception {
            //Successful post test
            //given
            var body = new AccountActivationRequest(UserTestFactory.createRandomCode());
            var accountActivationDto = new AccountActivationDto("Successful");

            //when
            when(authService.activateAccount(any()))
                    .thenReturn(Response.ok(accountActivationDto));
            ResultActions actions = mockMvc.perform(
                    post("/auth/activate")
                            .contentType(CONTENT_TYPE)
                            .content(objectMapper.writeValueAsString(body))).andDo(print());

            //then
            ArgumentCaptor<AccountActivationRequest> captor = ArgumentCaptor.forClass(AccountActivationRequest.class);
            actions.andExpect(status().isOk());
            verify(authService).activateAccount(captor.capture());
            assertThat(captor.getValue().getCode()).isEqualTo(body.getCode());

        }

        @Test
        @PreAuthorize("isAnonymous()")
        void ActivateAccount_Returns400() throws Exception {
            //given
            var body = new AccountActivationRequest();

            //when
            when(authService.activateAccount(any()))
                    .thenReturn(Response.notOk("400 Bad Request", EErrorCode.BAD_REQUEST));
            ResultActions actions = mockMvc.perform(
                    post("/auth/activate")
                            .contentType(CONTENT_TYPE)
                            .content(objectMapper.writeValueAsString(body))).andDo(print());

            //then
            actions.andExpect(status().isBadRequest());

        }

        @Test
        @PreAuthorize("isAnonymous()")
        void ActivateAccount_Returns404() throws Exception {
            //given
            var body = new AccountActivationRequest(UserTestFactory.createRandomCode());

            //when
            when(authService.activateAccount((AccountActivationRequest) any()))
                    .thenReturn(Response.notOk("404 Not Found", EErrorCode.NOT_FOUND));
            ResultActions actions = mockMvc.perform(
                    post("/auths/activate")
                            .contentType(CONTENT_TYPE)
                            .content(objectMapper.writeValueAsString(body))).andDo(print());

            //then
            actions.andExpect(status().isNotFound());

        }

        @Test
        @PreAuthorize("isAnonymous()")
        void ActivateAccount_Returns500() throws Exception {
            //given
            AccountActivationRequest body = new AccountActivationRequest(UserTestFactory.createRandomCode());

            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            ResultActions actions = mockMvc.perform(
                    post("/auth/activate")
                            .contentType(CONTENT_TYPE)
                            .content(objectMapper.writeValueAsString(body))).andDo(print());

            //then
            actions.andExpect(status().isInternalServerError());

        }
    }

    @Nested
    class ForgotPassword_Endpoint_Test_Cases {
        @Test
        @PreAuthorize("isAnonymous()")
        void ForgotPassword_Returns200() throws Exception {
            //Successful post test
            //given
            var forgotPasswordDto = new ForgotPasswordDto("Successful");
            var body = new ForgotPasswordRequest(eClientId.getWebClientId(),
                    "ali.veli@example.com");

            //when
            when(authService.forgotPassword(any()))
                    .thenReturn(Response.ok(forgotPasswordDto));
            ResultActions actions = mockMvc.perform(
                    post("/auth/forgot-password")
                            .contentType(CONTENT_TYPE)
                            .content(objectMapper.writeValueAsString(body))).andDo(print());

            //then
            ArgumentCaptor<ForgotPasswordRequest> captor = ArgumentCaptor.forClass(ForgotPasswordRequest.class);
            actions.andExpect(status().isOk());
            verify(authService).forgotPassword(captor.capture());
            assertThat(captor.getValue().getEmail()).isEqualTo(body.getEmail());
            assertThat(captor.getValue().getClientId()).isEqualTo(body.getClientId());
        }

        @Test
        @PreAuthorize("isAnonymous()")
        void ForgotPassword_Returns400() throws Exception {
            //given
            var forgotPasswordDto = new ForgotPasswordDto("Successful");
            var body = new ForgotPasswordRequest("11345",
                    "ali.veli@example.com");

            //when
            when(authService.forgotPassword(any()))
                    .thenReturn(Response.ok(forgotPasswordDto));
            ResultActions actions = mockMvc.perform(
                    post("/auth/forgot-password")
                            .contentType(CONTENT_TYPE)
                            .content(objectMapper.writeValueAsString(body))).andDo(print());

            //then
            actions.andExpect(status().isBadRequest());
        }

        @Test
        @PreAuthorize("isAnonymous()")
        void ForgotPassword_Returns404() throws Exception {
            //given
            var forgotPasswordDto = new ForgotPasswordDto("Successful");
            ForgotPasswordRequest body = new ForgotPasswordRequest(UserTestFactory.createRandomCode(),
                    "ali.veli@example.com");

            //when
            when(authService.forgotPassword((ForgotPasswordRequest) any()))
                    .thenReturn(Response.ok(forgotPasswordDto));
            ResultActions actions = mockMvc.perform(
                    post("/auths/forgot-password")
                            .contentType(CONTENT_TYPE)
                            .content(objectMapper.writeValueAsString(body))).andDo(print());

            //then
            actions.andExpect(status().isNotFound());
        }

        @Test
        @PreAuthorize("isAnonymous()")
        void ForgotPassword_Returns500() throws Exception {
            //given
            var body = new ForgotPasswordRequest(eClientId.getWebClientId(),
                    "ali.veli@example.com");

            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            ResultActions actions = mockMvc.perform(
                    post("/auth/forgot-password")
                            .contentType(CONTENT_TYPE)
                            .content(objectMapper.writeValueAsString(body))).andDo(print());

            //then
            actions.andExpect(status().isInternalServerError());
        }

    }

    @Nested
    class ResetPassword_Endpoint_Test_Cases {

        @Test
        @PreAuthorize("isAnonymous()")
        void ResetPassword_Returns200() throws Exception {
            //Successful post test
            //given
            var body = new ResetPasswordRequest("p4ssw0rd123", UserTestFactory.createRandomCode());
            var resetPasswordDto = new ResetPasswordDto("Successful");

            //when
            when(authService.resetPassword(any()))
                    .thenReturn(Response.ok(resetPasswordDto));
            ResultActions actions = mockMvc.perform(
                    post("/auth/reset-password")
                            .contentType(CONTENT_TYPE)
                            .content(objectMapper.writeValueAsString(body))).andDo(print());

            //then
            ArgumentCaptor<ResetPasswordRequest> captor = ArgumentCaptor.forClass(ResetPasswordRequest.class);
            actions.andExpect(status().isOk());
            verify(authService).resetPassword(captor.capture());
            assertThat(captor.getValue().getPassword()).isEqualTo(body.getPassword());
            assertThat(captor.getValue().getCode()).isEqualTo(body.getCode());

        }

        @Test
        @PreAuthorize("isAnonymous()")
        void ResetPassword_Returns400() throws Exception {
            //given
            var body = new ResetPasswordRequest();

            //when
            when(authService.resetPassword(any()))
                    .thenReturn(Response.notOk("400 Bad Request", EErrorCode.BAD_REQUEST));
            ResultActions actions = mockMvc.perform(
                    post("/auth/reset-password")
                            .contentType(CONTENT_TYPE)
                            .content(objectMapper.writeValueAsString(body))).andDo(print());

            //then
            actions.andExpect(status().isBadRequest());

        }

        @Test
        @PreAuthorize("isAnonymous()")
        void ResetPassword_Returns404() throws Exception {
            //given
            ResetPasswordRequest body = new ResetPasswordRequest("p4ssw0rd123", UserTestFactory.createRandomCode());

            //when
            when(authService.resetPassword((ResetPasswordRequest) any()))
                    .thenReturn(Response.notOk("404 Not Found", EErrorCode.BAD_REQUEST));
            ResultActions actions = mockMvc.perform(
                    post("/auths/reset-password")
                            .contentType(CONTENT_TYPE)
                            .content(objectMapper.writeValueAsString(body))).andDo(print());

            //then
            actions.andExpect(status().isNotFound());

        }

        @Test
        @PreAuthorize("isAnonymous()")
        void ResetPassword_Returns500() throws Exception {
            //given
            ResetPasswordRequest body = new ResetPasswordRequest("p4ssw0rd123", UserTestFactory.createRandomCode());


            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            ResultActions actions = mockMvc.perform(
                    post("/auth/reset-password")
                            .contentType(CONTENT_TYPE)
                            .content(objectMapper.writeValueAsString(body))).andDo(print());

            //then
            actions.andExpect(status().isInternalServerError());

        }
    }

    @Nested
    class Logout_Endpoint_Test_cases {

        @Test
        @WithMockUser("spring")
        void Logout_Returns200() throws Exception {
            //Successful post test
            //given
            var logoutResponse = new LogoutResponse("Successful");

            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            when(authService.logout(anyString(), anyString()))
                    .thenReturn(Response.ok(logoutResponse));
            ResultActions actions = mockMvc.perform(
                    get("/auth/logout")
                            .header(HttpHeaders.AUTHORIZATION, false)
                            .param("device_id", "device_id")).andDo(print());

            //then
            actions.andExpect(status().isOk());
            verify(authService).logout(anyString(), anyString());
        }

        @Test
        void Logout_Returns403() throws Exception {
            //given

            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            when(authService.logout(anyString(), anyString()))
                    .thenReturn(Response.notOk("403 Forbidden Error", EErrorCode.ACCESS_DENIED));
            ResultActions actions = mockMvc.perform(
                    get("/auth/logout")
                            .header(HttpHeaders.AUTHORIZATION, false)
                            .param("device_id", "device_id")).andDo(print());

            //then
            actions.andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser("spring")
        void Logout_Returns404() throws Exception {
            //given

            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            when(authService.logout(anyString(), anyString()))
                    .thenReturn(Response.notOk("404 Not Found", EErrorCode.NOT_FOUND));
            ResultActions actions = mockMvc.perform(
                    get("/auths/logout")
                            .header(HttpHeaders.AUTHORIZATION, false)
                            .param("device_id", "device_id")).andDo(print());

            //then
            actions.andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser("spring")
        void Logout_Returns500() throws Exception {
            //given

            //when
            when(messageSource.getMessage(any(), any(), any()))
                    .thenReturn("Message");
            ResultActions actions = mockMvc.perform(
                    get("/auth/logout")
                            .header(HttpHeaders.AUTHORIZATION, false)
                            .param("device_id", "device_id")).andDo(print());

            //then
            actions.andExpect(status().isInternalServerError());
        }
    }


}