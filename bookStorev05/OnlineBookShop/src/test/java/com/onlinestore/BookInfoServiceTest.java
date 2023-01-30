package com.onlinestore;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;

import com.onlinestore.dto.BookInfoDto;
import com.onlinestore.entity.BookInfo;
import com.onlinestore.repository.BookInfoRepository;
import com.onlinestore.services.BookInfoServiceImp;
import com.onlinestore.services.BookSearchSpecification;
import com.onlinestore.utility.Response;

@ExtendWith(MockitoExtension.class)
//@WebMvcTest(BookInfoServiceTest.class)
//@ActiveProfiles("test")
public class BookInfoServiceTest {

	@Mock
	private BookInfoRepository bookRepository;
	@Spy
	private ModelMapper modelMapper = new ModelMapper();
	@InjectMocks
	private BookInfoServiceImp service;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);

	}

	BookInfoDto book1 = new BookInfoDto(121L, "title", "Vk Kumavat", 2, 100);
	BookInfoDto book2 = new BookInfoDto(122L, "Sciense", "Rk Joshi", 4, 190);
	BookInfoDto book3 = new BookInfoDto(123L, "Maths", "Vk Mishra", 6, 160);

	BookInfo bookInfo1 = new BookInfo(121L, "title", "Vk Kumavat", 2, 100, 1);
	BookInfo bookInfo2 = new BookInfo(122L, "Sciense", "Rk Joshi", 4, 190, 5);
	BookInfo bookInfo3 = new BookInfo(123L, "Maths", "Vk Mishra", 6, 160, 9);

	@Test

	public void TestAddNewBook_BookAlreadyExist() throws Exception {

		Mockito.when(bookRepository.findById(book1.getIsbn())).thenReturn(Optional.of(bookInfo1));

		Response response = new Response();
		CompletableFuture<Response> future = service.addNewBook(book1);
		response = future.get();
		Assertions.assertEquals(response.getStatuscode(), 500L);
		Assertions.assertEquals(response.getSuccess(), false);

	}

	@Test
	public void TestAddBookNewBookAdded() throws Exception {

		Mockito.when(bookRepository.findById(book1.getIsbn())).thenReturn(Optional.empty());
		Mockito.when(modelMapper.map(book1, BookInfo.class)).thenReturn(bookInfo1);
		Mockito.when(bookRepository.save(bookInfo1)).thenReturn(bookInfo1);

		Response response = new Response();
		CompletableFuture<Response> future = service.addNewBook(book1);
		response = future.get();
		Assertions.assertEquals(response.getStatuscode(), 201L);
		Assertions.assertEquals(response.getSuccess(), true);

	}

	@Test
	public void TestAddBookAdded() throws Exception {
		Long isbn = 121L;
		Mockito.when(bookRepository.findById(isbn)).thenReturn(Optional.of(bookInfo1));
		Mockito.when(bookRepository.save(bookInfo1)).thenReturn(bookInfo1);

		Response response = new Response();
		CompletableFuture<Response> future = service.addBook(isbn, book1);
		response = future.get();
		Assertions.assertEquals(response.getStatuscode(), 200L);
		Assertions.assertEquals(response.getSuccess(), true);

	}

	@Test
	public void TestAddBook_BookNotExist() throws Exception {
		Long isbn = 121L;
		Mockito.when(bookRepository.findById(isbn)).thenReturn(Optional.empty());

		Response response = new Response();
		CompletableFuture<Response> future = service.addBook(isbn, book1);
		response = future.get();
		Assertions.assertEquals(response.getStatuscode(), 500L);
		Assertions.assertEquals(response.getSuccess(), false);

	}

	@Test
	public void TestgetBookByIdAvailable() throws Exception {

		Mockito.when(bookRepository.findById(bookInfo1.getIsbn())).thenReturn(Optional.of(bookInfo1));

		Response response = new Response();
		CompletableFuture<Response> future = service.getBookById(bookInfo1.getIsbn());
		response = future.get();
		Assertions.assertEquals(response.getStatuscode(), 200L);
		Assertions.assertEquals(response.getSuccess(), true);

	}

	@Test
	public void TestgetBookByIdNotAvailable() throws Exception {

		Mockito.when(bookRepository.findById(bookInfo1.getIsbn())).thenReturn(Optional.empty());

		Response response = new Response();
		CompletableFuture<Response> future = service.getBookById(bookInfo1.getIsbn());
		response = future.get();
		Assertions.assertEquals(response.getStatuscode(), 500L);
		Assertions.assertEquals(response.getSuccess(), false);

	}

	@Test
	public void TestgetallBook() throws Exception {

		List<BookInfo> books = Arrays.asList(bookInfo1, bookInfo2, bookInfo3);
		Mockito.when(bookRepository.findAll()).thenReturn(books);

		Response response = new Response();
		CompletableFuture<Response> future = service.getAllBooks();
		response = future.get();
		Assertions.assertEquals(response.getStatuscode(), 200L);
		Assertions.assertEquals(response.getSuccess(), true);

	}

	@Test
	public void TestgetallBookWhenTableIsEmpty() throws Exception {
		List<BookInfo> books = Arrays.asList();
		Mockito.when(bookRepository.findAll()).thenReturn(books);

		Response response = new Response();
		CompletableFuture<Response> future = service.getAllBooks();
		response = future.get();
		Assertions.assertEquals(response.getStatuscode(), 200L);
		Assertions.assertEquals(response.getSuccess(), true);

	}

	@Test
	public void TestfindBySearchTerm() throws Exception {

		String searchTerm = "Vk";
		Specification<BookInfo> searchSpec = BookSearchSpecification.titleOrAuthorContainsIgnoreCase(searchTerm);
		List<BookInfo> books = Arrays.asList(bookInfo1, bookInfo3);
		Mockito.when(bookRepository.findAll(searchSpec)).thenReturn(books);
		try (MockedStatic mocked = mockStatic(BookSearchSpecification.class)) {
			mocked.when(() -> BookSearchSpecification.titleOrAuthorContainsIgnoreCase(searchTerm))
					.thenReturn(searchSpec);

			Response response = new Response();
			CompletableFuture<Response> future = service.findBySearchTerm(searchTerm);

			response = future.get();
			Assertions.assertEquals(response.getStatuscode(), 200L);
			Assertions.assertEquals(response.getSuccess(), true);
		}
	}

	@Test
	public void testbuyBook_successful() throws Exception {

		Mockito.when(bookRepository.findById(bookInfo1.getIsbn())).thenReturn(Optional.of(bookInfo1));
		Mockito.when(bookRepository.save(bookInfo1)).thenReturn(bookInfo1);
		Response response = new Response();
		CompletableFuture<Response> future = service.buyBook(bookInfo1.getIsbn(), book1);
		response = future.get();
		Assertions.assertEquals(response.getStatuscode(), 200L);
		Assertions.assertEquals(response.getSuccess(), true);

	}

	@Test
	public void testbuyBookBuyingMoreBookThenAvailable() throws Exception {

		Mockito.when(bookRepository.findById(bookInfo1.getIsbn())).thenReturn(Optional.of(bookInfo1));
		Response response = new Response();
		book1.setQuantity(100);
		CompletableFuture<Response> future = service.buyBook(book1.getIsbn(), book1);
		response = future.get();
		Assertions.assertEquals(response.getStatuscode(), 500L);
		Assertions.assertEquals(response.getSuccess(), false);
		String message = (String) response.getData();
		Assertions.assertTrue("You can not buy more book then available stock".contains(message));

	}

	@Test
	public void testDeleteBookById() throws Exception {

		Mockito.when(bookRepository.findById(bookInfo1.getIsbn())).thenReturn(Optional.of(bookInfo1));
		doNothing().when(bookRepository).deleteById(bookInfo1.getIsbn());
		Response response = new Response();
		CompletableFuture<Response> future = service.removeBook(bookInfo1.getIsbn());
		response = future.get();
		Assertions.assertEquals(response.getStatuscode(), 200L);
		Assertions.assertEquals(response.getSuccess(), true);
		String message = response.getMessage();
		Assertions.assertTrue("Book Deleted Successfully".contains(message));

	}

	@Test
	public void testDeleteBookByIdWhereBookIsNotPresent() throws Exception {

		Mockito.when(bookRepository.findById(bookInfo1.getIsbn())).thenReturn(Optional.empty());
		Response response = new Response();
		CompletableFuture<Response> future = service.removeBook(bookInfo1.getIsbn());
		response = future.get();
		Assertions.assertEquals(response.getStatuscode(), 500L);
		Assertions.assertEquals(response.getSuccess(), false);

	}
}
