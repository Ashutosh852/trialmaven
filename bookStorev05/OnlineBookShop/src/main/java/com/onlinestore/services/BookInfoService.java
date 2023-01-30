package com.onlinestore.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;

import com.onlinestore.dto.BookInfoDto;
import com.onlinestore.utility.Response;

public interface BookInfoService {

	@Async
	public CompletableFuture<Response> addNewBook(BookInfoDto bookinfodto);

	@Async
	public CompletableFuture<Response> addBook(Long id, BookInfoDto book);

	@Async
	public CompletableFuture<Response> getBookById(Long isbn);

	@Async
	public CompletableFuture<Response> getAllBooks();

	@Async
	public CompletableFuture<Response> findBySearchTerm(String searchTerm);

	@Async
	public CompletableFuture<Response> buyBook(Long isbn, BookInfoDto book);

	@Async
	public CompletableFuture<Response> removeBook(Long isbn);

}
