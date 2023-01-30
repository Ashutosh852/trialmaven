package com.onlinestore.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.onlinestore.services.exceptions.BookAlreadyAvailable;
import com.onlinestore.utility.Response;

@RestControllerAdvice
//@Component
public class BookInfoControllerAdvice {

	@ExceptionHandler(BookAlreadyAvailable.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Response BookAlreadyAvailableHandler(Exception e) {
		Response response = new Response();
		response.setSuccess(false, 500L);
		response.addData(e.getLocalizedMessage());
		response.addMessage("Book with same id is already present");

		return response;

	}

	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Response exceptionHandler(Exception e) {
		Response response = new Response();
		response.setSuccess(false, 500L);
		response.addData(e.getLocalizedMessage());
		response.addMessage("Something went wrong during adding new book");

		return response;

	}

}
