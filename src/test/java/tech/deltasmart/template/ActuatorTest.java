package com.bookretail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ActuatorTest {
    @Value("${management.endpoints.web.base-path}")
    private String managementBaseUrl;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void Logfile_AccessDenied_WithoutToken() throws Exception {
        mockMvc.perform(get(managementBaseUrl + "/" + "logfile")).andExpect(status().isUnauthorized());
    }

    @Test
    public void Health_AccessDenied_WithoutToken() throws Exception {
        mockMvc.perform(get(managementBaseUrl + "/" + "health")).andExpect(status().isUnauthorized());
    }

    @Test
    public void Loggers_AccessDenied_WithoutToken() throws Exception {
        mockMvc.perform(get(managementBaseUrl + "/" + "loggers")).andExpect(status().isUnauthorized());
    }
}
