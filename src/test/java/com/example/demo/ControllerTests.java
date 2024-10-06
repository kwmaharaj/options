package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerTests {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * Tests for options + schema
	 * @throws Exception
	 */
	@Test
	public void shouldReturnOptionsWithSchema() throws Exception {
		MvcResult mvcResult = mockMvc.perform(options("http://localhost:8080/v1/cats"))
				.andExpect(status().isOk())
				.andExpect(header().string("Allow", containsString("GET,POST,PUT,DELETE,OPTIONS")))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string(containsString("\"properties\"")))
				.andExpect(content().string(containsString("\"id\"")))
				.andExpect(content().string(containsString("\"name\"")))
				.andExpect(content().string(containsString("\"string\""))).andReturn();

		System.out.println("Schema:" + mvcResult.getResponse().getContentAsString());
	}

}
