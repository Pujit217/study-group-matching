package edu.ncsu.acm.studygroup.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests the health-check API endpoint.
 */
@SpringBootTest
@AutoConfigureMockMvc
class HealthControllerTest {

    /**
     * MockMvc allows HTTP endpoint testing without opening a real browser.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Verifies that the health endpoint returns HTTP 200
     * and the expected JSON response.
     *
     * @throws Exception if the request cannot be completed
     */
    @Test
    void healthEndpointReturnsSuccess() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.message")
                        .value("Study Group Matching API is running"));
    }
}