package com.onlinestore.services;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.onlinestore.dto.BookInfoDto;
import com.onlinestore.entity.BookInfo;
import com.onlinestore.repository.BookInfoRepository;
import com.onlinestore.services.exceptions.BadRequestException;
import com.onlinestore.services.exceptions.BookAlreadyAvailable;
import com.onlinestore.services.exceptions.BookNotFoundException;
import com.onlinestore.utility.Response;

@Service
public class BookInfoServiceImp implements BookInfoService

{
	@Autowired
	private BookInfoRepository bookRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	@Transactional
	public CompletableFuture<Response> addNewBook(BookInfoDto bookinfodto) {
		Response response = new Response();
		try {
			Optional<BookInfo> bookById = bookRepository.findById(bookinfodto.getIsbn());
			bookById.ifPresent(book -> {
				throw new BookAlreadyAvailable(
						"Book with same id is already present, to add more of available books use different method");
			});

			if (!bookById.isPresent()) {
				BookInfo book = modelMapper.map(bookinfodto, BookInfo.class);
				book = bookRepository.save(book);
				response.setSuccess(true, 201L);
				response.addData(book);
				response.addMessage("Book Added Successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(false, 500L);
			response.addData(e.getLocalizedMessage());
			response.addMessage("Something went wrong");
		}
		return CompletableFuture.completedFuture(response);
	}

	@Transactional
	@Override
	public CompletableFuture<Response> addBook(Long isbn, BookInfoDto bookDto) {

		Response response = new Response();
		try {
			BookInfo book = bookRepository.findById(isbn).orElseThrow(() -> new BookNotFoundException(
					"Book with ISBN:" + isbn + " is not Present, For new ISBN add new book"));

			int totalquantityAfterAdd = book.getQuantity() + bookDto.getQuantity();
			book.setQuantity(totalquantityAfterAdd);
			book = bookRepository.save(book);
			response.setSuccess(true, 200L);
			response.addData(book);
			response.addMessage("Book Added Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(false, 500L);
			response.addData(e.getLocalizedMessage());
			response.addMessage("Something went wrong");
		}
		return CompletableFuture.completedFuture(response);
	}

	@Override
	public CompletableFuture<Response> getBookById(Long isbn) {
		Response response = new Response();
		try {
			BookInfo book = bookRepository.findById(isbn)
					.orElseThrow(() -> new BookNotFoundException("Book with ISBN:" + isbn + " is not found."));

			BookInfoDto bookdto = modelMapper.map(book, BookInfoDto.class);

			response.setSuccess(true, 200L);
			response.addData(bookdto);
			response.addMessage("Book Fetched Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(false, 500L);
			response.addData(e.getLocalizedMessage());
			response.addMessage("Something went wrong");
		}
		return CompletableFuture.completedFuture(response);

	}

	@Override
	public CompletableFuture<Response> getAllBooks() {
		Response response = new Response();
		List<BookInfo> books = bookRepository.findAll();

		response.setSuccess(true, 200L);
		response.addData(mapBookInfoListToBookInfoDtoList(books));
		response.addMessage("Book Fetched Successfully");

		return CompletableFuture.completedFuture(response);
	}

	public CompletableFuture<Response> findBySearchTerm(String searchTerm) {
		Response response = new Response();
		Specification<BookInfo> searchSpec = BookSearchSpecification.titleOrAuthorContainsIgnoreCase(searchTerm);

		List<BookInfo> books = bookRepository.findAll(searchSpec);
		response.setSuccess(true, 200L);
		response.addData(mapBookInfoListToBookInfoDtoList(books));
		response.addMessage("Book Fetched Successfully");

		return CompletableFuture.completedFuture(response);
	}

	@Override
	@Transactional
	public CompletableFuture<Response> buyBook(Long isbn, BookInfoDto bookDto) {
		Response response = new Response();
		BookInfo book = bookRepository.findById(isbn)

				.orElseThrow(() -> new BookNotFoundException("Book with id: " + isbn + " is not found."));
		try {
			int totalCount = book.getQuantity() - bookDto.getQuantity();
			if (totalCount < 0) {
				throw new BadRequestException("You can not buy more book then available stock");
			}

			book.setQuantity(totalCount);
			book.setSold(bookDto.getQuantity());
			book = bookRepository.save(book);

			response.setSuccess(true, 200L);
			response.addData(book);
			response.addMessage("Book Buyed Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(false, 500L);
			response.addData(e.getLocalizedMessage());
			response.addMessage("Something went wrong");
		}
		return CompletableFuture.completedFuture(response);
	}

	@Override
	@Transactional
	public CompletableFuture<Response> removeBook(Long isbn) {
		Response response = new Response();
		try {
			Optional<BookInfo> bookById = bookRepository.findById(isbn);
			bookById.ifPresent(book -> {
				bookRepository.deleteById(isbn);
				response.setSuccess(true, 200L);
				response.addData(book);
				response.addMessage("Book Deleted Successfully");
			});

			if (!bookById.isPresent()) {

				throw new BookNotFoundException("Book Can Not Be Deleted, Book with id " + isbn + " is not present");

			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(false, 500L);
			response.addData(e.getLocalizedMessage());
			response.addMessage("Something went wrong");
		}
		return CompletableFuture.completedFuture(response);
	}

	private List<BookInfoDto> mapBookInfoListToBookInfoDtoList(List<BookInfo> books) {
		return books.stream().map(book -> modelMapper.map(book, BookInfoDto.class)).collect(Collectors.toList());
	}

}
