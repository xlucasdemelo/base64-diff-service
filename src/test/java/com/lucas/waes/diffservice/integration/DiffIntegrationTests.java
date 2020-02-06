package com.lucas.waes.diffservice.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Base64;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.lucas.waes.diffservice.DiffServiceApplication;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DiffServiceApplication.class)
public class DiffIntegrationTests {
	
	@Autowired
    MockMvc mockMvc;

    @Test
    public void sendLeftPayloadShouldReturnDiff() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/diff/100/left")
                .content(Base64.getEncoder().encodeToString("abcd".getBytes()))
                .contentType(MediaType.APPLICATION_JSON)
                
        ).andExpect(status().is2xxSuccessful());
    }
	
}
