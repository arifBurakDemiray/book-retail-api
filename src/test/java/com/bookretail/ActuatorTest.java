package com.bookretail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ActuatorTest {
    @Value("${management.endpoints.web.base-path}")
    private String managementBaseUrl;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void Logfile_AccessDenied_WithoutToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(managementBaseUrl + "/" + "logfile")).andExpect(MockMvcResultMatchers.status().is(403));
    }

    @Test
    public void Health_AccessDenied_WithoutToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(managementBaseUrl + "/" + "health")).andExpect(MockMvcResultMatchers.status().is(403));
    }

    @Test
    public void Loggers_AccessDenied_WithoutToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(managementBaseUrl + "/" + "loggers")).andExpect(MockMvcResultMatchers.status().is(403));
    }
}
