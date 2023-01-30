package com.onlinestore.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.onlinestore.dto.BookInfoDto;
import com.onlinestore.services.BookInfoService;
import com.onlinestore.utility.Response;

@RestController
public class BookInfoController {
	@Autowired
	BookInfoService service;

	@PostMapping(value = "/book")

	public Response addnewBook(@RequestBody BookInfoDto book) throws InterruptedException, ExecutionException {

		return service.addNewBook(book).get();

	}

	@GetMapping("/book/{ISBN}")
	public Response findBookByIsbn(@PathVariable Long ISBN) throws InterruptedException, ExecutionException {

		return service.getBookById(ISBN).get();

	}

	@GetMapping("/book")
	public Response allBook() throws InterruptedException, ExecutionException {

		return service.getAllBooks().get();

	}

	@PutMapping("/book/{isbn}")
	public Response addBook(@PathVariable Long isbn, @RequestBody BookInfoDto book)
			throws InterruptedException, ExecutionException {

		return service.addBook(isbn, book).get();

	}

	@GetMapping("/book/search/{keyword}")
	public Response BookByauthorOrTitle(@PathVariable String keyword) throws InterruptedException, ExecutionException {
		Response response = new Response();

		response = service.findBySearchTerm(keyword).get();

		return response;

	}

	@PutMapping("/book/buy/{isbn}")
	public Response BuyBook(@PathVariable Long isbn, @RequestBody BookInfoDto book)
			throws InterruptedException, ExecutionException {
		

		return service.buyBook(isbn, book).get();

	}

	@DeleteMapping(value = "/book/{isbn}")

	public Response deleteBookById(@PathVariable Long isbn) throws InterruptedException, ExecutionException {
		Response response = new Response();

		response = service.removeBook(isbn).get();

		return response;
	}

}
