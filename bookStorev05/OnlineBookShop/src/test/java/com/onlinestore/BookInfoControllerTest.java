package com.onlinestore;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.onlinestore.advice.BookInfoControllerAdvice;
import com.onlinestore.controller.BookInfoController;
import com.onlinestore.dto.BookInfoDto;
import com.onlinestore.services.BookInfoService;
import com.onlinestore.services.exceptions.BookAlreadyAvailable;
import com.onlinestore.services.exceptions.BookNotFoundException;
import com.onlinestore.utility.Response;

@ExtendWith(MockitoExtension.class)
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(BookInfoControllerTest.class)
public class BookInfoControllerTest {

	@Autowired
	MockMvc mockMvc;

	ObjectMapper mapper = new ObjectMapper();
	ObjectWriter objectWriter = mapper.writer();


	@Mock
	private BookInfoService service;

	@InjectMocks
	private BookInfoController con;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(con).setControllerAdvice(new BookInfoControllerAdvice()).build();
	}

	BookInfoDto book1 = new BookInfoDto(121L, "title", "Vk Kumavat", 2, 100);
	BookInfoDto book2 = new BookInfoDto(122L, "Sciense", "Rk Joshi", 4, 190);
	BookInfoDto book3 = new BookInfoDto(123L, "Maths", "Vk Mishra", 6, 160);

	@Test
	public void testAllBookTest_Success() throws Exception {
		List<BookInfoDto> books = new ArrayList(Arrays.asList(book1, book2, book3));
		Response response = new Response();
		response.setSuccess(true, 200L);
		response.addData(books);
		response.addMessage("Book Fetched Successfully");
		Mockito.when(service.getAllBooks()).thenReturn(CompletableFuture.completedFuture(response));
		mockMvc.perform(MockMvcRequestBuilders.get("/book").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.statuscode", is(200)));

	}

	@Test
	public void testFindBookByIsbn_Success() throws Exception {

		Response response = new Response();
		response.setSuccess(true, 200L);
		response.addData(book1);
		response.addMessage("Book Fetched Successfully");
		Mockito.when(service.getBookById(book1.getIsbn())).thenReturn(CompletableFuture.completedFuture(response));
		mockMvc.perform(MockMvcRequestBuilders.get("/book/121").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.statuscode", is(200)));

	}

	@Test
	public void testAddNewBookWithException() throws Exception {

		Mockito.doThrow(BookAlreadyAvailable.class).when(service).addNewBook(any(BookInfoDto.class));

		String content = objectWriter.writeValueAsString(book1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/book")
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON)
				.content(content);

		mockMvc.perform(mockRequest).andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.message").value("Book with same id is already present"));

	}

	@Test
	public void testAddNewBook_Success() throws Exception {

		Response response = new Response();
		response.setSuccess(true, 201L);
		response.addData(book1);
		response.addMessage("Book Added Successfully");

		when(service.addNewBook(any(BookInfoDto.class))).thenReturn(CompletableFuture.completedFuture(response));

		String content = objectWriter.writeValueAsString(book1);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/book")
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON)
				.content(content);

		mockMvc.perform(mockRequest).andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.message").value("Book Added Successfully"));

	}

	@Test
	public void testBookByAuthorOrTitle_Sucess() throws Exception {
		List<BookInfoDto> books = new ArrayList(Arrays.asList(book1, book3));
		Response response = new Response();
		response.setSuccess(true, 200L);
		response.addData(books);
		response.addMessage("Book Fetched Successfully");
		Mockito.when(service.findBySearchTerm("Vk")).thenReturn(CompletableFuture.completedFuture(response));
		mockMvc.perform(MockMvcRequestBuilders.get("/book/search/Vk").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.statuscode", is(200)));

	}

	@Test
	public void testAddBookWithException() throws Exception {

		Mockito.doThrow(BookNotFoundException.class).when(service).addBook(anyLong(), any(BookInfoDto.class));

		String content = objectWriter.writeValueAsString(book1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/book/121")
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON)
				.content(content);

		mockMvc.perform(mockRequest).andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.statuscode").value("500"));

	}

	@Test
	public void testAddBook_Success() throws Exception {

		Response response = new Response();
		response.setSuccess(true, 201L);
		response.addData(book1);
		response.addMessage("Book Added Successfully");

		when(service.addBook(anyLong(), any(BookInfoDto.class)))
				.thenReturn(CompletableFuture.completedFuture(response));
		String content = objectWriter.writeValueAsString(book1);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/book/121")
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON)
				.content(content);

		mockMvc.perform(mockRequest).andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.message").value("Book Added Successfully"));

	}

	@Test
	public void testBuyBookWithException() throws Exception {

		Mockito.doThrow(BookNotFoundException.class).when(service).buyBook(anyLong(), any(BookInfoDto.class));

		book1.setSold(4);
		String content = objectWriter.writeValueAsString(book1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/book/buy/121")
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON)
				.content(content);

		mockMvc.perform(mockRequest).andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.statuscode").value("500"));
	}

	@Test
	public void testBuyBook_Success() throws Exception {
		book1.setSold(4);
		Response response = new Response();
		response.setSuccess(true, 200L);
		response.addData(book1);
		response.addMessage("Book Buyed Successfully");
		String content = objectWriter.writeValueAsString(book1);
		Mockito.when(service.buyBook(anyLong(), any(BookInfoDto.class)))
				.thenReturn(CompletableFuture.completedFuture(response));
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/book/buy/121")
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON)
				.content(content);

		mockMvc.perform(mockRequest).andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.statuscode").value("200"));

	}

	@Test
	public void testDeleteBookWithException() throws Exception {

		Mockito.doThrow(BookNotFoundException.class).when(service).removeBook(121L);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/book/121")
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(mockRequest).andExpect(jsonPath("$.success", is(false)))
				.andExpect(jsonPath("$.statuscode").value("500"));
	}

	@Test
	public void testDeleteBookById_Success() throws Exception {

		Response response = new Response();
		response.setSuccess(true, 200L);
		response.addData(book1);
		response.addMessage("Book Deleted Successfully");

		Mockito.when(service.removeBook(anyLong())).thenReturn(CompletableFuture.completedFuture(response));
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/book/121")
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(mockRequest).andExpect(jsonPath("$.success", is(true)))
				.andExpect(jsonPath("$.statuscode").value("200"));

	}

}
