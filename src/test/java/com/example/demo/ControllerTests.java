package com.example.demo;

import com.example.demo.controller.CatController;
import com.example.demo.model.Cat;
import com.example.demo.service.CatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private CatService catService;

	@Autowired
	private CatController catController;

	@BeforeEach
	void setUp() {
		catService = Mockito.mock(CatService.class);
		catController = new CatController(catService);
		mockMvc = MockMvcBuilders.standaloneSetup(catController).build();
	}

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
	}


	/**
	 * Tests for create ok
	 * @throws Exception
	 */
	@Test
	void createCat_Ok() throws Exception {
		// Given
		String catName = "GreenCat";
		Cat expectedCat = new Cat(UUID.randomUUID(), catName);
		when(catService.createCat(catName)).thenReturn(expectedCat);

		// When
		ResponseEntity<Cat> response = catController.createCat(catName);

		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedCat.getName(), response.getBody().getName());
		assertNotNull(response.getBody().getId());
	}

	@Test
	void createCat_Error() throws Exception {
		// Given
		String catName = "BlueCat";
		String errorMessage = "Database error";
		doThrow(new RuntimeException(errorMessage)).when(catService).createCat(anyString());

		// When
		ResponseEntity<Cat> response = catController.createCat(catName);

		// Then
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	void getCatByID_Success() {
		// Given
		UUID catId = UUID.randomUUID();
		Cat expectedCat = new Cat(catId, "OrangeCat");
		when(catService.getCatById(catId)).thenReturn(expectedCat);

		// When
		ResponseEntity<Cat> response = catController.getCatByID(catId);

		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedCat.getName(), response.getBody().getName());
		assertEquals(expectedCat.getId(), response.getBody().getId());
	}

	@Test
	void getCatByID_NotFound() {
		// Given
		UUID catId = UUID.randomUUID();
		String errorMessage = "Cat not found";
		doThrow(new RuntimeException(errorMessage)).when(catService).getCatById(catId);

		// When
		ResponseEntity<Cat> response = catController.getCatByID(catId);

		// Then
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void updateCat_Success() {
		// Given
		UUID catId = UUID.randomUUID();
		Cat catToUpdate = new Cat(catId, "PurpleCat");
		Cat updatedCat = new Cat(catId, "PurpleCat Updated");

		when(catService.updateCat(catToUpdate)).thenReturn(updatedCat);

		// When
		ResponseEntity<Cat> response = catController.updateCat(catToUpdate);

		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(updatedCat.getName(), response.getBody().getName());
		assertEquals(updatedCat.getId(), response.getBody().getId());
	}

	@Test
	void updateCat_NotFound() {
		// Given
		UUID catId = UUID.randomUUID();
		Cat catToUpdate = new Cat(catId, "PurpleCat");
		String errorMessage = "Cat not found";
		doThrow(new RuntimeException(errorMessage)).when(catService).updateCat(catToUpdate);

		// When
		ResponseEntity<Cat> response = catController.updateCat(catToUpdate);

		// Then
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}


	@Test
	void deleteCat_Success() {
		// Given
		UUID catId = UUID.randomUUID();
		String expectedResponse = "Cat id(%s) is deleted".formatted(catId);
		when(catService.deleteCat(catId)).thenReturn(expectedResponse);

		// When
		ResponseEntity<String> response = catController.deleteCat(catId);

		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedResponse, response.getBody());
	}

	@Test
	void deleteCat_NotFound() {
		// Given
		UUID catId = UUID.randomUUID();
		String errorMessage = "Cat not found";
		doThrow(new RuntimeException(errorMessage)).when(catService).deleteCat(catId);

		// When
		ResponseEntity<String> response = catController.deleteCat(catId);

		// Then
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

}
