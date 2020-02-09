package com.lucas.waes.diffservice.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Base64;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
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
    @Sql(scripts = "classpath:data/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void sendLeftPayloadShouldReturnCreatedDiff() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/diff/103/left")
                .content(Base64.getEncoder().encodeToString("abcd".getBytes()))
                .contentType(MediaType.APPLICATION_JSON)
                
        )
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().json("{\"id\":103,\"leftDirection\":\"YWJjZA==\",\"rightDirection\":\"abc\"}"));
    }
    
    @Test
    public void sendRightPayloadShouldReturnDiffWithRightValue() throws Exception {
    	this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/diff/1/right")
                .content(Base64.getEncoder().encodeToString("abcd".getBytes()))
                .contentType(MediaType.APPLICATION_JSON)
                
        )
    	.andExpect( status().is2xxSuccessful())
    	.andExpect(content().json("{\"id\":1,\"leftDirection\":null,\"rightDirection\":\"YWJjZA==\"}"));
    }
    
    @Test
    @Sql(scripts = "classpath:data/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void sendRightPayloadTryingToOverrideADirectionShouldThrowAnException() throws Exception {
    	this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/diff/100/right")
                .content(Base64.getEncoder().encodeToString("abcd".getBytes()))
                .contentType(MediaType.APPLICATION_JSON)
                
        )
    	.andExpect( status().is4xxClientError());
    }
    
    @Test
    @Sql(scripts = "classpath:data/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void sendDiffRequestShouldReturnEqualsPayloads() throws Exception {
    	this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/diff/100")
        )
    	.andExpect( status().is2xxSuccessful())
    	.andExpect(content().json("{\"reason\":\"EQUALS\",\"offsets\":[]}"));
    }
    
    @Test
    @Sql(scripts = "classpath:data/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void sendDiffRequestShouldReturnDifferentSize() throws Exception {
    	this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/diff/101")
        )
    	.andExpect( status().is2xxSuccessful())
    	.andExpect(content().json("{\"reason\":\"NOT_EQUAL_SIZES\",\"offsets\":[]}"));
    }
    
    @Test
    @Sql(scripts = "classpath:data/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void sendDiffRequestShouldReturnDifferentPayloads() throws Exception {
    	this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/diff/102")
        )
    	.andExpect( status().is2xxSuccessful())
    	.andExpect(content().json("{\"reason\":\"DIFFERENT_PAYLOADS\",\"offsets\":[{\"offset\":2,\"length\":2},{\"offset\":6}]}"));
    }
    
    @Test
    @Sql(scripts = "classpath:data/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void sendDiffForNonExistentIdShouldThrowAnException() throws Exception {
    	this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/diff/999")
        )
    	.andExpect( status().is4xxClientError());
    }
    
    @Test
    @Sql(scripts = "classpath:data/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void sendDiffForNullDirectionInDiffShouldThrowAnException() throws Exception {
    	this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/diff/103")
        )
    	.andExpect( status().is4xxClientError());
    }
}

