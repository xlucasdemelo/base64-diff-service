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

/**
 * Class with the integration tests for the application
 * 
 * @author lucas
 *
 */
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DiffServiceApplication.class)
public class DiffIntegrationTests {
	
	@Autowired
    MockMvc mockMvc;
	
	/**
	 * This will test the creation of a Diff object with only the Left direction
	 * 
	 * @throws Exception
	 */
    @Test
    @Sql(scripts = "classpath:data/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void sendLeftPayload_ShouldReturnCreatedDiff() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/diff/103/left")
                .content(Base64.getEncoder().encodeToString("abcd".getBytes()))
                .contentType(MediaType.APPLICATION_JSON)
                
        )
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().json("{\"id\":103,\"leftDirection\":\"YWJjZA==\",\"rightDirection\":\"abc\"}"));
    }
    
    /**
	 * This will test the creation of a Diff object with only the right direction
	 * 
	 * @throws Exception
	 */
    @Test
    public void sendRightPayload_ShouldReturnDiffWithRightValue() throws Exception {
    	this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/diff/1/right")
                .content(Base64.getEncoder().encodeToString("abcd".getBytes()))
                .contentType(MediaType.APPLICATION_JSON)
                
        )
    	.andExpect( status().is2xxSuccessful())
    	.andExpect(content().json("{\"id\":1,\"leftDirection\":null,\"rightDirection\":\"YWJjZA==\"}"));
    }
    
    /**
	 * This will test the override of a direction
	 * The application must return a exception
	 * 
	 * @throws Exception
	 */
    @Test
    @Sql(scripts = "classpath:data/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void sendRightPayloadTryingToOverrideADirection_ShouldThrowAnException() throws Exception {
    	this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/diff/100/right")
                .content(Base64.getEncoder().encodeToString("abcd".getBytes()))
                .contentType(MediaType.APPLICATION_JSON)
                
        )
    	.andExpect( status().is4xxClientError());
    }
    
    /**
	 * This will test the diff operation of two directions 
	 * The application must return a message "EQUALS"
	 * 
	 * @throws Exception
	 */
    @Test
    @Sql(scripts = "classpath:data/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void sendDiffRequest_ShouldReturnEqualsPayloads() throws Exception {
    	this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/diff/100")
        )
    	.andExpect( status().is2xxSuccessful())
    	.andExpect(content().json("{\"reason\":\"EQUALS\",\"offsets\":[]}"));
    }
    
    /**
	 * This will test the diff operation of two directions 
	 * The application must return a message "NOT_EQUAL_SIZES"
	 * 
	 * @throws Exception
	 */
    @Test
    @Sql(scripts = "classpath:data/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void sendDiffRequest_ShouldReturnDifferentSize() throws Exception {
    	this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/diff/101")
        )
    	.andExpect( status().is2xxSuccessful())
    	.andExpect(content().json("{\"reason\":\"NOT_EQUAL_SIZES\",\"offsets\":[]}"));
    }
    
    /**
   	 * This will test the diff operation of two directions 
   	 * The application must return a message "DIFFERENT_PAYLOADS" and a list of offsets
   	 * 
   	 * @throws Exception
   	 */
    @Test
    @Sql(scripts = "classpath:data/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void sendDiffRequest_ShouldReturnDifferentPayloads() throws Exception {
    	this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/diff/102")
        )
    	.andExpect( status().is2xxSuccessful())
    	.andExpect(content().json("{\"reason\":\"DIFFERENT_PAYLOADS\",\"offsets\":[{\"offset\":2,\"length\":2},{\"offset\":6}]}"));
    }
    
    /**
   	 * This will test the diff operation for a non existent ID
   	 * The application must return a exception
   	 * 
   	 * @throws Exception
   	 */
    @Test
    @Sql(scripts = "classpath:data/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void sendDiffForNonExistentId_ShouldThrowAnException() throws Exception {
    	this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/diff/999")
        )
    	.andExpect( status().is4xxClientError());
    }
    
    /**
   	 * This will test the diff operation for a Diff with one of the directions NULL
   	 * The application must return a exception
   	 * 
   	 * @throws Exception
   	 */
    @Test
    @Sql(scripts = "classpath:data/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void sendDiffForNullDirectionInDiff_ShouldThrowAnException() throws Exception {
    	this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/diff/103")
        )
    	.andExpect( status().is4xxClientError());
    }
}

