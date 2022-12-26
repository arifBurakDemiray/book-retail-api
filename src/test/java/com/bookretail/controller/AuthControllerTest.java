package com.bookretail.controller;

import com.bookretail.dto.auth.LoginRequest;
import com.bookretail.enums.EClientId;
import com.bookretail.enums.EGrantType;
import com.bookretail.enums.ERole;
import com.bookretail.factory.UserTestFactory;
import com.bookretail.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(AuthController.class)
@Disabled
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private EClientId eClientId;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;

    @InjectMocks
    AuthController authController;

    @Before(value = "test")
    public void setup() {

        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void Login_Fail_UserRole_WebClient() throws Exception {

        var user = UserTestFactory.createFailTestUserWithRole(ERole.USER);

        LoginRequest loginRequest = new LoginRequest(
                EGrantType.password,
                eClientId.getWebClientId(),
                "none",
                user.getEmail(),
                user.getPassword(),
                "null"
        );

        // then
        String url = "/auth/login";
        RequestBuilder request = post(url).contentType(MediaType.MULTIPART_FORM_DATA)
                .content(objectMapper.writeValueAsString(loginRequest));

        mockMvc.perform(request).andExpect(status().isOk());
    }
}



